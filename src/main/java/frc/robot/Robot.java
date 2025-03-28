// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.spark.config.SoftLimitConfig;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
//import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
//import frc.robot.Constants.ModuleConstants;



/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {


  private final PS4Controller Janky = new PS4Controller(2);
  

  private final SparkFlex m_Algae_Intake = new SparkFlex(10,MotorType.kBrushless);

  private final SparkMax m_Algae_Retrack = new SparkMax(11,MotorType.kBrushless);

  private final SparkFlex m_Deep_Climb = new SparkFlex(12,MotorType.kBrushless);

  /* 
  private final SparkFlex m_ElevatorTower = new SparkFlex(12,MotorType.kBrushless);

  private final SparkMax m_Coral_Retrack = new SparkMax(13,MotorType.kBrushless);

  private final SparkMax m_Coral_Intake = new SparkMax(14,MotorType.kBrushless);
  */
  

  UsbCamera camera1;
  UsbCamera camera2;

  NetworkTableEntry cameraSelection;

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    CameraServer.startAutomaticCapture(0);
    CameraServer.startAutomaticCapture(1);

    cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");

    //camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    //camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    
    SparkMaxConfig AlgaeConfig = new SparkMaxConfig();

    SparkFlexConfig DeepClimbConfig = new SparkFlexConfig();
    
    /* 
    SparkFlexConfig ElevatorConfig = new SparkFlexConfig();

    SparkMaxConfig CoralConfig = new SparkMaxConfig();
    */




    AlgaeConfig.softLimit
        .reverseSoftLimit(-16)
        .reverseSoftLimitEnabled(true)
        .forwardSoftLimit(-1.5)
        .forwardSoftLimitEnabled(true);

    DeepClimbConfig.softLimit
        .reverseSoftLimit(-16)
        .reverseSoftLimitEnabled(false)
        .forwardSoftLimit(-1.5)
        .forwardSoftLimitEnabled(false);
  
    /* 
    ElevatorConfig.softLimit
        .reverseSoftLimit(0.2)
        .reverseSoftLimitEnabled(true)
        .forwardSoftLimit(165)
        .forwardSoftLimitEnabled(true);

    CoralConfig.softLimit
        .reverseSoftLimit(-44)
        .reverseSoftLimitEnabled(true)
        .forwardSoftLimit(-1)
        .forwardSoftLimitEnabled(true);
    */

    m_Algae_Retrack.configure(AlgaeConfig, ResetMode.kResetSafeParameters,PersistMode.kPersistParameters);

    m_Deep_Climb.configure(DeepClimbConfig, ResetMode.kResetSafeParameters,PersistMode.kPersistParameters);

    /* 
    m_ElevatorTower.configure(ElevatorConfig, ResetMode.kResetSafeParameters,PersistMode.kPersistParameters);

    m_Coral_Retrack.configure(CoralConfig, ResetMode.kResetSafeParameters,PersistMode.kPersistParameters);
    */

    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {



    // Algae intake
    if (Janky.getPOV() == 270) {


      m_Algae_Intake.set(0.8);


    }  else if (Janky.getPOV() == 90) {


      m_Algae_Intake.set(-0.8); 


    } else {  m_Algae_Intake.set(0);  }

    
    
    // Algae Retrack
    if (Janky.getL2Button()) {


      m_Algae_Retrack.set(0.5);


    } else if (Janky.getL1Button()) {

  
      m_Algae_Retrack.set(-0.5); 


    } else {m_Algae_Retrack.set(0); }
 


    // Deep Climb
    if (Janky.getTriangleButton()) {


      m_Deep_Climb.set(0.7);


    } else if (Janky.getCrossButton()) {


      m_Deep_Climb.set(-0.7);


    } else { m_Deep_Climb.set(0);  }
    


    /* 
    // Elevator Tower
    if (Janky.getTriangleButton()) {


      m_ElevatorTower.set(0.6);


    } else if (Janky.getCrossButton()) {


      m_ElevatorTower.set(-0.6);


    } else { m_ElevatorTower.set(0);  }
 
       

    // Coral Retrack
    if (Janky.getR1Button()) {


      m_Coral_Retrack.set(0.3);
  

    } else if (Janky.getR2Button()) {
  

      m_Coral_Retrack.set(-0.3); 
  

    } else {m_Coral_Retrack.set(0);}



    // Coral Intake
    if (Janky.getCircleButton()) {


      m_Coral_Intake.set(0.7);
  

    } else if (Janky.getSquareButton()) {
  

      m_Coral_Intake.set(-0.7); 
  

    } else {m_Coral_Intake.set(0);}
  
  */

  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
