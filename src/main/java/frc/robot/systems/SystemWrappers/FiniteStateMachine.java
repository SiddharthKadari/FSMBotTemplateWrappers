package frc.robot.systems.SystemWrappers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import frc.robot.TeleopInput;

public abstract class FiniteStateMachine {
	/* ======================== Static variables ======================== */
	private static final ArrayList<FiniteStateMachine> FINITE_STATE_MACHINES = new ArrayList<>();

    /* ======================== Private variables ======================== */
	private FiniteState<?> currentState;
	private final Class<? extends FiniteState<?>> startStateAuto;
	private final Class<? extends FiniteState<?>> startStateTeleop;

	/* ======================== Constructor ======================== */
	/**
	 * Create FSMSystem and initialize to starting state. Also perform any
	 * one-time initialization or configuration of hardware required. Note
	 * the constructor is called only once when the robot boots.
	 * @param startState the desired init state of the FSM.
	 */
	public FiniteStateMachine(Class<? extends FiniteState<?>> autoStart,
							  Class<? extends FiniteState<?>> teleopStart) {
		FINITE_STATE_MACHINES.add(this);

		startStateAuto = autoStart;
		startStateTeleop = teleopStart;
		// Reset state machine
		reset();
	}

	/**
	 * Reset all of the system's components. Zero sensors, initialize variables,
	 * etc.
	 */
	public void reset() {}
	
	/**
	 * Reset this system to its start state during Autonomous. This may be
	 * called from mode init when the robot is enabled.
	 *
	 * Note this is distinct from the one-time initialization in the constructor
	 * as it may be called multiple times in a boot cycle,
	 * Ex. if the robot is enabled, disabled, then reenabled.
	 */
	private final void resetAutonomous() {
		reset();

		setState(startStateAuto);

		currentState.handle(null);
	}
	
	/**
	 * Reset this system to its start state during Autonomous. This may be
	 * called from mode init when the robot is enabled.
	 *
	 * Note this is distinct from the one-time initialization in the constructor
	 * as it may be called multiple times in a boot cycle,
	 * Ex. if the robot is enabled, disabled, then reenabled.
	 */
	private final void resetTeleop(TeleopInput input) {
		reset();

		setState(startStateTeleop);

		currentState.handle(input);
	}

    /**
	 * Update FSM based on new inputs. This function only calls the FSM state
	 * specific handlers.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
	private final void update(TeleopInput input) {
		currentState.handle(input);
		if(input == null){
			updateAutonomous();
		} else{
			updateTeleop(input);
		}
		setState(currentState.nextState(input));
	}

	/**
	 * Called after the Current State is handled, and before the state is updated.
	 * @param input Global TeleopInput, since robot in teleop mode
	 */
	public void updateTeleop(TeleopInput input){}

	/**
	 * Called after the Current State is handled, and before the state is updated.
\	 */
	public void updateAutonomous(){}

	/**
	 * Sets the state of this StateMachine to the new State. Resets all timers and
	 * tick counters if state changes.
	 * @param newState The desired state of the state machine
	 */
	@SuppressWarnings("unchecked")
	private final void setState(Class<? extends FiniteState<?>> newState) {
		try {
			currentState = ((Constructor<? extends FiniteState<?>>)newState.getDeclaredConstructors()[0]).newInstance(this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the current state of the FSM.
	 * @return the current state of the FSM
	 */
	@SuppressWarnings("unchecked")
	public final Class<? extends FiniteState<?>> getCurrentState(){
		return (Class<? extends FiniteState<?>>) currentState.getClass();
	}

	/**
	 * Returns the instance of a desired FSM, null if provided FSM was never instantiated.
	 * @param targetFSM The class of the desired FSM
	 * @return the instance of a desired FSM, null if provided FSM was never instantiated
	 */
	public static final FiniteStateMachine getStateMachine(Class<? extends FiniteStateMachine> targetFSM) {
		for(int i = 0; i < FINITE_STATE_MACHINES.size(); i++) {
			if(targetFSM.isInstance(FINITE_STATE_MACHINES.get(i))) {
				return FINITE_STATE_MACHINES.get(i);
			}
		}

		return null;
	}

	/**
     * Returns the number of times that the FSM has updated while in this Finite State
     * since changing into this Finite State.
     * @return the number of times that the FSM has updated while in this Finite State
     * since changing into this Finite State
     */
    public final int ticksElapsedInState(){
        return currentState.ticksElapsedInState();
    }

	/**
     * Returns the amount of time in seconds that the FSM has been in this Finite State
     * since changing into this Finite State.
     * @return the amount of time in seconds that the FSM has been in this Finite State
     * since changing into this Finite State
     */
	public final double timeElapsedInState(){
		return currentState.getTimer().get();
	}

	/**
	 * Resets all existing instantiated State Machines for Autonomous.
	 */
	public static final void resetAllStateMachinesAutonomous() {
		for(int i = 0; i < FINITE_STATE_MACHINES.size(); i++){
			FINITE_STATE_MACHINES.get(i).resetAutonomous();
		}
	}

	/**
	 * Resets all existing instantiated State Machines for Teleop.
	 * @param input Global TeleopInput, since robot in teleop mode
	 */
	public static final void resetAllStateMachinesTeleop(TeleopInput input) {
		for(int i = 0; i < FINITE_STATE_MACHINES.size(); i++){
			FINITE_STATE_MACHINES.get(i).resetTeleop(input);
		}
	}

	/**
	 * Updates all existing instantiated State Machines.
	 */
	public static final void updateAllStateMachines(TeleopInput input) {
		for(int i = 0; i < FINITE_STATE_MACHINES.size(); i++){
			FINITE_STATE_MACHINES.get(i).update(input);
		}
	}
}
