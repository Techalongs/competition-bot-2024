package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
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
@Autonomous(name = "Simple Async Test")
public class SimpleAsynchTest extends OpMode {
    private VerticalClaw verticalClaw;

    private Follower follower;
    private Action currentAction;
    private Timer pathTimer, opmodeTimer;
    private PathChain[] paths;
    private int pathState;

    private Action toggleVerticalClaw;

    @Override
    public void init() {
        verticalClaw = new VerticalClaw(hardwareMap);

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
        toggleVerticalClaw = new SequentialAction(
                verticalClaw.open(),
                new SleepAction(1.0),
                verticalClaw.close(),
                new SleepAction(1.0),
                verticalClaw.open(),
                new SleepAction(1.0),
                verticalClaw.close()
        );
    }

    private void buildPaths() {
        paths = new PathChain[1];

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
    }

    private void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // To chamber
                currentAction = new ParallelAction(
                            pathAction(paths[0]),
                            toggleVerticalClaw);
                setPathState(1);
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
