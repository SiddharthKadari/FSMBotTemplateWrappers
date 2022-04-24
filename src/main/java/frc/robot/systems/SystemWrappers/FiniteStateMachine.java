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
	private Class<? extends FiniteState<? extends FiniteStateMachine>> startStateAuto;
	private Class<? extends FiniteState<? extends FiniteStateMachine>> startStateTeleop;

	/* ======================== Constructor ======================== */
	/**
	 * Create FSMSystem and initialize to starting state. Also perform any
	 * one-time initialization or configuration of hardware required. Note
	 * the constructor is called only once when the robot boots.
	 * @param startState the desired init state of the FSM.
	 */
	public FiniteStateMachine(Class<? extends FiniteState<? extends FiniteStateMachine>> autoStart,
							  Class<? extends FiniteState<? extends FiniteStateMachine>> teleopStart) {
		FINITE_STATE_MACHINES.add(this);

		startStateAuto = autoStart;
		startStateTeleop = teleopStart;
	}

	/**
	 * Reassigns the Autonomous starting state to the provided state. At the start of
	 * every Autonomous period, the state passed through this parameter will be the
	 * FSM's initial state.
	 * @param startStateAuto The desired starting state during the Autonomous period
	 */
	protected final void reassignAutoStartState(Class<? extends FiniteState<? extends FiniteStateMachine>> autoStart) {
		startStateAuto = autoStart;
	}

	/**
	 * Reassigns the Teleop starting state to the provided state. At the start of
	 * every Teleop period, the state passed through this parameter will be the
	 * FSM's initial state.
	 * @param startStateAuto The desired starting state during the Teleop period
	 */
	protected final void reassignTeleopStartState(Class<? extends FiniteState<? extends FiniteStateMachine>> teleopStart) {
		startStateTeleop = teleopStart;
	}

	/**
	 * Reset all of the system's components. Zero sensors, initialize variables,
	 * etc.
	 */
	protected void reset() {}
	
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

		currentState.handleState(null);
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

		currentState.handleState(input);
	}

    /**
	 * Update FSM based on new inputs. This function only calls the FSM state
	 * specific handlers.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
	private final void update(TeleopInput input) {
		if (input == null) {
			updateAutonomousData();
		} else {
			updateTeleopData(input);
		}

		currentState.handle(input);
		
		//We need to store the results of nextState, since calling nextState twice consecutively could 
		//produce differing results, due to the behavior of the getRawButtonPressed() method of joysticks
		Class<? extends FiniteState<? extends FiniteStateMachine>> nextState = currentState.nextState(input);
		if (nextState != currentState.getClass()) {
			setState(nextState);
		}
	}

	/**
	 * Called once during each Teleop tick, before the current state has been handled,
	 * and before the state has been switched. This method should be overridden to update
	 * all relavent state variables, such as sensor inputs and any other relavent data that
	 * should be updated during every Teleop tick.
	 * @param input Global TeleopInput, since robot in teleop mode
	 */
	protected void updateTeleopData(TeleopInput input) {}

	/**
	 * Called once during each Autonomous tick, before the current state has been handled,
	 * and before the state has been switched. This method should be overridden to update
	 * all relavent state variables, such as sensor inputs and any other relavent data that
	 * should be updated during every Autonomous tick.
	 */
	protected void updateAutonomousData() {}

	/**
	 * Sets the state of this StateMachine to the new State. Resets all timers and
	 * tick counters if state changes.
	 * @param newState The desired state of the state machine
	 */
	private final void setState(Class<? extends FiniteState<? extends FiniteStateMachine>> newState) throws IllegalArgumentException {
		try {
			currentState = ((Constructor<? extends FiniteState<? extends FiniteStateMachine>>)newState.getDeclaredConstructor()).newInstance();
			currentState.setFSM(this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException | NoSuchMethodException e) {
			throw new IllegalArgumentException("The desired State is not a state of this FiniteStateMachine");
		}
	}

	/**
	 * Returns the current state of the FSM.
	 * @return the current state of the FSM
	 */
	@SuppressWarnings("unchecked")
	private final Class<? extends FiniteState<? extends FiniteStateMachine>> getCurrentState() {
		return (Class<? extends FiniteState<? extends FiniteStateMachine>>) currentState.getClass();
	}

	/**
	 * Returns the instance of a desired FSM, null if provided FSM was never instantiated.
	 * @param targetFSM The class of the desired FSM
	 * @return the instance of a desired FSM, null if provided FSM was never instantiated
	 */
	private static final FiniteStateMachine getStateMachine(Class<? extends FiniteStateMachine> targetFSM) {
		for (int i = 0; i < FINITE_STATE_MACHINES.size(); i++) {
			if (targetFSM.isInstance(FINITE_STATE_MACHINES.get(i))) {
				return FINITE_STATE_MACHINES.get(i);
			}
		}

		return null;
	}

	/**
	 * Returns the current state of the provided FSM, null if FSM was never instantiated.
	 * @param targetFSM The class of the desired FSM
	 * @return The class of the current state of the provided FSM, null if FSM was never instantiated.
	 */
	public static final Class<? extends FiniteState<? extends FiniteStateMachine>> getStateOf(Class<? extends FiniteStateMachine> targetFSM) {
		FiniteStateMachine target = getStateMachine(targetFSM);
		return target == null ? null : target.getCurrentState();
	}

	/**
     * Returns the number of times that the FSM has updated while in this Finite State
     * since changing into this Finite State.
     * @return the number of times that the FSM has updated while in this Finite State
     * since changing into this Finite State
     */
    public final int ticksElapsedInState() {
        return currentState.ticksElapsedInState();
    }

	/**
     * Returns the amount of time in seconds that the FSM has been in this Finite State
     * since changing into this Finite State.
     * @return the amount of time in seconds that the FSM has been in this Finite State
     * since changing into this Finite State
     */
	public final double timeElapsedInState() {
		return currentState.getTimer().get();
	}

	/**
	 * Resets all existing instantiated State Machines for Autonomous.
	 */
	public static final void resetAllStateMachinesAutonomous() {
		for (int i = 0; i < FINITE_STATE_MACHINES.size(); i++) {
			FINITE_STATE_MACHINES.get(i).resetAutonomous();
		}
	}

	/**
	 * Resets all existing instantiated State Machines for Teleop.
	 * @param input Global TeleopInput, since robot in teleop mode
	 */
	public static final void resetAllStateMachinesTeleop(TeleopInput input) {
		for (int i = 0; i < FINITE_STATE_MACHINES.size(); i++) {
			FINITE_STATE_MACHINES.get(i).resetTeleop(input);
		}
	}

	/**
	 * Updates all existing instantiated State Machines.
	 */
	public static final void updateAllStateMachines(TeleopInput input) {
		for (int i = 0; i < FINITE_STATE_MACHINES.size(); i++) {
			FINITE_STATE_MACHINES.get(i).update(input);
		}
	}

	/**
	 * Calls the overridable reset() method for all existing instantiated State Machines.
	 */
	public static final void resetAllStateMachines() {
		for (int i = 0; i < FINITE_STATE_MACHINES.size(); i++) {
			FINITE_STATE_MACHINES.get(i).reset();
		}
	}
}
