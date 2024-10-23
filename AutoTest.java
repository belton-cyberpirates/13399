package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Config;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name = "BobsAuto")


public class AutoTest {
  DcMotorEx frontRight;
  DcMotorEx frontLeft;
  DcMotorEx backRight;
  DcMotorEx backLeft;
  
  
  /*
   * Called on initialization
   */
  public void AutoTest() {
    this.frontRight = hardwareMap.get(DcMotorEx.class, "");
    this.frontLeft = hardwareMap.get(DcMotorEx.class, "");
    this.backRight = hardwareMap.get(DcMotorEx.class, "");
    this.backLeft = hardwareMap.get(DcMotorEx.class, "");
  }
  
  
  private void motorInit() {
    this.Reset();
    this.SetZeroBehaviour();
    this.setTargetPositions(0, 0, 0, 0);
    this.SetToRunPosition();
  }
  
    
  private void Reset() {
    this.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    this.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    this.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    this.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
   
  }


  private void SetToRunPosition() {
    this.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    this.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    this.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    this.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
  }
 
 
  private void SetZeroBehaviour() {
    this.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    this.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    this.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    this.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
  }
  
  
  void forward(int dist) {
    this.MotorInit();
    this.setVelocity(Config.DRIVE_VELOCITY);
    
    this.frontLeft.setTargetPosition(dist);
    this.frontRight.setTargetPosition(-dist);
    this.backLeft.setTargetPosition(dist);
    this.backRight.setTargetPosition(-dist);
  }
  
  
  void turn(int angle) {
    this.MotorInit();
    this.setVelocity(Config.TURN_VELOCITY);
    
    int ticks = angle * Config.TICKS_TO_DEGREES;
    
    this.frontLeft.setTargetPosition(ticks);
    this.frontRight.setTargetPosition(ticks);
    this.backLeft.setTargetPosition(ticks);
    this.backRight.setTargetPosition(ticks);
  }
  
  void setVelocity(int velocity) {
      this.frontLeft.setVelocity(velocity);
      this.frontRight.setVelocity(velocity);
      this.backLeft.setVelocity(velocity);
      this.backRight.setVelocity(velocity);
  }
 void arm_move(int ticks, boolean waitForArm) {
    this.setVelocity(Config.ARM_VELOCITY);
    this.setTargetPosition(ticks)
    while (waitForArm && arm.isBusy()) {}
  }
  
  @Override
  public void runOpMode() {
    motorInit();
    
    waitForStart();
    
    if (opModeIsActive()) { // ---------------START----------------
      forward(4 * Config.INCH);
      
    }
  }
}