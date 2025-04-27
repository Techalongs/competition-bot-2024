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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;

@SuppressWarnings("unused")
@Autonomous(name = "Full Specimen Auto")
public class FullSpecimenAuto extends LinearOpMode {
    private VerticalExtension extension;
    private VerticalClaw verticalClaw;
    private HorizontalClaw horizontalClaw;

    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private PathChain[] paths;
    private Action currentAction;

    private Action scoreSpecimen;

    private Action pickupSpecimen;

    private Action handoff;

    @Override
    public void runOpMode() throws InterruptedException {
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

        setAction();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            runCurrentAction();
            follower.update();

            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("heading", follower.getPose().getHeading());
            telemetry.update();
        }
    }

    private void initActions() {
        scoreSpecimen =
                new SequentialAction(
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
        paths = new PathChain[4];

        /* Note: Line _ corresponds to lines generated on https://pedro-path-generator.vercel.app/
         * File for this Auto is /Downloads/SpecimenAutoTraj2025.txt
         */

        paths[0] = follower.pathBuilder()
                .addPath(
                        // Line 1
                        new BezierLine(
                                new Point(9.000, 57.000, Point.CARTESIAN),
                                new Point(41.000, 72.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        paths[1] = follower.pathBuilder()
                .addPath(
                        // Line 2
                        new BezierCurve(
                                new Point(41.000, 72.000, Point.CARTESIAN),
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
                        // Line 4
                        new BezierCurve(
                                new Point(30.000, 26.000, Point.CARTESIAN),
                                new Point(68.000, 30.000, Point.CARTESIAN),
                                new Point(58.000, 15.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 5
                        new BezierLine(
                                new Point(58.000, 15.000, Point.CARTESIAN),
                                new Point(30.000, 15.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .addPath(
                        // Line 8
                        new BezierCurve(
                                new Point(30.000, 15.000, Point.CARTESIAN),
                                new Point(45.000, 17.000, Point.CARTESIAN),
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
                                new Point(41.000, 70.000, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        paths[3] = follower.pathBuilder()
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

    private void setAction() {
        currentAction =
                new SequentialAction(
                        pathAction(paths[0]),
                 //       scoreSpecimen,
                        pathAction(paths[1]),
                 //       pickupSpecimen,
                        new ParallelAction(
                                pathAction(paths[2])
                 //               handoff
                        ),
                        //scoreSpecimen,
                        pathAction(paths[3]),
                        //pickupSpecimen,
                        new ParallelAction(
                                pathAction(paths[2]),
                                handoff
                        ),
                        scoreSpecimen,
                        pathAction(paths[3]),
                        pickupSpecimen,
                        new ParallelAction(
                                pathAction(paths[2]),
                                handoff
                        ),
                        scoreSpecimen,
                        pathAction(paths[3])
                );
    }

    private Action pathAction(PathChain path) {
        return telemetryPacket -> {
            follower.followPath(path);
            follower.update();
            return false;
            // return follower.isBusy();
        };
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


//package org.firstinspires.ftc.teamcode;
//
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.ParallelAction;
//import com.acmerobotics.roadrunner.SequentialAction;
//import com.acmerobotics.roadrunner.SleepAction;
//import com.acmerobotics.roadrunner.ftc.Actions;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.localization.Pose;
//import com.pedropathing.pathgen.BezierCurve;
//import com.pedropathing.pathgen.BezierLine;
//import com.pedropathing.pathgen.PathChain;
//import com.pedropathing.pathgen.Point;
//import com.pedropathing.util.Constants;
//import com.pedropathing.util.Timer;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
//import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
//
//@SuppressWarnings("unused")
//@Autonomous(name = "Full Specimen Auto")
//public class FullSpecimenAuto extends OpMode {
//    private VerticalExtension extension;
//    private VerticalClaw verticalClaw;
//    private HorizontalClaw horizontalClaw;
//
//    private Follower follower;
//    private Timer pathTimer, opmodeTimer;
//    private PathChain[] paths;
//    private Action currentAction;
//
//    private Action scoreSpecimen;
//
//    private Action pickupSpecimen;
//
//    private Action handoff;
//
//    @Override
//    public void init() {
//        extension = new VerticalExtension(hardwareMap);
//        verticalClaw = new VerticalClaw(hardwareMap);
//        horizontalClaw = new HorizontalClaw(hardwareMap);
//
//        pathTimer = new Timer();
//        opmodeTimer = new Timer();
//        opmodeTimer.resetTimer();
//
//        initActions();
//
//        Constants.setConstants(FConstants.class, LConstants.class);
//        follower = new Follower(hardwareMap);
//        follower.setStartingPose(new Pose(9, 57, Math.toRadians(0), true));
//        buildPaths();
//
//        Actions.runBlocking(
//                new SequentialAction(
//                        horizontalClaw.hingeTo(Positions.HorizontalHingePosition.UP),
//                        horizontalClaw.open(),
//                        horizontalClaw.wristTo(Positions.HorizontalWristPosition.UP),
//                        verticalClaw.close()
//                )
//        );
//    }
//
//    @Override
//    public void start() {
//        opmodeTimer.resetTimer();
//    }
//
//    @Override
//    public void loop() {
//        follower.update();
//
//        setAction();
//        runCurrentAction();
//
//        telemetry.addData("x", follower.getPose().getX());
//        telemetry.addData("y", follower.getPose().getY());
//        telemetry.addData("heading", follower.getPose().getHeading());
//        telemetry.update();
//
//        return;
//    }
//
//    private void initActions() {
//        scoreSpecimen =
//                new SequentialAction(
//                        verticalClaw.asynchHingeTo(Positions.VerticalHingePosition.SPECIMEN),
//                        new SleepAction(0.3),
//                        extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_2),
//                        new SleepAction(0.25),
//                        verticalClaw.open(),
//                        extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_1),
//                        verticalClaw.close(),
//                        new ParallelAction(
//                                verticalClaw.asynchHingeTo(Positions.VerticalHingePosition.DOWN),
//                                extension.moveTo(Positions.VerticalExtPosition.BOTTOM),
//                                horizontalClaw.wristTo(Positions.HorizontalWristPosition.DOWN)
//                        )
//                );
//
//        pickupSpecimen =
//                new SequentialAction(
//                        horizontalClaw.close(),
//                        verticalClaw.open(),
//                        new SleepAction(0.25),
//                        horizontalClaw.wristTo(Positions.HorizontalWristPosition.UP)
//                );
//
//        handoff =
//                new SequentialAction(
//                        new SleepAction(1),
//                        horizontalClaw.wristTo(Positions.HorizontalWristPosition.DOWN),
//                        new SleepAction(1),
//                        horizontalClaw.wristTo(Positions.HorizontalWristPosition.UP),
//                        new SleepAction(0.5),
//                        verticalClaw.close(),
//                        new SleepAction(0.5),
//                        horizontalClaw.open()
//                );
//    }
//
//    private void buildPaths() {
//        paths = new PathChain[4];
//
//        /* Note: Line _ corresponds to lines generated on https://pedro-path-generator.vercel.app/
//         * File for this Auto is /Downloads/SpecimenAutoTraj2025.txt
//         */
//
//        paths[0] = follower.pathBuilder()
//                .addPath(
//                        // Line 1
//                        new BezierLine(
//                                new Point(9.000, 57.000, Point.CARTESIAN),
//                                new Point(41.000, 72.000, Point.CARTESIAN)
//                        )
//                )
//                .setConstantHeadingInterpolation(Math.toRadians(0))
//                .build();
//
//        paths[1] = follower.pathBuilder()
//                .addPath(
//                        // Line 2
//                        new BezierCurve(
//                                new Point(41.000, 72.000, Point.CARTESIAN),
//                                new Point(1.000, 23.000, Point.CARTESIAN),
//                                new Point(80.000, 42.000, Point.CARTESIAN),
//                                new Point(58.000, 26.000, Point.CARTESIAN)
//                        )
//                )
//                .setConstantHeadingInterpolation(Math.toRadians(0))
//                .addPath(
//                        // Line 3
//                        new BezierLine(
//                                new Point(58.000, 26.000, Point.CARTESIAN),
//                                new Point(30.000, 26.000, Point.CARTESIAN)
//                        )
//                )
//                .setConstantHeadingInterpolation(Math.toRadians(0))
//                .addPath(
//                        // Line 4
//                        new BezierCurve(
//                                new Point(30.000, 26.000, Point.CARTESIAN),
//                                new Point(68.000, 30.000, Point.CARTESIAN),
//                                new Point(58.000, 15.000, Point.CARTESIAN)
//                        )
//                )
//                .setConstantHeadingInterpolation(Math.toRadians(0))
//                .addPath(
//                        // Line 5
//                        new BezierLine(
//                                new Point(58.000, 15.000, Point.CARTESIAN),
//                                new Point(30.000, 15.000, Point.CARTESIAN)
//                        )
//                )
//                .setConstantHeadingInterpolation(Math.toRadians(0))
//                .addPath(
//                        // Line 8
//                        new BezierCurve(
//                                new Point(30.000, 15.000, Point.CARTESIAN),
//                                new Point(45.000, 17.000, Point.CARTESIAN),
//                                new Point(8.000, 28.000, Point.CARTESIAN)
//                        )
//                )
//                .setConstantHeadingInterpolation(Math.toRadians(0))
//                .build();
//
//        paths[2] = follower.pathBuilder()
//                .addPath(
//                        // Line 9
//                        new BezierLine(
//                                new Point(8.000, 28.000, Point.CARTESIAN),
//                                new Point(41.000, 70.000, Point.CARTESIAN)
//                        )
//                )
//                .setConstantHeadingInterpolation(Math.toRadians(0))
//                .build();
//
//        paths[3] = follower.pathBuilder()
//                .addPath(
//                        // Line 10
//                        new BezierLine(
//                                new Point(41.000, 70.000, Point.CARTESIAN),
//                                new Point(8.000, 28.000, Point.CARTESIAN)
//                        )
//                )
//                .setConstantHeadingInterpolation(Math.toRadians(0))
//                .build();
//    }
//
//    private void setAction() {
//        currentAction =
//                new SequentialAction(
//                        verticalClaw.open(),
//                        pathAction(paths[0])
//                );
////                new SequentialAction(
////                        pathAction(paths[0]),
////                        scoreSpecimen,
////                        pathAction(paths[1]),
////                        pickupSpecimen,
////                        new ParallelAction(
////                                pathAction(paths[2]),
////                                handoff
////                        ),
////                        scoreSpecimen,
////                        pathAction(paths[3]),
////                        pickupSpecimen,
////                        new ParallelAction(
////                                pathAction(paths[2]),
////                                handoff
////                        ),
////                        scoreSpecimen,
////                        pathAction(paths[3]),
////                        pickupSpecimen,
////                        new ParallelAction(
////                                pathAction(paths[2]),
////                                handoff
////                        ),
////                        scoreSpecimen,
////                        pathAction(paths[3])
////                );
//    }
//
//    private Action pathAction(PathChain path) {
//        return telemetryPacket -> {
//            follower.followPath(path);
//            return false;
//        };
//    }
//
//    private void runCurrentAction() {
//        if (currentAction == null) return;
//
//        TelemetryPacket p = new TelemetryPacket();
//        if (!currentAction.run(p)) {
//            // The current action is done. Set it to null
//            currentAction = null;
//        }
//    }
//}
