package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
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

@SuppressWarnings("unused")
@Autonomous(name = "Asynch Specimen")
public class AsynchSpecimen extends OpMode {
    private VerticalExtension extension;
    private VerticalClaw verticalClaw;
    private HorizontalClaw horizontalClaw;

    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private PathChain[] paths;
    private int pathState;
    private Action currentAction;

    private Action scoreSpecimen;
    private Action pickupSpecimen;
    private Action handoff;

    @Override
    public void init() {
        extension = new VerticalExtension(hardwareMap);
        verticalClaw = new VerticalClaw(hardwareMap);
        horizontalClaw = new HorizontalClaw(hardwareMap);

        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        initActions();

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hardwareMap);
        follower.setStartingPose(new Pose(9, 57, Math.toRadians(0), true));
        buildPaths();

        Actions.runBlocking(
                new SequentialAction(
                        horizontalClaw.hingeTo(Positions.HorizontalHingePosition.UP),
                        horizontalClaw.open(),
                        horizontalClaw.wristTo(Positions.HorizontalWristPosition.UP),
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

        runCurrentAction();

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

    private void initActions() {
        scoreSpecimen =
                new SequentialAction(
                        verticalClaw.asynchHingeTo(Positions.VerticalHingePosition.SPECIMEN),
                        new SleepAction(0.3),
                        extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_2),
                        new SleepAction(0.25),
                        verticalClaw.open(),
                        new SleepAction(0.25),
                        extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_1),
                        verticalClaw.close(),
                        new ParallelAction(
                                verticalClaw.asynchHingeTo(Positions.VerticalHingePosition.DOWN),
                                extension.moveTo(Positions.VerticalExtPosition.BOTTOM),
                                horizontalClaw.wristTo(Positions.HorizontalWristPosition.DOWN)
                        )
                );

        pickupSpecimen =
                new SequentialAction(
                        horizontalClaw.close(),
                        verticalClaw.open(),
                        new SleepAction(0.25),
                        horizontalClaw.wristTo(Positions.HorizontalWristPosition.UP)
                );

        handoff =
                new SequentialAction(
                        new SleepAction(1),
                        horizontalClaw.wristTo(Positions.HorizontalWristPosition.DOWN),
                        new SleepAction(1),
                        horizontalClaw.wristTo(Positions.HorizontalWristPosition.UP),
                        new SleepAction(0.5),
                        verticalClaw.close(),
                        new SleepAction(0.5),
                        horizontalClaw.open()
                );
    }

    private void buildPaths() {
        paths = new PathChain[6];

        /* Note: Line _ corresponds to lines generated on https://pedro-path-generator.vercel.app/
         * File for this Auto is /Downloads/SpecimenAutoTraj2025.txt
         */

        paths[0] = follower.pathBuilder()
                .addPath(
                        // Line 1
                        new BezierLine(
                                new Point(9.000, 57.000, Point.CARTESIAN),
                                new Point(41.000, 74.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        paths[1] = follower.pathBuilder()
                .addPath(
                        // Line 2
                        new BezierCurve(
                                new Point(41.000, 74.000, Point.CARTESIAN),
                                new Point(1.000, 23.000, Point.CARTESIAN),
                                new Point(80.000, 42.000, Point.CARTESIAN),
                                new Point(58.000, 26.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 3
                        new BezierLine(
                                new Point(58.000, 26.000, Point.CARTESIAN),
                                new Point(30.000, 26.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 8
                        new BezierCurve(
                                new Point(30.000, 26.000, Point.CARTESIAN),
                                new Point(50.000, 27.000, Point.CARTESIAN),
                                new Point(8.000, 28.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        paths[2] = follower.pathBuilder()
                .addPath(
                        // Line 9
                        new BezierLine(
                                new Point(8.000, 28.000, Point.CARTESIAN),
                                new Point(41.000, 72.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        paths[3] = follower.pathBuilder()
                .addPath(
                        // Line 10
                        new BezierLine(
                                new Point(41.000, 72.000, Point.CARTESIAN),
                                new Point(8.000, 28.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        paths[4] = follower.pathBuilder()
                .addPath(
                        // Line 9
                        new BezierLine(
                                new Point(8.000, 28.000, Point.CARTESIAN),
                                new Point(41.000, 70.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        paths[5] = follower.pathBuilder()
                .addPath(
                        // Line 10
                        new BezierLine(
                                new Point(41.000, 70.000, Point.CARTESIAN),
                                new Point(8.000, 28.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();
    }

    private void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // To chamber
                currentAction = pathAction(paths[0]);
                setPathState(1);
                break;
            case 1: // Shove samples into observation zone
                if (isLastActionCompleted()) {
                    currentAction =
                            new SequentialAction(
                                    scoreSpecimen,
                                    pathAction(paths[1])
                            );
                    setPathState(2);
                }
                break;
            case 2: // Goes to chamber
            case 4:
                if (isLastActionCompleted()) {
                    currentAction =
                            new SequentialAction(
                                    horizontalClaw.close(),
                                    verticalClaw.open(),
                                    new SleepAction(0.25),
                                    horizontalClaw.wristTo(Positions.HorizontalWristPosition.UP),
                                    new ParallelAction(
                                            pathAction(paths[pathState]),
                                            new SequentialAction(
                                                    new SleepAction(1),
                                                    horizontalClaw.wristTo(Positions.HorizontalWristPosition.DOWN),
                                                    new SleepAction(1),
                                                    horizontalClaw.wristTo(Positions.HorizontalWristPosition.UP),
                                                    new SleepAction(0.5),
                                                    verticalClaw.close(),
                                                    new SleepAction(0.5),
                                                    horizontalClaw.open()
                                            )
                                    )
                            );
                    setPathState(pathState + 1);
                }
                break;
            case 3: // Goes back to collect next specimen
            case 5:
                if (isLastActionCompleted()) {
                    currentAction =
                            new SequentialAction(
                                    //scoreSpecimen,
                                    verticalClaw.asynchHingeTo(Positions.VerticalHingePosition.SPECIMEN),
                                    new SleepAction(0.3),
                                    extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_2),
                                    new SleepAction(0.25),
                                    verticalClaw.open(),
                                    extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_1),
                                    verticalClaw.close(),
                                    new ParallelAction(
                                            verticalClaw.asynchHingeTo(Positions.VerticalHingePosition.DOWN),
                                            extension.moveTo(Positions.VerticalExtPosition.BOTTOM),
                                            horizontalClaw.wristTo(Positions.HorizontalWristPosition.DOWN)
                                    ),
                                    pathAction(paths[pathState])
                            );
                    setPathState((pathState != 5) ? pathState + 1 : -1);
                }
                break;
        }
    }

    private Action pathAction(PathChain path) {
        return telemetryPacket -> {
            follower.followPath(path);
            return false;
        };
    }

    private boolean isLastActionCompleted() {
        telemetry.addData("isBusy", follower.isBusy());
        telemetry.addData("currentAction", currentAction);
        telemetry.update();
        return (!follower.isBusy()) && (currentAction == null);
    }

    private void runCurrentAction() {
        if (currentAction == null) return;

        TelemetryPacket p = new TelemetryPacket();
        if (!currentAction.run(p)) {
            // The current action is done. Set it to null
            currentAction = null;
        }
    }
}
