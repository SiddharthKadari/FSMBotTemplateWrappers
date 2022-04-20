package frc.robot.systems.ExampleFSM;

import frc.robot.TeleopInput;
import frc.robot.systems.SystemWrappers.FiniteState;

public class ReverseState extends FiniteState<ExampleFSMSystem> {
    public ReverseState(ExampleFSMSystem fsm) {
        super(fsm);
    }

    @Override
    public void handle(TeleopInput input) {
        if (input == null) {
			return;
		}
        FSM.exampleMotor.set(-1);
    }

    @Override
    public Class<? extends FiniteState<?>> nextState(TeleopInput input) {
        if (input == null) {
			return IdleState.class;
		}
        if(input.isForwardButtonPressed()){
            return ForwardState.class;
        }else if(input.isReverseButtonPressed()){
            return ReverseState.class;
        }else{
            return IdleState.class;
        }
    }
    
}
