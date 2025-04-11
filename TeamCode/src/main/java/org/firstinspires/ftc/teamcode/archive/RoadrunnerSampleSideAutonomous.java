package org.firstinspires.ftc.teamcode.archive;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HorizontalClaw;
import org.firstinspires.ftc.teamcode.Positions;
import org.firstinspires.ftc.teamcode.VerticalClaw;
import org.firstinspires.ftc.teamcode.VerticalExtension;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

// IN PROGRESS
@Autonomous(name = "Roadrunner Sample Autonomous")
@Disabled
@Deprecated
public class RoadrunnerSampleSideAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(-16, 64, Math.toRadians(90)));
        VerticalExtension extension = new VerticalExtension(hardwareMap);
        VerticalClaw verticalClaw = new VerticalClaw(hardwareMap);
        HorizontalClaw horizontalClaw = new HorizontalClaw(hardwareMap);

        Actions.runBlocking(
                new SequentialAction(
                        horizontalClaw.hingeTo(Positions.HorizontalHingePosition.UP),
                        horizontalClaw.open(),
                        horizontalClaw.wristTo(Positions.HorizontalWristPosition.UP),
                        verticalClaw.close()
                )
        );

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            TrajectoryActionBuilder[] trajs = new TrajectoryActionBuilder[10]; // change size

            trajs[0] = drive.actionBuilder(new Pose2d(10, 64, Math.toRadians(90)))
                    .strafeTo(new Vector2d(1, 64))
                    .strafeTo(new Vector2d(1, 10));

            trajs[1] = drive.actionBuilder(new Pose2d(1, 10, Math.toRadians(90)))
                    .strafeTo(new Vector2d(1, 30))
                    .strafeTo(new Vector2d(39, 30))
                    .turn(Math.toRadians(155))
                    .strafeTo(new Vector2d(39, 15));

            trajs[2] = drive.actionBuilder(new Pose2d(39, 15, Math.toRadians(270)))
                    .strafeTo(new Vector2d(55, 15))
                    .turn(Math.toRadians(-25))
                    .strafeTo(new Vector2d(53, 15));

            trajs[3] = drive.actionBuilder(new Pose2d(53, 15, Math.toRadians(245)))
                    .strafeTo(new Vector2d(53, -30))
                    .turn(Math.toRadians(25))
                    .strafeTo(new Vector2d(53, 0))
                    .strafeTo(new Vector2d(0, 0));

            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    trajs[0].build(),
                                    verticalClaw.hingeTo(Positions.VerticalHingePosition.SPECIMEN),
                                    extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_1)
                            ),
                            new SleepAction(1),
                            extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_2),
                            verticalClaw.open(),
                            extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_1),
                            verticalClaw.close(),
                            new ParallelAction(
                                    trajs[1].build(),
                                    extension.moveTo(Positions.VerticalExtPosition.BOTTOM),
                                    horizontalClaw.wristTo(Positions.HorizontalWristPosition.MID)
                            ),
                            horizontalClaw.hingeTo(Positions.HorizontalHingePosition.DOWN),
                            new SleepAction(0.25),
                            horizontalClaw.close(),
                            new SleepAction(0.25),
                            horizontalClaw.hingeTo(Positions.HorizontalHingePosition.UP),
                            new ParallelAction(
                                    trajs[2].build(),
                                    new SequentialAction(
                                            horizontalClaw.wristTo(Positions.HorizontalWristPosition.DOWN),
                                            new SleepAction(0.75),
                                            horizontalClaw.wristTo(Positions.HorizontalWristPosition.UP),
                                            new SleepAction(0.75),
                                            verticalClaw.close(),
                                            new SleepAction(0.75),
                                            horizontalClaw.open(),
                                            new SleepAction(0.25),
                                            verticalClaw.hingeTo(Positions.VerticalHingePosition.SPECIMEN),
                                            extension.moveTo(Positions.VerticalExtPosition.TOP),
                                            horizontalClaw.wristTo(Positions.HorizontalWristPosition.UP),
                                            horizontalClaw.open()
                                    )
                            ),
                            verticalClaw.hingeTo(Positions.VerticalHingePosition.UP),
                            new SleepAction(0.25),
                            verticalClaw.open(),
                            new SleepAction(0.25),
                            verticalClaw.close(),
                            new SleepAction(0.25),
                            verticalClaw.hingeTo(Positions.VerticalHingePosition.DOWN),
                            new SleepAction(5),
//                            new ParallelAction(
//                                    trajs[3].build(),
//                                    extension.moveTo(VerticalExtension.Position.BOTTOM)
//                            ),
                            sleepAction(10000)
                    )
            );
        }
    }

    private Action sleepAction(int milliseconds) {
        return telemetryPacket -> {
            sleep(milliseconds);
            return false;
        };
    }
}
