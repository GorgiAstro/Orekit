package org.orekit.propagation.sampling;

import org.orekit.propagation.PropagatorsParallelizer;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;

import java.util.List;

public class EmptyMultiSatStepHandler implements MultiSatStepHandler {
    /**
     * Initialize step handler at the start of a propagation.
     * <p>
     * This method is called once at the start of the propagation. It
     * may be used by the step handler to initialize some internal data
     * if needed.
     * </p>
     * <p>
     * The default method does nothing
     * </p>
     *
     * @param states0 initial states, one for each satellite in the same order
     *                used to {@link PropagatorsParallelizer#PropagatorsParallelizer(List, MultiSatStepHandler)
     *                build} the {@link PropagatorsParallelizer multi-sat propagator}.
     * @param t       target time for the integration
     */
    @Override
    public void init(List<SpacecraftState> states0, AbsoluteDate t) {

    }

    /**
     * Handle the current step.
     * <p>
     * When called by {@link PropagatorsParallelizer PropagatorsParallelizer},
     * all interpolators have the same time range.
     * </p>
     *
     * @param interpolators interpolators set up for the current step in the same order
     *                      used to {@link PropagatorsParallelizer#PropagatorsParallelizer(List, MultiSatStepHandler)
     *                      build} the {@link PropagatorsParallelizer multi-sat propagator}
     */
    @Override
    public void handleStep(List<OrekitStepInterpolator> interpolators) {

    }

    /**
     * Finalize propagation.
     * @param finalStates states at propagation end
     * @since 11.0
     */
    public void finish(final List<SpacecraftState> finalStates) {

    }
}
