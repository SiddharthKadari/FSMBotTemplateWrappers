package frc.robot.systems.ExamplePistonFSM;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.TeleopInput;
import frc.robot.systems.ExampleMotorFSM.ExampleMotorFSM;
import frc.robot.systems.ExampleMotorFSM.ForwardState;
import frc.robot.systems.SystemWrappers.FiniteState;
import frc.robot.systems.SystemWrappers.FiniteStateMachine;

public class RetractedState extends FiniteState<ExamplePistonFSM> {
    @Override
    public void handleState(TeleopInput input) {
        getFSM().solenoid.set(getFSM().ticksElapsedInState() == 0 ?
            DoubleSolenoid.Value.kReverse :
            DoubleSolenoid.Value.kOff);
    }

    @Override
    public Class<? extends FiniteState<ExamplePistonFSM>> nextState(TeleopInput input) {
        return FiniteStateMachine.getStateMachine(ExampleMotorFSM.class).getCurrentState() == ForwardState.class ?
            ExtendedState.class :
            RetractedState.class;
    }
    
}
