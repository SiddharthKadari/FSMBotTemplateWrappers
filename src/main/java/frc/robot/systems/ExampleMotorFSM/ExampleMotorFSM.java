package frc.robot.systems.ExampleMotorFSM;

// WPILib Imports

// Third party Hardware Imports
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// Robot Imports
import frc.robot.systems.SystemWrappers.FiniteStateMachine;
import frc.robot.HardwareMap;

public class ExampleMotorFSM extends FiniteStateMachine {
	final static double MOTOR_POWER = 0.1;
	final CANSparkMax exampleMotor;

	public ExampleMotorFSM(){
		super(IdleState.class, IdleState.class);

		exampleMotor = new CANSparkMax(HardwareMap.CAN_ID_SPARK_EXAMPLE,
			MotorType.kBrushless);
	}

	@Override
	protected void reset() {
		if (exampleMotor.getEncoder() != null) {
			exampleMotor.getEncoder().setPosition(0);
		}
	}
}
