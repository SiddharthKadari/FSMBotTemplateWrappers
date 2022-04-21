package frc.robot.systems.ExampleMotorFSM;

import frc.robot.TeleopInput;
import frc.robot.systems.SystemWrappers.FiniteState;

public class IdleState extends FiniteState<ExampleMotorFSM> {
    public IdleState(ExampleMotorFSM fsm) {
        super(fsm);
    }

    @Override
    public void handleState(TeleopInput input) {
        getFSM().exampleMotor.set(0);
    }

    @Override
    public Class<? extends FiniteState<ExampleMotorFSM>> nextState(TeleopInput input) {
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
