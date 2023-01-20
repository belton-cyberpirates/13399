package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import org.firstinspires.ftc.teamcode.Direction;
import org.firstinspires.ftc.teamcode.Config;


public class DriveMotors {
  private DcMotor frontLeft;
  private DcMotor frontRight;
  private DcMotor backLeft;
  private DcMotor backRight;
  Orientation angles;





  public DriveMotors(DcMotor frontRight,
                     DcMotor frontLeft,
                     DcMotor backLeft,
                     DcMotor backRight) {
    this.frontRight = frontRight;
    this.frontLeft = frontLeft;
    this.backLeft = backLeft;
    this.backRight = backRight;
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
  
  
  private void MotorInit() {
    this.Reset();
    this.SetTargetPositions(0, 0, 0, 0);
    this.SetToRunPosition();
  }

  private void SetPower(double power, double compensation) {
    this.frontLeft.setPower(power * compensation);
    this.frontRight.setPower(power * compensation);
    this.backLeft.setPower(power);
    this.backRight.setPower(power);
  }
  
  
  private void SetTargetPositions(int fr, int fl, int bl, int br) {
    this.frontRight.setTargetPosition(fr);
    this.frontLeft.setTargetPosition(fl);
    this.backLeft.setTargetPosition(bl);
    this.backRight.setTargetPosition(br);
  }

  public void Pivot(int distance) {
    this.MotorInit();
    this.frontLeft.setPower(Config.AUTO_PIVOT_POWER);
    this.frontRight.setPower((int)(Config.AUTO_PIVOT_POWER * 1.5));
    this.backLeft.setPower(Config.AUTO_PIVOT_POWER);
    this.backRight.setPower(Config.AUTO_PIVOT_POWER);
    this.SetTargetPositions((int)(distance * 1.5), distance, 0, distance);
    WaitForMotors();
  }
  
  public void Move(Direction direction, int distance) {
    this.MotorInit();
    
    switch(direction) {
      case FORWARD:
        this.SetPower(Config.AUTONOMOUS_MOVE_SPEED, 1);
        this.SetTargetPositions(-distance, distance, distance, -distance);
        break;
  
      case BACKWARD:
        this.SetPower(Config.AUTONOMOUS_MOVE_SPEED, 1);
        this.SetTargetPositions(distance, -distance, -distance, distance);
        break;
  
      case LEFT:
        this.SetPower(Config.AUTONOMOUS_MOVE_SPEED, Config.STRAFE_DRIFT_COMPENSATION);
        this.SetTargetPositions((int)(-distance * Config.STRAFE_DRIFT_COMPENSATION),
                                (int)(-distance * Config.STRAFE_DRIFT_COMPENSATION),
                                distance,
                                distance);
        break;
  
      case RIGHT:
        this.SetPower(Config.AUTONOMOUS_MOVE_SPEED, Config.STRAFE_DRIFT_COMPENSATION);
        this.SetTargetPositions((int)(distance * Config.STRAFE_DRIFT_COMPENSATION),
                                (int)(distance * Config.STRAFE_DRIFT_COMPENSATION),
                                -distance,
                                -distance);
        break;
        
      case FRONT_RIGHT:
        this.SetPower(Config.AUTONOMOUS_DIAGONAL_SPEED, 1);
        this.SetTargetPositions(0, distance, 0, -distance);
        break;
        
      case BACK_LEFT:
        this.SetPower(Config.AUTONOMOUS_DIAGONAL_SPEED, 1);
        this.SetTargetPositions(0, -distance, 0, distance);
        break;
        
      case FRONT_LEFT:
        this.SetPower(Config.AUTONOMOUS_DIAGONAL_SPEED, 1);
        this.SetTargetPositions(-distance, 0, distance, 0);
        break;
        
      case BACK_RIGHT:
        this.SetPower(Config.AUTONOMOUS_DIAGONAL_SPEED, 1);
        this.SetTargetPositions(distance, 0, -distance, 0);
        break;
    }
    
    WaitForMotors();
  }
  
  
  public void Turn(int angle) {
    int distance = (int)((angle * Config.TICKS_PER_360_DEG) / 360);
    
    this.MotorInit();
    this.SetPower(Config.AUTONOMOUS_TURN_SPEED, 1);
    this.SetTargetPositions(distance, distance, distance, distance);
    
    WaitForMotors();
  }
  
  
  private void WaitForMotors() {
    while (!!(this.frontLeft.isBusy() ||
              this.frontRight.isBusy() ||
              this.backLeft.isBusy() ||
              this.backRight.isBusy() )) {}
  }

  //front right, front left, back left, back right
  public void PivotLeft() {
    MotorInit();
    this.SetTargetPositions(519, 0, 0, 0); //does the same thing as below
    // frontLeft.setTargetPosition((int) 286);
    // rearLeft.setTargetPosition(0);
    // frontRight.setTargetPosition((int) 519);
    // rearRight.setTargetPosition((int) 286);
    frontLeft.setPower(0);
    backLeft.setPower(1);
    frontRight.setPower(0.7);
    backRight.setPower(0);
    while (!!(this.frontRight.isBusy() ||
              this.backLeft.isBusy() )) {}
  }
  
  public void PivotRight() {
    MotorInit();
    this.SetTargetPositions(0, 1136, 0, 0); //does the same thing as below
    // frontLeft.setTargetPosition( 1012);
    // rearLeft.setTargetPosition(532);
    // frontRight.setTargetPosition(532);
    // rearRight.setTargetPosition(0);
    frontLeft.setPower(0.7);
    backLeft.setPower(0);
    frontRight.setPower(0);
    backRight.setPower(1);
    while (!!(this.frontLeft.isBusy() ||
              this.backRight.isBusy() )) {}
  }

  public void PivotLeft2() {
    MotorInit();
    SetTargetPositions(1400, 700, 0, 700); //does the same thing as below
    // frontLeft.setTargetPosition(572);
    // rearLeft.setTargetPosition(0);
    // frontRight.setTargetPosition(1038);
    // rearRight.setTargetPosition(572);
    frontLeft.setPower(0.35);
    backLeft.setPower(1);
    frontRight.setPower(0.7);
    backRight.setPower(0.35);
    while (!!(this.frontRight.isBusy() ||
              this.backLeft.isBusy() )) {}
    }

    public void MoveGyro(Direction direction, int distance) {
        this.MotorInit();
        
        this.SetPower(Config.AUTONOMOUS_MOVE_SPEED, 1);
        switch(direction) {
          case FORWARD:
            this.SetTargetPositions(-distance, distance, distance, -distance);
            break;
      
          case BACKWARD:
            this.SetTargetPositions(distance, -distance, -distance, distance);
            break;
      
          case LEFT:
          this.SetTargetPositions(-distance, -distance, distance, distance);
          break;
      
          case RIGHT:
            this.SetTargetPositions(distance, distance, -distance, -distance);
            break;
            
          case FRONT_RIGHT:
            this.SetTargetPositions(0, distance, 0, -distance);
            break;
            
          case BACK_LEFT:
            this.SetTargetPositions(0, -distance, 0, distance);
            break;
            
          case FRONT_LEFT:
            this.SetTargetPositions(-distance, 0, distance, 0);
            break;
            
          case BACK_RIGHT:
            this.SetTargetPositions(distance, 0, -distance, 0);
            break;
        }
        // while motors are running, correct power with gyro angle
        angles = AutoRight.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double initial = angles.firstAngle;
        while (this.frontLeft.isBusy() || this.frontRight.isBusy() || this.backLeft.isBusy() || this.backRight.isBusy() ) {
          angles = AutoRight.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
          GyroCorrect(direction, initial);
        }
      }
    
    
      private void GyroCorrect(Direction direction, double initial) {
        double correction = (angles.firstAngle - initial) * Config.GYRO_CORRECTION;
        //left drift is positive angle, right is negative
        switch(direction) {
          case FORWARD:
            this.setCorrection(-correction, correction, correction, -correction);
            break;
    
          case BACKWARD:
            this.setCorrection(correction, -correction, -correction, correction);
            break;
    
          case LEFT:
            this.setCorrection(-correction, -correction, correction, correction);
            break;
    
          case RIGHT:
            this.setCorrection(correction, correction, -correction, -correction);
            break;
    
          case FRONT_RIGHT:
            this.setCorrection(0, -correction, 0, correction);
            break;
    
          case FRONT_LEFT:
            this.setCorrection(correction, 0, -correction, 0);
            break;
    
          case BACK_LEFT:
            this.setCorrection(0, correction, 0, -correction);
            break;
    
          case BACK_RIGHT:
            this.setCorrection(-correction, 0, correction, 0);
            break;
    
        }
      }
    
    
      private void setCorrection(double fr, double fl, double bl, double br) {
        this.frontRight.setPower(Config.AUTONOMOUS_MOVE_SPEED + fr);
        this.frontLeft.setPower(Config.AUTONOMOUS_MOVE_SPEED + fl);
        this.backLeft.setPower(Config.AUTONOMOUS_MOVE_SPEED + bl);
        this.backRight.setPower(Config.AUTONOMOUS_MOVE_SPEED + br);
      }    

}
