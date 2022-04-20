package frc.robot.systems.SystemWrappers;

import frc.robot.TeleopInput;

public abstract class FiniteState<T extends FiniteStateMachine> {
    /* ======================== Private variables ======================== */
    protected final T FSM;

    /* ======================== Constructor ======================== */
    /**
     * Creates an instance of this Finite State.
     * @param fsm the finite state machine corresponding to this state
     */
    public FiniteState(T fsm) {
        FSM = fsm;
    }

    /**
	 * Handles the robot's behavior in this Finite State.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
    public abstract void handle(TeleopInput input);

    /**
	 * Decide the next state to transition to. This is a function of the inputs
	 * and the current state of this FSM. This method should not have any side
	 * effects on outputs. In other words, this method should only read or get
	 * values to decide what state to go to.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 * @return FSM state for the next iteration
	 */
    public abstract Class<? extends FiniteState<?>> nextState(TeleopInput input);
}
