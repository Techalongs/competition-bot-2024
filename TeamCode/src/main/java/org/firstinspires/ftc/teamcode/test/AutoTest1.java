package org.firstinspires.ftc.teamcode.test;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.util.Constants;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.HorizontalClaw;
import org.firstinspires.ftc.teamcode.Positions;
import org.firstinspires.ftc.teamcode.VerticalClaw;
import org.firstinspires.ftc.teamcode.VerticalExtension;
import org.firstinspires.ftc.teamcode.async.HorizontalArm;
import org.firstinspires.ftc.teamcode.async.VerticalArm;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.util.TeamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Autonomous(name = "Auto Test 1")
public class AutoTest1 extends OpMode {

    private CommandScheduler commandScheduler;
    private GamepadEx driver;

    private HorizontalArm horizontalArm;
    private VerticalArm verticalArm;

    private Follower follower;

    private double maxPower = 0.5;
    private Pose startPose;
    private Pose endPose;
    private PathPosition pathPosition;
    private BezierLine forwardLine;
    private BezierLine backwardLine;
    private PathChain forwardPathChain;
    private PathChain backwardPathChain;

    private boolean readyForNextPath;
    private String status;
    private String currentPathName = "Unknown";
    private String currentPathCallback = "Unknown";

    private enum PathPosition {
        START,
        END
    }

    @Override
    public void init() {
        Constants.setConstants(FConstants.class, LConstants.class);
        commandScheduler = CommandScheduler.getInstance();
        commandScheduler.enable();
        driver = new GamepadEx(gamepad1);

        horizontalArm = new HorizontalArm(hardwareMap);
        verticalArm = new VerticalArm(hardwareMap);

        pathPosition = PathPosition.START;
        startPose = new Pose(0, 0, Math.toRadians(-90), false);
        endPose = new Pose(0, -80, Math.toRadians(-90), false);

        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        follower.setMaxPower(maxPower);

        forwardLine = new BezierLine(startPose, endPose);
        backwardLine = new BezierLine(endPose, startPose);

        Action jazzHands = new SequentialAction(
                verticalArm.openClaw(),
                new SleepAction(0.5),
                verticalArm.hingeTo(Positions.VerticalHingePosition.UP),
                new SleepAction(0.5),
                verticalArm.openClaw(),
                verticalArm.hingeTo(Positions.VerticalHingePosition.DOWN)
        );

        forwardPathChain = follower.pathBuilder()
                .addPath(forwardLine)
                .addParametricCallback(0.10, () -> {
                    currentPathCallback = "0.10";
//                    Actions.runBlocking(verticalArm.moveExtensionTo(Positions.VerticalExtPosition.SPECIMEN_2, 0.6));
//                    Actions.runBlocking(verticalArm.hingeTo(Positions.VerticalHingePosition.UP));
                    Actions.runBlocking(newJazzHands());
                })
//                .addParametricCallback(0.40, () -> {
//                    currentPathCallback = "0.40";
//                    Actions.runBlocking(verticalArm.hingeTo(Positions.VerticalHingePosition.UP));
//                })
//                .addParametricCallback(0.70, () -> {
//                    currentPathCallback = "0.70";
//                    Actions.runBlocking(verticalArm.hingeTo(Positions.VerticalHingePosition.DOWN));
//                })
                .addParametricCallback(0.99, () -> { pathPosition = PathPosition.END; readyForNextPath = true; })
                .build();

        backwardPathChain = follower.pathBuilder()
                .addPath(backwardLine)
                .addParametricCallback(0.20, () -> {
                    currentPathCallback = "0.20";
//                    Actions.runBlocking(verticalArm.moveExtensionTo(Positions.VerticalExtPosition.SPECIMEN_1, 0.6));
//                    Actions.runBlocking(verticalArm.hingeTo(Positions.VerticalHingePosition.DOWN));
                    Actions.runBlocking(newJazzHands());
                })
//                .addParametricCallback(0.40, () -> {
//                    currentPathCallback = "0.40";
//                    Actions.runBlocking(verticalArm.hingeTo(Positions.VerticalHingePosition.UP));
//                })
//                .addParametricCallback(0.70, () -> {
//                    currentPathCallback = "0.70";
//                    Actions.runBlocking(verticalArm.hingeTo(Positions.VerticalHingePosition.DOWN));
//                })
                .addParametricCallback(0.99, () -> { pathPosition = PathPosition.START; readyForNextPath = true; })
                .build();

//        TeamUtils.runAndSleep(horizontalArm.openClaw(), 2);
//        Actions.runBlocking(verticalArm.moveExtensionTo(Positions.VerticalExtPosition.SPECIMEN_1, 0.5));

        Actions.runBlocking(verticalArm.hingeTo(Positions.VerticalHingePosition.DOWN));
        TeamUtils.runAndSleep(horizontalArm.closeClaw(), 2);

        status = "Initialized";
        this.logData();
    }

    @Override
    public void start() {
        status = "Started";
        readyForNextPath = true;
        this.logData();
    }

    @Override
    public void loop() {
//        Actions.runBlocking(new SequentialAction(
//                new SleepAction(5),
//                verticalArm.hingeTo(Positions.VerticalHingePosition.UP),
//                new SleepAction(5),
//                verticalArm.hingeTo(Positions.VerticalHingePosition.DOWN),
//                new SleepAction(5)
//        ));
        follower.update();
        if (readyForNextPath) {
            PathChain nextPath;
            switch (pathPosition) {
                case START:
                    nextPath = forwardPathChain;
                    currentPathName = "ForwardPathChain";
                    break;
                case END:
                    nextPath = backwardPathChain;
                    currentPathName = "BackwardPathChain";
                    break;
                default:
                    nextPath = null;
                    currentPathName = "None";
                    break;
            }
            readyForNextPath = false;
            if (nextPath != null) {
                currentPathCallback = "0";
                follower.followPath(nextPath, maxPower, true);
                follower.update();
            }
            telemetry.addData("Path", nextPath);
        }
        this.logData();
    }

    @Override
    public void stop() {
        status = "Stopped";
        this.logData();
    }

    public Action newJazzHands() {
        Action jazzHands = new SequentialAction(
                verticalArm.openClaw(),
//                new SleepAction(0.5),
                verticalArm.hingeTo(Positions.VerticalHingePosition.UP),
//                new SleepAction(0.5),
                verticalArm.openClaw(),
                verticalArm.hingeTo(Positions.VerticalHingePosition.DOWN)
        );
        return jazzHands;
    }

    private void logData() {
        telemetry.addData("Status", status);
        telemetry.addLine("PATHING |");
        telemetry.addData("• Path Position", pathPosition);
        telemetry.addData("• Current Path", currentPathName);
        telemetry.addData("• Current Callback", currentPathCallback);
        telemetry.addData("• Ready for Next Path", readyForNextPath);
        telemetry.addLine("ODOMETRY |");
        telemetry.addData("• Position", "(" + follower.getPose().getX() + ", " + follower.getPose().getY() + ")");
        telemetry.update();
    }

}
