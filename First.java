package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "First (Blocks to Java)", group = "")
public class First extends LinearOpMode {

  private DcMotor frontLeft;
  private DcMotor frontRight;
  private DcMotor rearLeft;
  private DcMotor rearRight;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    frontLeft = hardwareMap.dcMotor.get("frontLeft");
    frontRight = hardwareMap.dcMotor.get("frontRight");
    rearLeft = hardwareMap.dcMotor.get("rearLeft");
    rearRight = hardwareMap.dcMotor.get("rearRight");

    frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
    waitForStart();
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        if (gamepad1.left_stick_y < -0.1) {
          frontLeft.setPower(-1);
          rearLeft.setPower(-1);
        } else if (gamepad1.left_stick_y > 0.1) {
          frontLeft.setPower(1);
          rearLeft.setPower(1);
        } else {
          if (gamepad1.left_stick_x < -0.1) {
            frontLeft.setPower(1);
            rearLeft.setPower(-1);
          } else if (gamepad1.left_stick_x > 0.1) {
            frontLeft.setPower(-1);
            rearLeft.setPower(1);
          } else {
            frontLeft.setPower(0);
            rearLeft.setPower(0);
          }
        }
        if (gamepad1.right_stick_y < -0.1) {
          frontRight.setPower(-1);
          rearRight.setPower(-1);
        } else if (gamepad1.right_stick_y > 0.1) {
          frontRight.setPower(1);
          rearRight.setPower(1);
        } else {
          if (gamepad1.right_stick_x < -0.1) {
            frontRight.setPower(-1);
            rearRight.setPower(1);
          } else if (gamepad1.right_stick_x > 0.1) {
            frontRight.setPower(1);
            rearRight.setPower(-1);
          } else {
            frontRight.setPower(0);
            rearRight.setPower(0);
          }
        }
      }
    }
  }
}
