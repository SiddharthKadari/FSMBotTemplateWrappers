package frc.robot.systems.ExampleMotorFSM;

// WPILib Imports

// Third party Hardware Imports
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// Robot Imports
import frc.robot.systems.SystemWrappers.FiniteStateMachine;
import frc.robot.HardwareMap;

public class ExampleMotorFSM extends FiniteStateMachine{
	final CANSparkMax exampleMotor;

	public ExampleMotorFSM(){
		super(IdleState.class, IdleState.class);

		exampleMotor = new CANSparkMax(HardwareMap.CAN_ID_SPARK_EXAMPLE,
			MotorType.kBrushless);
	}

	@Override
	public void reset(){
		//Example use of the reset method
		if(exampleMotor.getEncoder() != null){
			exampleMotor.getEncoder().setPosition(0);
		}
	}
}
