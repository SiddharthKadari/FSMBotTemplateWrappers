package frc.robot.systems.SystemWrappers;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.TeleopInput;

public abstract class FiniteState<T extends FiniteStateMachine> {
    /* ======================== Private variables ======================== */
    protected final T FSM;
    private final Timer stateTimer;
    private int tickCounter;

    /* ======================== Constructor ======================== */
    /**
     * Creates an instance of this Finite State.
     * @param fsm the finite state machine corresponding to this state
     */
    public FiniteState(T fsm) {
        FSM = fsm;

        stateTimer = new Timer();
        stateTimer.start();
        tickCounter = 0;
    }

    /**
	 * Handles the robot's behavior in this Finite State. Updates tick counter.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
    final void handle(TeleopInput input) {
        tickCounter++;

        handleState(input);
    }

    /**
	 * Handles the robot's behavior in this Finite State.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
    public abstract void handleState(TeleopInput input);

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

    /**
     * Returns the number of times that the FSM has updated while in this Finite State
     * since changing into this Finite State.
     * @return the number of times that the FSM has updated while in this Finite State
     * since changing into this Finite State
     */
    final int ticksElapsedInState(){
        return tickCounter;
    }

    /**
     * Returns the instance of the Timer object being used to maintain state timing.
     * @return the instance of the Timer object being used to maintain state timing
     */
    final Timer getTimer() {
        return stateTimer;
    }
}
