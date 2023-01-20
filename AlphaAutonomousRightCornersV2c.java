package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.Tfod;

@Autonomous(name = "AlphaAutonomousRightCornersV2c (Blocks to Java)", preselectTeleOp = "CurrentDriveV3.1")
public class AlphaAutonomousRightCornersV2c extends LinearOpMode {

  private DcMotor arm0;
  private DcMotor arm1;
  private DcMotor claw;
  private AnalogInput clawpot;
  private AnalogInput armpot;
  private VuforiaCurrentGame vuforiaPOWERPLAY;
  private Tfod tfod;
  private DcMotor frontLeft;
  private DcMotor frontRight;
  private DcMotor rearLeft;
  private DcMotor rearRight;

  int ParkingVariable;
  Recognition Recognition2;
  double StepSpeed;
  double speedScale;
  int rlE;
  double maxSpeed;
  double stepIncrement;
  int rrE;
  int frE;
  int SleepDuration;

  /**
   * Describe this function...
   */
  private void RaiseArm(int TargetPosition) {
    arm0.setTargetPosition(TargetPosition);
    arm1.setTargetPosition(-TargetPosition);
    telemetry.addData("Position", arm0.getCurrentPosition());
    telemetry.addData("Position", arm1.getCurrentPosition());
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  private void CloseClaw() {
    claw.setDirection(DcMotorSimple.Direction.FORWARD);
    claw.setPower(0.8);
    telemetry.addData("ClawPot", clawpot.getVoltage());
    sleep(300);
    claw.setPower(0.1);
  }

  /**
   * Describe this function...
   */
  private void OpenClaw() {
    claw.setDirection(DcMotorSimple.Direction.REVERSE);
    claw.setPower(0.5);
    telemetry.addData("ClawPot", clawpot.getVoltage());
    sleep(100);
    claw.setPower(0);
  }

  /**
   * Describe this function...
   */
   
  //private void LowerArm(       //commented out because it did not work and i make better
  //     // TODO: Enter the type for argument named Distance2
  //     UNKNOWN_TYPE Distance2) {
  //   // TODO: Enter the type for variable named PotValue
  //   UNKNOWN_TYPE PotValue;

  //   while (armpot.getVoltage() > PotValue) {
  //     arm0.setPower(-0.5);
  //     arm1.setPower(0.5);
  //   }
  //   arm0.setPower(0);
  //   arm1.setPower(0);
  //   telemetry.addData("Pot", armpot.getVoltage());
  //   telemetry.update();
  //   sleep(100);
  // }

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    arm0 = hardwareMap.get(DcMotor.class, "arm0");
    arm1 = hardwareMap.get(DcMotor.class, "arm1");
    claw = hardwareMap.get(DcMotor.class, "claw");
    clawpot = hardwareMap.get(AnalogInput.class, "clawpot");
    armpot = hardwareMap.get(AnalogInput.class, "armpot");
    vuforiaPOWERPLAY = new VuforiaCurrentGame();
    tfod = new Tfod();
    frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
    frontRight = hardwareMap.get(DcMotor.class, "frontRight");
    rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
    rearRight = hardwareMap.get(DcMotor.class, "rearRight");

    vuforiaPOWERPLAY.initialize(
        "", // vuforiaLicenseKey
        hardwareMap.get(WebcamName.class, "Webcam 1"), // cameraName
        "", // webcamCalibrationFilename
        false, // useExtendedTracking
        true, // enableCameraMonitoring
        VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES, // cameraMonitorFeedback
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
    tfod.useModelFromFile("FishGGearsOctopus.tflite", JavaUtil.createListWith("Fish", "Gears", "Gears", "Octopus"), true, true, 320);
    tfod.initialize(vuforiaPOWERPLAY, (float) 0.7, true, true);
    tfod.setClippingMargins(0, 80, 0, 0);
    tfod.activate();
    tfod.setZoom(2.5, 9 / 9);
    telemetry.addData("TenserFlowReady", "Three dots");
    telemetry.update();
    // Put initialization blocks here.
    arm0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    arm1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    arm0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    arm1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    arm0.setTargetPosition(0);
    arm1.setTargetPosition(0);
    arm0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    arm0.setPower(1);
    arm1.setPower(1);
    frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    stepIncrement = 0.0075;
    maxSpeed = 0.5;
    SleepDuration = 100;
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      RunTensor(ParkingVariable);
      // telemetry.addData("ParkingSpot", ParkingVariable);
      // telemetry.update();
      CloseClaw();
      // claw.setPower(0.1);
      // stepIncrement = 0.0125;
      // maxSpeed = 0.75;
      Forward_Backward(1696);
      arm0.setTargetPosition(130);
      arm1.setTargetPosition(-130);
      // sleep(100);
      // stepIncrement = 0.0075;
      // maxSpeed = 0.5;
      RotateLeft_Right(-254.4);
      Forward_Backward(170);
      arm0.setTargetPosition(100);
      arm1.setTargetPosition(-100);
      // sleep(1000);
      OpenClaw();
      arm0.setTargetPosition(130);
      arm1.setTargetPosition(-130);
      RotateLeft_Right(10.6);
      // sleep(1000);
      Forward_Backward(-170);
      arm0.setTargetPosition(10);
      arm1.setTargetPosition(-10);
      // sleep(500);
      pivotLeft();
      pivotRight();
      // New Stuff
      arm0.setTargetPosition(16);
      arm1.setTargetPosition(-16);
      sleep(250);
      Forward_Backward(200);
      CloseClaw();
      claw.setPower(0.3);
      sleep(100);
      Forward_Backward(-30);
      arm0.setTargetPosition(75);
      arm1.setTargetPosition(-75);
      sleep(500);
      pivotLeft2();
      StrafeRight_Left(-30);
      arm0.setTargetPosition(25);
      arm1.setTargetPosition(-25);
      sleep(1000);
      OpenClaw();
      arm0.setTargetPosition(100);
      arm1.setTargetPosition(-100);
      telemetry.addData("Parking3", ParkingVariable);
      telemetry.update();
      if (ParkingVariable != 1250) {
        StrafeRight_Left(ParkingVariable);
      } else {
        stepIncrement = 0.015;
        StrafeRight_Left(ParkingVariable);
      }
    }

    vuforiaPOWERPLAY.close();
    tfod.close();
  }

  /**
   * Positive is Right
   * Negative is Left
   */
  private void StrafeRight_Left(int Distance2) {
    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearRight.setTargetPosition(-Distance2);
    frontRight.setTargetPosition(Distance2);
    frontLeft.setTargetPosition(Distance2);
    rearLeft.setTargetPosition(-Distance2);
    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    StepSpeed = 0;
    speedScale = 0.001;
    while (frontLeft.isBusy()) {
      if (StepSpeed < maxSpeed) {
        StepSpeed += stepIncrement;
      }
      rlE = Math.abs(frontLeft.getCurrentPosition()) - Math.abs(rearLeft.getCurrentPosition());
      rrE = Math.abs(frontLeft.getCurrentPosition()) - Math.abs(rearRight.getCurrentPosition());
      frE = Math.abs(frontLeft.getCurrentPosition()) - Math.abs(frontRight.getCurrentPosition());
      rearRight.setPower(StepSpeed + speedScale * rrE);
      frontRight.setPower(StepSpeed + speedScale * frE);
      frontLeft.setPower(StepSpeed);
      rearLeft.setPower(StepSpeed + speedScale * rlE);
      telemetry.addData("tp", frontRight.getTargetPosition());
      telemetry.addData("cp", frontRight.getCurrentPosition());
      telemetry.addData("key", frontRight.getPower());
      telemetry.update();
    }
    rearRight.setPower(0);
    frontRight.setPower(0);
    frontLeft.setPower(0);
    rearLeft.setPower(0);
    sleep(SleepDuration);
  }

  /**
   * Forward is positive distance, Back is Negative distance.
   */
  private void Forward_Backward(int Distance2) {
    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearRight.setTargetPosition(-Distance2);
    frontRight.setTargetPosition(-Distance2);
    frontLeft.setTargetPosition(Distance2);
    rearLeft.setTargetPosition(Distance2);
    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    StepSpeed = 0.05;
    speedScale = 0.003;
    while (frontLeft.isBusy()) {
      if (StepSpeed < maxSpeed) {
        StepSpeed += stepIncrement;
      }
      rlE = Math.abs(frontLeft.getCurrentPosition()) - Math.abs(rearLeft.getCurrentPosition());
      rrE = Math.abs(frontLeft.getCurrentPosition()) - Math.abs(rearRight.getCurrentPosition());
      frE = Math.abs(frontLeft.getCurrentPosition()) - Math.abs(frontRight.getCurrentPosition());
      rearRight.setPower(StepSpeed + speedScale * rrE);
      frontRight.setPower(StepSpeed + speedScale * frE);
      frontLeft.setPower(StepSpeed);
      rearLeft.setPower(StepSpeed + speedScale * rlE);
      telemetry.addData("tp", frontRight.getTargetPosition());
      telemetry.addData("cp", frontRight.getCurrentPosition());
      telemetry.addData("key", frontRight.getPower());
      telemetry.update();
    }
    rearRight.setPower(0);
    frontRight.setPower(0);
    frontLeft.setPower(0);
    rearLeft.setPower(0);
    sleep(SleepDuration);
  }

  /**
   * Positive is
   * Negative is
   */
  private void RotateLeft_Right(double Distance2) {
    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearRight.setTargetPosition((int) Distance2);
    frontRight.setTargetPosition((int) Distance2);
    frontLeft.setTargetPosition((int) Distance2);
    rearLeft.setTargetPosition((int) Distance2);
    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    StepSpeed = 0;
    speedScale = 0.001;
    while (frontLeft.isBusy()) {
      if (StepSpeed < maxSpeed) {
        StepSpeed += stepIncrement;
      }
      rlE = Math.abs(frontLeft.getCurrentPosition()) - Math.abs(rearLeft.getCurrentPosition());
      rrE = Math.abs(frontLeft.getCurrentPosition()) - Math.abs(rearRight.getCurrentPosition());
      frE = Math.abs(frontLeft.getCurrentPosition()) - Math.abs(frontRight.getCurrentPosition());
      rearRight.setPower(StepSpeed + speedScale * rrE);
      frontRight.setPower(StepSpeed + speedScale * frE);
      frontLeft.setPower(StepSpeed);
      rearLeft.setPower(StepSpeed + speedScale * rlE);
      telemetry.addData("tp", frontRight.getTargetPosition());
      telemetry.addData("cp", frontRight.getCurrentPosition());
      telemetry.addData("key", frontRight.getPower());
      telemetry.update();
    }
    rearRight.setPower(0);
    frontRight.setPower(0);
    frontLeft.setPower(0);
    rearLeft.setPower(0);
    sleep(SleepDuration);
  }

  /**
   * Describe this function...
   */
  private void pivotLeft() {
    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontLeft.setTargetPosition((int) 286);
    rearLeft.setTargetPosition(0);
    frontRight.setTargetPosition((int) 519);
    rearRight.setTargetPosition((int) 286);
    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    while (rearRight.isBusy()) {
      frontLeft.setPower(0.35);
      rearLeft.setPower(1);
      frontRight.setPower(0.7);
      rearRight.setPower(0.35);
    }
    frontLeft.setPower(0);
    rearLeft.setPower(0);
    frontRight.setPower(0);
    rearRight.setPower(0);
    sleep(500);
  }

  /**
   * Describe this function...
   */
  private void pivotRight() {
    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontLeft.setTargetPosition((int) 1012.2);
    rearLeft.setTargetPosition((int) 532.6);
    frontRight.setTargetPosition((int) 532.6);
    rearRight.setTargetPosition(0);
    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    while (frontLeft.isBusy()) {
      frontLeft.setPower(0.7);
      rearLeft.setPower(0.35);
      frontRight.setPower(0.35);
      rearRight.setPower(1);
    }
    frontLeft.setPower(0);
    rearLeft.setPower(0);
    frontRight.setPower(0);
    rearRight.setPower(0);
  }

  /**
   * Describe this function...
   */
  private void pivotLeft2() {
    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontLeft.setTargetPosition((int) 572.4);
    rearLeft.setTargetPosition(0);
    frontRight.setTargetPosition((int) 1038.8);
    rearRight.setTargetPosition((int) 572.4);
    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    while (rearRight.isBusy()) {
      frontLeft.setPower(0.35);
      rearLeft.setPower(1);
      frontRight.setPower(0.7);
      rearRight.setPower(0.35);
    }
    frontLeft.setPower(0);
    rearLeft.setPower(0);
    frontRight.setPower(0);
    rearRight.setPower(0);
    sleep(500);
  }

  /**
   * Describe this function...
   */
  private void do_something(int i) {
    telemetry.addData("label" + i, Recognition2.getLabel());
    if (Recognition2.getLabel().equals("Gears")) {
      ParkingVariable = 1250;
    } else if (Recognition2.getLabel().equals("Fish")) {
      ParkingVariable = 424;
    } else if (Recognition2.getLabel().equals("Octopus")) {
      ParkingVariable = -424;
    } else {
    }
    telemetry.addData("Parking1", ParkingVariable);
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  private void RunTensor(double ParkingVariable) {
    boolean ObjectDetected;
    List<Recognition> Recognitions;
    int Index;

    ObjectDetected = false;
    while (ObjectDetected == false) {
      Recognitions = tfod.getRecognitions();
      if (JavaUtil.listLength(Recognitions) == 0) {
        telemetry.addData("TenserFlow", "Nothing Found");
      } else {
        Index = 0;
        for (Recognition Recognition2_item : Recognitions) {
          Recognition2 = Recognition2_item;
          do_something(Index);
        }
        ObjectDetected = true;
      }
    }
    telemetry.addData("Parking2", ParkingVariable);
    telemetry.update();
  }
}
