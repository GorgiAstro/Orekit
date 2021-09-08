package org.orekit.forces;

import org.hipparchus.Field;
import org.hipparchus.CalculusFieldElement;
import org.hipparchus.geometry.euclidean.threed.FieldVector3D;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.forces.AbstractForceModel;
import org.orekit.propagation.FieldSpacecraftState;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.events.FieldEventDetector;
import org.orekit.utils.ParameterDriver;

import java.util.stream.Stream;

public class PythonAbstractForceModel extends AbstractForceModel {

    /** Part of JCC Python interface to object */
    private long pythonObject;

    /** Part of JCC Python interface to object */
    public void pythonExtension(long pythonObject)
    {
        this.pythonObject = pythonObject;
    }

    /** Part of JCC Python interface to object */
    public long pythonExtension()
    {
        return this.pythonObject;
    }

    /** Part of JCC Python interface to object */
    public void finalize()
            throws Throwable
    {
        pythonDecRef();
    }

    /** Part of JCC Python interface to object */
    public native void pythonDecRef();

    /**
     * Check if force models depends on position only.
     * Extension point for Python.
     * @return true if force model depends on position only, false
     * if it depends on velocity, either directly or due to a dependency
     * on attitude
     * @since 9.0
     */
    @Override
    public native boolean dependsOnPositionOnly();

    /**
     * Compute acceleration.
     * Extension point for Python.
     * @param s          current state information: date, kinematics, attitude
     * @param parameters values of the force model parameters
     * @return acceleration in same frame as state
     * @since 9.0
     */
    @Override
    public native Vector3D acceleration(SpacecraftState s, double[] parameters);

    /**
     * Compute acceleration. Automatically directs to the Python extension point acceleration_FT
     *
     * @param s          current state information: date, kinematics, attitude
     * @param parameters values of the force model parameters
     * @return acceleration in same frame as state
     * @since 9.0
     */
    @Override
    public <T extends CalculusFieldElement<T>> FieldVector3D<T> acceleration(FieldSpacecraftState<T> s, T[] parameters) {
        return this.acceleration_FT(s,parameters);
    }

    /**
     * Compute acceleration, Alternative python interface point for the acceleration method.
     * Extension point for Python.
     *
     * @param s          current state information: date, kinematics, attitude
     * @param parameters values of the force model parameters
     * @return acceleration in same frame as state
     * @since 9.0
     */
    public native <T extends CalculusFieldElement<T>> FieldVector3D<T> acceleration_FT(FieldSpacecraftState<T> s, T[] parameters);

    /**
     * Get the discrete events related to the model.
     * Extension point for Python.
     *
     * @return stream of events detectors
     */
    @Override
    public native Stream<EventDetector> getEventsDetectors();

    /**
     * Get the discrete events related to the model.
     * Extension point for Python.
     *
     * @param field field to which the state belongs
     * @return stream of events detectors
     */
    @Override
    public native <T extends CalculusFieldElement<T>> Stream<FieldEventDetector<T>> getFieldEventsDetectors(Field<T> field);

    /**
     * Get the drivers for force model parameters.
     * Extension point for Python.
     *
     * @return drivers for force model parameters
     * @since 8.0
     */
    @Override
    public native ParameterDriver[] getParametersDrivers();

    }