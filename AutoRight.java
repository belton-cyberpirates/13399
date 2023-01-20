package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import java.util.Set;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.Tfod;

import org.firstinspires.ftc.teamcode.DriveMotors;
import org.firstinspires.ftc.teamcode.Arm;
import org.firstinspires.ftc.teamcode.Direction;
import org.firstinspires.ftc.teamcode.ParkingSpot;
import org.firstinspires.ftc.teamcode.Config;

@Autonomous(name = "AutoRight")
public class AutoRight extends LinearOpMode {
  private VuforiaCurrentGame vuforiaPOWERPLAY;
  private Tfod tfod;
  private DriveMotors driveMotors;
  private Arm arm;
  private DcMotor claw;
  public static BNO055IMU imu;
  public static BNO055IMU.Parameters imuParameters;
  Recognition recognition;
  


  /**
   * Set reliable initial configuration for robot motors
   */
  private void MotorSetup() {
    arm.DropArm();
    sleep(200);
    arm.Initialize();
    claw.setPower(0.5);
   }
   
  /**
   * Describe this function...
   */
  private void TFInitialize() {
    vuforiaPOWERPLAY.initialize(
        "", // vuforiaLicenseKey
        hardwareMap.get(WebcamName.class, "Webcam 1"), // cameraName
        "", // webcamCalibrationFilename
        false, // useExtendedTracking
        true, // enableCameraMonitoring
        VuforiaLocalizer.Parameters.CameraMonitorFeedback.NONE, // cameraMonitorFeedback
        0, // dx
        0, // dy
        0, // dz
        AxesOrder.XZY, // axesOrder
        90, // firstAngle
        90, // secondAngle
        0, // thirdAngle
        true); // useCompetitionFieldTargetLocations
    // Set isModelTensorFlow2 to true if you used a TensorFlow 2 tool,
    // such as ftc-ml, to create the model. Set isModelQuantized to
    // true if the model is quantized. Models created with ftc-ml are
    // quantized. Set inputSize to the image size corresponding to
    // the model. If your model is based on SSD MobileNet v2 320x320,
    // the image size is 300 (srsly!). If your model is based on
    // SSD MobileNet V2 FPNLite 320x320, the image size is 320.
    // If your model is based on SSD MobileNet V1 FPN 640x640 or
    // SSD MobileNet V2 FPNLite 640x640, the image size is 640.
    tfod.useModelFromFile("FishGGearsOctopus.tflite", JavaUtil.createListWith("FISH", "GEARS", "GEARS", "OCTOPUS"), true, true, 320);
    tfod.initialize(vuforiaPOWERPLAY, (float) 0.7, true, true);
    tfod.activate();
    tfod.setZoom(1, 16 / 9);
    telemetry.addData("tfready", "camera dots");
    telemetry.update();
  }
  

  /**
   * Describe this function...
   */
  private ParkingSpot doTF() {
    for (int cycles = 0; cycles < 50; cycles += 1) {

      // retrieve recognitions from TensorFlow
      List<Recognition> recognitions = tfod.getRecognitions();
      
      // Check if we've detected anything
      if (JavaUtil.listLength(recognitions) > 0) {
        // Log *all* detections
        telemetry.addData("Detected", recognitions);
        telemetry.update();
        
        // use *last* recognition to determine parking spot
        switch(recognitions.get(recognitions.size() - 1).getLabel()) {
          case "Octopus":
            return ParkingSpot.OCTOPUS;
      
          case "Gear":
            return ParkingSpot.GEARS;
      
          case "Fish":
            return ParkingSpot.FISH;
            
          default:
          // We detected something, but it has a label we don't recognize
            telemetry.addData("Detected", "[ERROR-BAD_LABEL], returning spot 1");
            telemetry.update();
            return ParkingSpot.FISH;
        }
      }
    } // end for loop
  
    // if we didn't find anything after however many tries, assume FISH,
    //  since we have trouble detecting it.
    telemetry.addData("Detected", "none detected -- assuming parking spot 1");
    telemetry.update();
    return ParkingSpot.FISH;
  }
  

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {



  imu = hardwareMap.get(BNO055IMU.class, "imu");
  // Create new IMU Parameters object.
    imuParameters = new BNO055IMU.Parameters();
    // Use degrees as angle unit.
    imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
    // Express acceleration as m/s^2.
    imuParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    // Disable logging.
    imuParameters.loggingEnabled = false;
    // Initialize IMU.
    imu.initialize(imuParameters);
    
    vuforiaPOWERPLAY = new VuforiaCurrentGame();
    tfod = new Tfod();

    // argument order *must* be fr-fl-bl-br
    driveMotors = new DriveMotors(
      hardwareMap.get(DcMotor.class, "frontRight"),
      hardwareMap.get(DcMotor.class, "frontLeft"),
      hardwareMap.get(DcMotor.class, "rearLeft"),
      hardwareMap.get(DcMotor.class, "rearRight")
    );
    
    arm = new Arm(
      hardwareMap.get(DcMotor.class, "arm0"),
      hardwareMap.get(DcMotor.class, "arm1")
    );
    claw = hardwareMap.get(DcMotor.class, "claw");

    TFInitialize();
    waitForStart();
    MotorSetup();
    if (opModeIsActive()) {
      ParkingSpot parkingSpot = doTF();
      
      // <------------------------------------------- Auto code here
      /// First Cone
      CloseClaw();
      driveMotors.Move(Direction.FORWARD, 1710);
      driveMotors.Turn(-45);
      arm.Move(125,true);
      driveMotors.Move(Direction.FORWARD, 155);
      arm.Move(100);
      OpenClaw();
      driveMotors.Move(Direction.BACKWARD, 170);
      arm.Move(10,true);
      driveMotors.Turn(15);
      driveMotors.PivotLeft();
      driveMotors.PivotRight();
///Small cone stack
      arm.Move(30);
      driveMotors.Move(Direction.FORWARD, 250);
      CloseClaw();
      arm.Move(75);
      driveMotors.PivotLeft2();
      arm.Move(60);
      OpenClaw();
      arm.Move(75);
      /*///Park
      driveMotors.Move(Direction.LEFT, 30);
      arm.Move(25);
      arm.Move(100);

      
      Park(parkingSpot);//*/
    }
    
    vuforiaPOWERPLAY.close();
    tfod.close();
  }
  
  
  private void Park(ParkingSpot target) {
    
    switch(target) {
    case FISH:
      telemetry.addData("Parking", "PARKING FISH");
      driveMotors.Move(Direction.LEFT, (int)(Config.TILE_LENGTH * 1.6));
      break;
      
    case GEARS:
      telemetry.addData("Parking", "PARKING GEARS");
      driveMotors.Move(Direction.LEFT, (int)(Config.TILE_LENGTH * .3));
      break;
      
    case OCTOPUS:
      telemetry.addData("Parking", "PARKING OCTOPUS");
      driveMotors.Move(Direction.RIGHT, (int)(Config.TILE_LENGTH * .3));
    }
    telemetry.update();
  }
  
  private void OpenClaw() {
    sleep(200);
    claw.setPower(-0.3);
    sleep(200);
    claw.setPower(0);
  }
  
  
  private void CloseClaw() {
    claw.setPower(0.9);
    sleep(200);
    claw.setPower(0.3);
  }
}
