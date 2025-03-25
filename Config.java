package org.firstinspires.ftc.teamcode;


public class Config {
  public static final double STRAFE_DRIFT_COMPENSATION = 1;
  public static final double FWD_DRIFT_COMPENSATION = 1;
  public static final int TICKS_PER_360_DEG = 2009;

  public static final double AUTONOMOUS_MOVE_SPEED = 0.4;
  public static final double AUTONOMOUS_DIAGONAL_SPEED = 0.45;
  public static final double AUTONOMOUS_TURN_SPEED = 0.4;
  public static final double BUMP_SPEED = 0.2;
  public static final double AUTO_PIVOT_POWER = 0.4;
  public static final double GYRO_CORRECTION = 0.03;
  
  public static final int LOW_POLE_HEIGHT = 70;
  public static final int MID_POLE_HEIGHT = 90;
  public static final int HIGH_POLE_HEIGHT = 120;
  public static final int SIDE_STACK_HEIGHT = 45;
  
  public static final int BOTTOM = 0;
  public static final int CRUISING_HEIGHT = 25;
  public static final int TOP = 125;
  
  public static final int TILE_LENGTH = 800;
  public static final int D_TILE_LENGTH = 1300;
  public static final int CORRECTION = (int)(TILE_LENGTH * 0.10);
  public static final int BUMP = 175;
  public static final int DIAGONAL_BUMP = 325;
}