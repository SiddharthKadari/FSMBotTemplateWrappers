package frc.robot.systems.ExamplePistonFSM;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.HardwareMap;
import frc.robot.systems.SystemWrappers.FiniteStateMachine;

public class ExamplePistonFSM extends FiniteStateMachine {
    final DoubleSolenoid solenoid;

    public ExamplePistonFSM() {
        super(ExtendedState.class, RetractedState.class);

        solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,
            HardwareMap.PCM_CHANNEL_INTAKE_CYLINDER_FORWARD,
            HardwareMap.PCM_CHANNEL_INTAKE_CYLINDER_REVERSE);
    }
}
