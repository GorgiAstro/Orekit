package org.orekit.propagation.integration;

import org.hipparchus.ode.ODEIntegrator;
import org.orekit.attitudes.AttitudeProvider;
import org.orekit.frames.Frame;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.PropagationType;
import org.orekit.propagation.integration.AbstractIntegratedPropagator;
import org.orekit.propagation.integration.StateMapper;
import org.orekit.time.AbsoluteDate;

public class PythonAbstractIntegratedPropagator extends AbstractIntegratedPropagator {
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

    /** {@inheritDoc} */
    public PythonAbstractIntegratedPropagator(ODEIntegrator integrator, PropagationType propagationType) {
        super(integrator, propagationType);
    }

    /** {@inheritDoc} */
    @Override
    public native StateMapper createMapper(AbsoluteDate referenceDate, double mu, OrbitType orbitType, PositionAngle positionAngleType, AttitudeProvider attitudeProvider, Frame frame);

    /** {@inheritDoc} */
    @Override
    public native MainStateEquations getMainStateEquations(ODEIntegrator integ);
}
