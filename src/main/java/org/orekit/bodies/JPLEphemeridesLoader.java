/* Copyright 2002-2008 CS Communication & Systèmes
 * Licensed to CS Communication & Systèmes (CS) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * CS licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.orekit.bodies;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.orekit.data.DataLoader;
import org.orekit.data.DataProvidersManager;
import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.ChronologicalComparator;
import org.orekit.time.TimeScalesFactory;
import org.orekit.time.TimeStamped;

/** Loader for JPL ephemerides binary files (DE 405, DE 406).
 * <p>JPL ephemerides binary files contain ephemerides for all solar system planets.</p>
 * <p>The JPL ephemerides binary files are recognized thanks to their base names,
 * which must match the pattern <code>unx[mp]####.ddd</code> (or
 * <code>unx[mp]####.ddd.gz</code> for gzip-compressed files) where # stands for a
 * digit character and where ddd is an ephemeris type (typically 405 or 406).</p>
 * @author Luc Maisonobe
 * @version $Revision:1665 $ $Date:2008-06-11 12:12:59 +0200 (mer., 11 juin 2008) $
 */
public class JPLEphemeridesLoader implements DataLoader {

    /** Error message for no JPL files. */
    private static final String NO_JPL_FILES_FOUND =
        "no JPL ephemerides binary files found";

    /** Error message for header read error. */
    private static final String HEADER_READ_ERROR =
        "unable to read header record from JPL ephemerides binary file {0}";

    /** Error message for unsupported file. */
    private static final String NOT_JPL_EPHEMERIS =
        "file {0} is not a JPL ephemerides binary file";

    /** Error message for unsupported file. */
    private static final String OUT_OF_RANGE_DATE =
        "out of range date for ephemerides: {0}";

    /** Binary record size in bytes for DE 405. */
    private static final int DE405_RECORD_SIZE = 1018 * 8;

    /** Binary record size in bytes for DE 406. */
    private static final int DE406_RECORD_SIZE =  728 * 8;

    /** Supported files name pattern. */
    private static final String SUPPORTED_FILES = "^unx[mp](\\d\\d\\d\\d)\\.(?:(?:405)|(?:406))$";

    /** List of supported ephemerides types. */
    public enum EphemerisType {

        /** Constant for Mercury. */
        MERCURY,

        /** Constant for Venus. */
        VENUS,

        /** Constant for the Earth-Moon barycenter. */
        EARTH_MOON,

        /** Constant for Mars. */
        MARS,

        /** Constant for Jupiter. */
        JUPITER,

        /** Constant for Saturn. */
        SATURN,

        /** Constant for Uranus. */
        URANUS,

        /** Constant for Neptune. */
        NEPTUNE,

        /** Constant for Pluto. */
        PLUTO,

        /** Constant for the Moon. */
        MOON,

        /** Constant for the Sun. */
        SUN;

    }

    /** Constants defined in the file. */
    private static final Map<String, Double> CONSTANTS = new HashMap<String, Double>();

    /** Ephemeris type to load. */
    private final EphemerisType type;

    /** Desired central date. */
    private final AbsoluteDate centralDate;

    /** Ephemeris for selected body. */
    private SortedSet<TimeStamped> ephemerides;

    /** Current file start epoch. */
    private AbsoluteDate startEpoch;

    /** Current file final epoch. */
    private AbsoluteDate finalEpoch;

    /** Chunks duration (in seconds). */
    private double maxChunksDuration;

    /** Current file chunks duration (in seconds). */
    private double chunksDuration;

    /** Index of the first data for selected body. */
    private int firstIndex;

    /** Number of coefficients for selected body. */
    private int coeffs;

    /** Number of chunks for the selected body. */
    private int chunks;

    /** Create a loader for JPL ephemerides binary files.
     * @param type ephemeris type to load
     * @param centralDate desired central date
     * (all data within a +/-50 days range around this date will be loaded)
     * @exception OrekitException if the header constants cannot be read
     */
    public JPLEphemeridesLoader(final EphemerisType type, final AbsoluteDate centralDate)
        throws OrekitException {

        if (CONSTANTS.isEmpty()) {
            loadConstants();
        }

        this.type          = type;
        this.centralDate   = centralDate;
        maxChunksDuration  = Double.NaN;
        chunksDuration     = Double.NaN;
    }

    /** Load ephemerides.
     * <p>The data is concatenated from all JPL ephemerides files
     * which can be found in the configured data directories tree.</p>
     * @return a set of ephemerides (all contained elements are really
     * {@link PosVelChebyshev} instances)
     * @exception OrekitException if some data can't be read or some
     * file content is corrupted
     */
    public synchronized SortedSet<TimeStamped> loadEphemerides() throws OrekitException {
        ephemerides = new TreeSet<TimeStamped>(new ChronologicalComparator());
        if (!DataProvidersManager.getInstance().feed(SUPPORTED_FILES, this)) {
            throw new OrekitException(NO_JPL_FILES_FOUND);
        }
        return ephemerides;
    }

    /** Get astronomical unit.
     * @return astronomical unit in meters
     * @exception OrekitException if constants cannot be loaded
     */
    public static double getAstronomicalUnit() throws OrekitException {

        if (CONSTANTS.isEmpty()) {
            loadConstants();
        }

        return 1000.0 * getConstant("AU");

    }

    /** Get Earth/Moon mass ratio.
     * @return Earth/Moon mass ratio
     * @exception OrekitException if constants cannot be loaded
     */
    public static double getEarthMoonMassRatio() throws OrekitException {

        if (CONSTANTS.isEmpty()) {
            loadConstants();
        }

        return getConstant("EMRAT");

    }

    /** Get the gravitational coefficient of a body.
     * @param body body for which the gravitational coefficient is requested
     * @return gravitational coefficient in m<sup>3</sup>/s<sup>2</sup>
     * @exception OrekitException if constants cannot be loaded
     */
    public static double getGravitationalCoefficient(final EphemerisType body)
        throws OrekitException {

        if (CONSTANTS.isEmpty()) {
            loadConstants();
        }

        // coefficient in au<sup>3</sup>/day<sup>2</sup>
        final double rawGM;
        switch (body) {
        case MERCURY :
            rawGM = getConstant("GM1");
            break;
        case VENUS :
            rawGM = getConstant("GM2");
            break;
        case EARTH_MOON :
            rawGM = getConstant("GMB");
            break;
        case MARS :
            rawGM = getConstant("GM4");
            break;
        case JUPITER :
            rawGM = getConstant("GM5");
            break;
        case SATURN :
            rawGM = getConstant("GM6");
            break;
        case URANUS :
            rawGM = getConstant("GM7");
            break;
        case NEPTUNE :
            rawGM = getConstant("GM8");
            break;
        case PLUTO :
            rawGM = getConstant("GM9");
            break;
        case MOON :
            return getGravitationalCoefficient(EphemerisType.EARTH_MOON) /
                   (1.0 + getEarthMoonMassRatio());
        case SUN :
            rawGM = getConstant("GMS");
            break;
        default :
            throw OrekitException.createInternalError(null);
        }

        final double au    = getAstronomicalUnit();
        return rawGM * au * au * au / (86400.0 * 86400.0);

    }

    /** Get a constant defined in the ephemerides headers.
     * <p>Note that since constants are defined in the JPL headers
     * files, they are available as soon as one file is available, even
     * if it doesn't match the desired central date. This is because the
     * header must be parsed before the dates can be checked.</p>
     * @param name name of the constant
     * @return value of the constant of NaN if the constant is not defined
     * @exception OrekitException if constants cannot be loaded
     */
    public static double getConstant(final String name) throws OrekitException {

        if (CONSTANTS.isEmpty()) {
            loadConstants();
        }

        final Double value = CONSTANTS.get(name);
        return (value == null) ? Double.NaN : value.doubleValue();

    }

    /** Load the header constants.
     * @exception OrekitException if constants cannot be loaded
     */
    private static void loadConstants() throws OrekitException {
        if (!DataProvidersManager.getInstance().feed(SUPPORTED_FILES, new HeaderConstantsLoader())) {
            throw new OrekitException(NO_JPL_FILES_FOUND);
        }
    }

    /** Get the maximal chunks duration.
     * @return chunks maximal duration in seconds
     */
    public double getMaxChunksDuration() {
        return maxChunksDuration;
    }

    /** {@inheritDoc} */
    public boolean stillAcceptsData() {
        return true;
    }

    /** {@inheritDoc} */
    public void loadData(final InputStream input, final String name)
        throws OrekitException, IOException {

        // read first header record
        final byte[] record = readFirstRecord(input, name);

        // parse first header record
        parseFirstHeaderRecord(record, name);

        if (tooFarRange(startEpoch, finalEpoch)) {
            // this file does not cover a range we are interested in,
            // there is no need to parse it further
            return;
        }

        // the second record contains the values of the constants used for least-square filtering
        // we ignore them here (they have been read once for all while setting up the constants map)
        if (!readInRecord(input, record, 0)) {
            throw new OrekitException(HEADER_READ_ERROR, name);
        }

        // read ephemerides data
        while (readInRecord(input, record, 0)) {
            parseDataRecord(record);
        }

    }

    /** Check if a range is too far from the central date.
     * <p>"Too far" is considered to be either end more than one year
     * before central date or to start more than one year after central
     * date.</p>
     * @param start start date of the range
     * @param end end date of the range
     * @return true if the range is closer than one year to the central date
     */
    private boolean tooFarRange(final AbsoluteDate start, final AbsoluteDate end) {

        // 50 days in seconds
        final double fiftyDays = 50 * 86400;

        // check range bounds
        return (centralDate.durationFrom(end) > fiftyDays) ||
               (start.durationFrom(centralDate) > fiftyDays);

    }

    /** Parse the first header record.
     * @param record first header record
     * @param name name of the file (or zip entry)
     * @exception OrekitException if the header is not a JPL ephemerides binary file header
     */
    private void parseFirstHeaderRecord(final byte[] record, final String name)
        throws OrekitException {

        // extract covered date range
        startEpoch = extractDate(record, 2652);
        finalEpoch = extractDate(record, 2660);
        boolean ok = finalEpoch.compareTo(startEpoch) > 0;

        // check astronomical unit consistency
        final double au = 1000 * extractDouble(record, 2680);
        ok = ok && (au > 1.4e11) && (au < 1.6e11);
        if (Math.abs(getAstronomicalUnit() - au) >= 0.001) {
            throw new OrekitException("inconsistent values of astronomical unit in JPL ephemerides files: ({0} and {1})",
                                      getAstronomicalUnit(), au);
        }

        final double emRat = extractDouble(record, 2688);
        ok = ok && (emRat > 80) && (emRat < 82);
        if (Math.abs(getEarthMoonMassRatio() - emRat) >= 1.0e-8) {
            throw new OrekitException("inconsistent values of Earth/Moon mass ratio in JPL ephemerides files: ({0} and {1})",
                                      getEarthMoonMassRatio(), emRat);
        }

        // indices of the Chebyshev coefficients for each ephemeris
        for (int i = 0; i < 12; ++i) {
            final int row1 = extractInt(record, 2696 + 12 * i);
            final int row2 = extractInt(record, 2700 + 12 * i);
            final int row3 = extractInt(record, 2704 + 12 * i);
            ok = ok && (row1 > 0) && (row2 >= 0) && (row3 >= 0);
            if (((i ==  0) && (type == EphemerisType.MERCURY))    ||
                ((i ==  1) && (type == EphemerisType.VENUS))      ||
                ((i ==  2) && (type == EphemerisType.EARTH_MOON)) ||
                ((i ==  3) && (type == EphemerisType.MARS))       ||
                ((i ==  4) && (type == EphemerisType.JUPITER))    ||
                ((i ==  5) && (type == EphemerisType.SATURN))     ||
                ((i ==  6) && (type == EphemerisType.URANUS))     ||
                ((i ==  7) && (type == EphemerisType.NEPTUNE))    ||
                ((i ==  8) && (type == EphemerisType.PLUTO))      ||
                ((i ==  9) && (type == EphemerisType.MOON))       ||
                ((i == 10) && (type == EphemerisType.SUN))) {
                firstIndex = row1;
                coeffs     = row2;
                chunks     = row3;
            }
        }

        // compute chunks duration
        final double timeSpan = extractDouble(record, 2668);
        ok = ok && (timeSpan > 0) && (timeSpan < 100);
        chunksDuration = 86400.0 * (timeSpan / chunks);
        if (Double.isNaN(maxChunksDuration)) {
            maxChunksDuration = chunksDuration;
        } else {
            maxChunksDuration = Math.max(maxChunksDuration, chunksDuration);
        }

        // sanity checks
        if (!ok) {
            throw new OrekitException(NOT_JPL_EPHEMERIS, name);
        }

    }

    /** Parse regular ephemeris record.
     * @param record record to parse
     * @exception OrekitException if the header is not a JPL ephemerides binary file header
     */
    private void parseDataRecord(final byte[] record) throws OrekitException {

        // extract time range covered by the record
        final AbsoluteDate rangeStart = extractDate(record, 0);
        if (rangeStart.compareTo(startEpoch) < 0) {
            throw new OrekitException(OUT_OF_RANGE_DATE, rangeStart);
        }

        final AbsoluteDate rangeEnd   = extractDate(record, 8);
        if (rangeEnd.compareTo(finalEpoch) > 0) {
            throw new OrekitException(OUT_OF_RANGE_DATE, rangeEnd);
        }

        if (tooFarRange(rangeStart, rangeEnd)) {
            // we are not interested in this record, don't parse it
            return;
        }

        // loop over chunks inside the time range
        AbsoluteDate chunkEnd = rangeStart;
        final int nbChunks    = chunks;
        final int nbCoeffs    = coeffs;
        final int first       = firstIndex;
        final double duration = chunksDuration;
        synchronized (this) {
            for (int i = 0; i < nbChunks; ++i) {

                // set up chunk validity range
                final AbsoluteDate chunkStart = chunkEnd;
                chunkEnd = (i == nbChunks - 1) ?
                        rangeEnd : new AbsoluteDate(rangeStart, (i + 1) * duration);

                // extract Chebyshev coefficients for the selected body
                // and convert them from kilometers to meters
                final double[] xCoeffs = new double[nbCoeffs];
                final double[] yCoeffs = new double[nbCoeffs];
                final double[] zCoeffs = new double[nbCoeffs];
                for (int k = 0; k < nbCoeffs; ++k) {
                    final int index = first + 3 * i * nbCoeffs + k - 1;
                    xCoeffs[k] = 1000.0 * extractDouble(record, 8 * index);
                    yCoeffs[k] = 1000.0 * extractDouble(record, 8 * (index +  nbCoeffs));
                    zCoeffs[k] = 1000.0 * extractDouble(record, 8 * (index + 2 * nbCoeffs));
                }

                // build the position-velocity model for current chunk
                ephemerides.add(new PosVelChebyshev(chunkStart, duration,
                                                    xCoeffs, yCoeffs, zCoeffs));

            }
        }

    }

    /** Read first header record.
     * @param input input stream
     * @param name name of the file (or zip entry)
     * @return record record where to put bytes
     * @exception OrekitException if the stream does not contain a JPL ephemeris
     * @exception IOException if a read error occurs
     */
    private static byte[] readFirstRecord(final InputStream input, final String name)
        throws OrekitException, IOException {

        // read first part of record, up to the ephemeris type
        final byte[] firstPart = new byte[2844];
        if (!readInRecord(input, firstPart, 0)) {
            throw new OrekitException(HEADER_READ_ERROR, name);
        }

        // get the ephemeris type, deduce the record size
        int recordSize = 0;
        switch (extractInt(firstPart, 2840)) {
        case 405 :
            recordSize = DE405_RECORD_SIZE;
            break;
        case 406 :
            recordSize = DE406_RECORD_SIZE;
            break;
        default :
            throw new OrekitException(NOT_JPL_EPHEMERIS, name);
        }

        // build a record with the proper size and finish read of the first complete record
        final int start = firstPart.length;
        final byte[] record = new byte[recordSize];
        System.arraycopy(firstPart, 0, record, 0, firstPart.length);
        if (!readInRecord(input, record, start)) {
            throw new OrekitException(HEADER_READ_ERROR, name);
        }

        return record;

    }

    /** Read bytes into the current record array.
     * @param input input stream
     * @param record record where to put bytes
     * @param start start index where to put bytes
     * @return true if record has been filled up
     * @exception IOException if a read error occurs
     */
    private static boolean readInRecord(final InputStream input,
                                        final byte[] record, final int start)
        throws IOException {
        int index = start;
        while (index != record.length) {
            final int n = input.read(record, index, record.length - index);
            if (n < 0) {
                return false;
            }
            index += n;
        }
        return true;
    }

    /** Extract a date from a record.
     * @param record record to parse
     * @param offset offset of the double within the record
     * @return extracted date
     */
    private static AbsoluteDate extractDate(final byte[] record, final int offset) {
        final double dt = extractDouble(record, offset) * 86400;
        return new AbsoluteDate(AbsoluteDate.JULIAN_EPOCH, dt, TimeScalesFactory.getTT());
    }

    /** Extract a double from a record.
     * <p>Double numbers are stored according to IEEE 754 standard, with
     * most significant byte first.</p>
     * @param record record to parse
     * @param offset offset of the double within the record
     * @return extracted double
     */
    private static double extractDouble(final byte[] record, final int offset) {
        final long l8 = ((long) record[offset + 0]) & 0xffl;
        final long l7 = ((long) record[offset + 1]) & 0xffl;
        final long l6 = ((long) record[offset + 2]) & 0xffl;
        final long l5 = ((long) record[offset + 3]) & 0xffl;
        final long l4 = ((long) record[offset + 4]) & 0xffl;
        final long l3 = ((long) record[offset + 5]) & 0xffl;
        final long l2 = ((long) record[offset + 6]) & 0xffl;
        final long l1 = ((long) record[offset + 7]) & 0xffl;
        final long l = (l8 << 56) | (l7 << 48) | (l6 << 40) | (l5 << 32) |
                       (l4 << 24) | (l3 << 16) | (l2 <<  8) | l1;
        return Double.longBitsToDouble(l);
    }

    /** Extract an int from a record.
     * @param record record to parse
     * @param offset offset of the double within the record
     * @return extracted int
     */
    private static int extractInt(final byte[] record, final int offset) {
        final int l4 = ((int) record[offset + 0]) & 0xff;
        final int l3 = ((int) record[offset + 1]) & 0xff;
        final int l2 = ((int) record[offset + 2]) & 0xff;
        final int l1 = ((int) record[offset + 3]) & 0xff;
        return (l4 << 24) | (l3 << 16) | (l2 <<  8) | l1;
    }

    /** Extract a String from a record.
     * @param record record to parse
     * @param offset offset of the string within the record
     * @param length maximal length of the string
     * @return extracted string, with whitespace characters stripped
     */
    private static String extractString(final byte[] record, final int offset, final int length) {
        try {
            return new String(record, offset, length, "US-ASCII").trim();
        } catch (UnsupportedEncodingException uee) {
            throw OrekitException.createInternalError(uee);
        }
    }

    /** Specialized loader for extracting constants from the headers. */
    private static class HeaderConstantsLoader implements DataLoader {

        /** {@inheritDoc} */
        public boolean stillAcceptsData() {
            // we try to load files only until the constants map has been set up
            return CONSTANTS.isEmpty();
        }

        /** {@inheritDoc} */
        public void loadData(final InputStream input, final String name)
            throws IOException, ParseException, OrekitException {

            // read header records
            final byte[] first  = readFirstRecord(input, name);
            final byte[] second = new byte[first.length];
            if (!readInRecord(input, second, 0)) {
                throw new OrekitException(HEADER_READ_ERROR, name);
            }

            // constants defined in the file
            for (int i = 0; i < 400; ++i) {
                final String constantName = extractString(first, 252 + i * 6, 6);
                if (constantName.length() == 0) {
                    // no more constants to read
                    return;
                }
                final double constantValue = extractDouble(second, 8 * i);
                CONSTANTS.put(constantName, constantValue);
            }

        }
    };

}
