package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
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

@Autonomous(name = "Specimen Autonomous")
public class SpecimenAuto extends OpMode {
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
        follower.setStartingPose(new Pose(9, 57, Math.toRadians(0), true));
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
        telemetry.update();
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    private void buildPaths() {
        paths = new PathChain[2];

        /* Note: Line _ corresponds to lines generated on https://pedro-path-generator.vercel.app/
         * File for this Auto is /Downloads/SpecimenAutoTraj2025.txt
         */

        paths[0] = follower.pathBuilder()
                .addPath(
                        // Line 1
                        new BezierLine(
                                new Point(9.000, 85.000, Point.CARTESIAN),
                                new Point(38.000, 72.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        paths[1] = follower.pathBuilder()
                .addPath(
                        // Line 2
                        new BezierCurve(
                                new Point(38.000, 72.000, Point.CARTESIAN),
                                new Point(1.000, 23.000, Point.CARTESIAN),
                                new Point(83.000, 42.000, Point.CARTESIAN),
                                new Point(58.000, 25.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 3
                        new BezierLine(
                                new Point(58.000, 25.000, Point.CARTESIAN),
                                new Point(15.000, 25.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 4
                        new BezierCurve(
                                new Point(15.000, 25.000, Point.CARTESIAN),
                                new Point(68.000, 30.000, Point.CARTESIAN),
                                new Point(58.000, 14.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 5
                        new BezierLine(
                                new Point(58.000, 14.000, Point.CARTESIAN),
                                new Point(15.000, 14.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 6
                        new BezierCurve(
                                new Point(15.000, 14.000, Point.CARTESIAN),
                                new Point(68.000, 20.000, Point.CARTESIAN),
                                new Point(58.000, 8.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 7
                        new BezierLine(
                                new Point(58.000, 8.000, Point.CARTESIAN),
                                new Point(15.000, 8.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 8
                        new BezierCurve(
                                new Point(15.000, 8.000, Point.CARTESIAN),
                                new Point(37.000, 17.000, Point.CARTESIAN),
                                new Point(9.000, 25.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
    }

    private void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // To chamber
                follower.followPath(paths[0]);
                setPathState(1);
                break;
            case 1: // Shove samples into observation zone
                if (!follower.isBusy()) {
                    // Code for scoring specimen
                    Actions.runBlocking(
                            new SequentialAction(
                                    verticalClaw.hingeTo(VerticalClaw.HingePosition.SPECIMEN),
                                    extension.moveTo(VerticalExtension.Position.SPECIMEN_2),
                                    new SleepAction(1),
                                    verticalClaw.open(),
                                    extension.moveTo(VerticalExtension.Position.SPECIMEN_1),
                                    verticalClaw.close(),
                                    new ParallelAction(
                                            verticalClaw.hingeTo(VerticalClaw.HingePosition.DOWN),
                                            extension.moveTo(VerticalExtension.Position.BOTTOM),
                                            horizontalClaw.wristTo(HorizontalClaw.WristPosition.DOWN)
                                    )
                            )
                    );

                    follower.followPath(paths[1]);
                    setPathState(-1);
                    break;
                }
        }
    }
}
