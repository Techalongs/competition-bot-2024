package org.firstinspires.ftc.teamcode.test;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;

@TeleOp(name = "Pedro TeleOp", group = "Test")
public class PedroTeleOp extends OpMode {

    protected GamepadEx driver;
    protected GamepadEx operator;
    protected CommandScheduler commandScheduler;
    protected Follower follower;
    protected boolean driveRobotCentric = true;
    protected boolean squareInputs = true;
    protected boolean driveWithDpad = false;
    protected double dpadDrivePower = 0.2;
    protected double dpadTurnPower = 0.2;

    @Override
    public void init() {
        Constants.setConstants(FConstants.class, LConstants.class);
        commandScheduler = CommandScheduler.getInstance();
        commandScheduler.enable();
        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);
        follower = new Follower(hardwareMap);

        driver.getGamepadButton(GamepadKeys.Button.OPTIONS).whenPressed(() -> follower.resetOffset());
        driver.getGamepadButton(GamepadKeys.Button.SHARE).whenPressed(() -> driveWithDpad = !driveWithDpad);
    }

    @Override
    public void start() {
        follower.startTeleopDrive();
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void loop() {
        commandScheduler.run();
        driver.readButtons();
        operator.readButtons();
        /* Update Pedro to move the robot based on:
        - Forward/Backward Movement: -gamepad1.left_stick_y
        - Left/Right Movement: -gamepad1.left_stick_x
        - Turn Left/Right Movement: -gamepad1.right_stick_x
        - Robot-Centric Mode: true
        */

//        -gamepad.left_stick_y, gamepad.left_stick_x, gamepad.right_stick_y
        double[] driveVectors = getDriveVectors();
        follower.setTeleOpMovementVectors(driveVectors[0], driveVectors[1], driveVectors[2], driveRobotCentric);
        follower.update();

        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Drive w/ D-Pad", driveWithDpad);
        telemetry.update();
    }

    @Override
    public void stop() {
    }

    protected double[] getDriveVectors() {
        double forward = 0.0, lateral = 0.0, heading = 0.0;
        if (driveWithDpad) {
            if (driver.isDown(GamepadKeys.Button.DPAD_UP)) {
                forward = dpadDrivePower;
            } else if (driver.isDown(GamepadKeys.Button.DPAD_DOWN)) {
                forward = -dpadDrivePower;
            } else if (driver.isDown(GamepadKeys.Button.DPAD_LEFT)) {
                lateral = dpadDrivePower;
            } else if (driver.isDown(GamepadKeys.Button.DPAD_RIGHT)) {
                lateral = -dpadDrivePower;
            }
            if (driver.isDown(GamepadKeys.Button.LEFT_BUMPER)) {
                heading = -dpadTurnPower;
            } else if (driver.isDown(GamepadKeys.Button.RIGHT_BUMPER)) {
                heading = dpadTurnPower;
            }
        } else {
            forward = driver.getLeftY();
            lateral = -driver.getLeftX();
            heading = -driver.getRightX();
            if (squareInputs) {
                lateral = Math.signum(lateral) * lateral * lateral;
                forward = Math.signum(forward) * forward * forward;
                heading = Math.signum(heading) * heading * heading;
            }
        }
        return new double[] { forward, lateral, heading };
    }

}
