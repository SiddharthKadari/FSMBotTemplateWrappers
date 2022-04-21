package frc.robot.systems.SystemWrappers;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.TeleopInput;

public abstract class FiniteState<StateMachine extends FiniteStateMachine> {
    /* ======================== Private variables ======================== */
    private StateMachine FSM;
    private final Timer stateTimer;
    private int tickCounter;

    /* ======================== Constructor ======================== */
    /**
     * Creates an instance of this Finite State.
     * @param fsm the finite state machine corresponding to this state
     */
    public FiniteState() {
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
        handleState(input);

        tickCounter++;
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
    public abstract Class<? extends FiniteState<StateMachine>> nextState(TeleopInput input);

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

    /**
     * Returns the Finite State Machine instance that this State is acting within.
     * @return the Finite State Machine instance that this State is acting within.
     */
    public final StateMachine getFSM(){
        return FSM;
    }

    /**
     * Sets the Finite State Machine instance that this State is acting within.
     */
    @SuppressWarnings("unchecked")
    final void setFSM(FiniteStateMachine fsm) throws IllegalArgumentException{
        try{
            FSM = (StateMachine) fsm;
        }catch(ClassCastException e){
            throw new IllegalArgumentException("This state can only be assigned to the Finite State Machine specified in its generic type parameter");
        }
    }
}
