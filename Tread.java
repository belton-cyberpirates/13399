package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;


public class Tread {
  private DcMotor front;
  private DcMotor back;
  // encoder
  private int direction = 1;


  public Tread(DcMotor front, DcMotor back) {
    this.front = front;
    this.back = back;
  }


  public Tread(DcMotor front, DcMotor back, boolean reverse) {
    this.front = front;
    this.back = back;

    this.direction = reverse ? -1 : 1;
  }


  /**
  Moves the tread to the desired position using the encoder(s)

  @param distance the target distance
  */
  public void Move(int distance) {
    this.Init()
    this.front.setTargetPosition(this.direction * distance);
    this.back.setTargetPosition(this.direction * distance);
  }


  /**
  Reset and re-initialize encoders for accurate distance read

  Should be done before each movement
  */
  private void Init() {
    this.Reset();
    this.SetPower(0);
    this.SetToRunPosition();
  }


  /**
  Reset all encoders
  */
  private void Reset() {
    this.front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    this.back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
  }


  /**
  Set all motors in tread to given power
  */
  public void SetPower(double power) {
    this.front.setPower(power);
    this.back.setPower(power);
  }


  /**
  Set motors to run to encoder position
  */
  private void SetToRunPosition() {
    this.front.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    this.back.setMode(DcMotor.RunMode.RUN_TO_POSITION);
  }
}