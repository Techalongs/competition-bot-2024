package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;

@Autonomous(name = "Sample Autonomous")
public class SampleAuto extends OpMode {
    private VerticalExtension extension;
    private VerticalClaw verticalClaw;
    private HorizontalClaw horizontalClaw;

    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private PathChain[] paths;
    private int pathState;

    @Override
    public void init() {
        extension = new VerticalExtension(hardwareMap);
        verticalClaw = new VerticalClaw(hardwareMap);
        horizontalClaw = new HorizontalClaw(hardwareMap);

        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(new Pose(9, 87, Math.toRadians(0), true));
        buildPaths();

        Actions.runBlocking(
                new SequentialAction(
                        horizontalClaw.hingeTo(HorizontalClaw.HingePosition.UP),
                        horizontalClaw.open(),
                        horizontalClaw.wristTo(HorizontalClaw.WristPosition.UP),
                        verticalClaw.close()
                )
        );
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("t-value", follower.getCurrentTValue());
        telemetry.update();
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    private void buildPaths() {
        paths = new PathChain[8];

        /* Note: Line _ corresponds to lines generated on https://pedro-path-generator.vercel.app/
         * File for this Auto is /Downloads/SampleAutoTraj2025.txt
         */

        paths[0] = follower.pathBuilder()
                .addPath(
                        // Line 1
                        new BezierLine(
                                new Point(9.000, 87.000, Point.CARTESIAN),
                                new Point(15.000, 87.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 2
                        new BezierLine(
                                new Point(15.000, 87.000, Point.CARTESIAN),
                                new Point(15.000, 126.000, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(135))
                .build();

        paths[1] = follower.pathBuilder()
                .addPath(
                        // Line 3
                        new BezierLine(
                                new Point(15.000, 126.000, Point.CARTESIAN),
                                new Point(33.000, 117.000, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))
                .build();

        paths[2] = follower.pathBuilder()
                .addPath(
                        // Line 4
                        new BezierLine(
                                new Point(33.000, 117.000, Point.CARTESIAN),
                                new Point(15.000, 126.000, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                .build();

        paths[3] = follower.pathBuilder()
                .addPath(
                        // Line 5
                        new BezierLine(
                                new Point(15.000, 126.000, Point.CARTESIAN),
                                new Point(33.000, 128.000, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))
                .build();

        paths[4] = follower.pathBuilder()
                .addPath(
                        // Line 6
                        new BezierLine(
                                new Point(33.000, 128.000, Point.CARTESIAN),
                                new Point(15.000, 126.000, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                .build();

        paths[5] = follower.pathBuilder()
                .addPath(
                        // Line 7
                        new BezierLine(
                                new Point(15.000, 126.000, Point.CARTESIAN),
                                new Point(32.000, 130.000, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(210))
                .build();

        paths[6] = follower.pathBuilder()
                .addPath(
                        // Line 8
                        new BezierLine(
                                new Point(32.000, 130.000, Point.CARTESIAN),
                                new Point(15.000, 126.000, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(210), Math.toRadians(135))
                .build();

        paths[7] = follower.pathBuilder()
                .addPath(
                        // Line 9
                        new BezierCurve(
                                new Point(16.000, 126.000, Point.CARTESIAN),
                                new Point(60.000, 120.000, Point.CARTESIAN),
                                new Point(60.000, 97.000, Point.CARTESIAN)
                        )
                )
                .setTangentHeadingInterpolation()
                .build();
    }

    private void autonomousPathUpdate() {
        Action dropSample =
                new SequentialAction(
                        verticalClaw.hingeTo(VerticalClaw.HingePosition.SPECIMEN),
                        extension.moveTo(VerticalExtension.Position.TOP),
                        verticalClaw.hingeTo(VerticalClaw.HingePosition.UP),
                        new SleepAction(0.5),
                        verticalClaw.open(),
                        new SleepAction(0.5),
                        verticalClaw.close(),
                        new SleepAction(0.25),
                        verticalClaw.hingeTo(VerticalClaw.HingePosition.DOWN)
                );

        Action readyForPickup =
                new SequentialAction(
                        extension.moveTo(VerticalExtension.Position.BOTTOM),
                        verticalClaw.open()
                );

        Action pickup =
                new SequentialAction(
                        horizontalClaw.hingeTo(HorizontalClaw.HingePosition.DOWN),
                        new SleepAction(0.5),
                        horizontalClaw.wristTo(HorizontalClaw.WristPosition.MID),
                        new SleepAction(0.5),
                        horizontalClaw.close(),
                        new SleepAction(0.5),
                        horizontalClaw.hingeTo(HorizontalClaw.HingePosition.UP)
                );

        Action handoff =
                new SequentialAction(
                        new SleepAction(1),
                        horizontalClaw.wristTo(HorizontalClaw.WristPosition.DOWN),
                        new SleepAction(0.75),
                        horizontalClaw.wristTo(HorizontalClaw.WristPosition.UP),
                        new SleepAction(0.5),
                        verticalClaw.close(),
                        new SleepAction(0.5),
                        horizontalClaw.open()
                );

        switch (pathState) {
            case 0: // Go to bucket
                follower.followPath(paths[0]);
                setPathState(1);
                break;
            case 1: // Get sample
            case 3:
            case 5:
                if (!follower.isBusy()) {
                    Actions.runBlocking(dropSample);
                    follower.followPath(paths[pathState]);
                    Actions.runBlocking(readyForPickup);
                    setPathState(pathState + 1);
                    break;
                }
            case 2: // Go back to bucket
            case 4:
            case 6:
                if (!follower.isBusy()) {
                    Actions.runBlocking(pickup);
                    Actions.runBlocking(handoff);
                    follower.followPath(paths[pathState]);
                    setPathState((pathState != 6) ? pathState + 1 : 7);
                    break;
                }
            case 7: // Go to park
                if (!follower.isBusy()) {
                    Actions.runBlocking(dropSample);
                    follower.followPath(paths[pathState]);
                    Actions.runBlocking(extension.moveTo(VerticalExtension.Position.BOTTOM));
                    setPathState(-1);
                    break;
                }
        }
    }
}