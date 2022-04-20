package frc.robot.systems.ExampleFSM;

import frc.robot.TeleopInput;
import frc.robot.systems.SystemWrappers.FiniteState;

public class ForwardState extends FiniteState<ExampleFSMSystem> {
    public ForwardState(ExampleFSMSystem fsm) {
        super(fsm);
    }

    @Override
    public void handleState(TeleopInput input) {
        getFSM().exampleMotor.set(1);
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
