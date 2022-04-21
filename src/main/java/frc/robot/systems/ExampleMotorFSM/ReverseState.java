package frc.robot.systems.ExampleMotorFSM;

import frc.robot.TeleopInput;
import frc.robot.systems.SystemWrappers.FiniteState;

public class ReverseState extends FiniteState<ExampleMotorFSM> {
    @Override
    public void handleState(TeleopInput input) {
        getFSM().exampleMotor.set(-1);
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
