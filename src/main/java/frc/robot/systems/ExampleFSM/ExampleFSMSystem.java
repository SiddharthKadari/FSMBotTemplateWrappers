package frc.robot.systems.ExampleFSM;

// WPILib Imports

// Third party Hardware Imports
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// Robot Imports
import frc.robot.systems.SystemWrappers.FiniteStateMachine;
import frc.robot.HardwareMap;
import frc.robot.TeleopInput;

public class ExampleFSMSystem extends FiniteStateMachine{
	CANSparkMax exampleMotor;

	public ExampleFSMSystem(){
		super(IdleState.class, IdleState.class);

		exampleMotor = new CANSparkMax(HardwareMap.CAN_ID_SPARK_EXAMPLE,
			MotorType.kBrushless);
	}

	@Override
	public void updateTeleop(TeleopInput input){
		System.out.println(getCurrentState());
	}
}
