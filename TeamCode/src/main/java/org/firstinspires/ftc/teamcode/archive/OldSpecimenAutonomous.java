package org.firstinspires.ftc.teamcode.archive;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HorizontalClaw;
import org.firstinspires.ftc.teamcode.Positions;
import org.firstinspires.ftc.teamcode.VerticalClaw;
import org.firstinspires.ftc.teamcode.VerticalExtension;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Old Specimen Autonomous")
@Deprecated
public class OldSpecimenAutonomous extends LinearOpMode {
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
                        verticalClaw.close(),
                        sleepAction(1000),
                        verticalClaw.hingeTo(Positions.VerticalHingePosition.SPECIMEN)
                )
        );

        // drive.setDrivePowers(new PoseVelocity2d());

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        TrajectoryActionBuilder[] trajs = new TrajectoryActionBuilder[13];

        // Goes to hang specimen
        trajs[0] = drive.actionBuilder(new Pose2d(-16, 64, Math.toRadians(90)))
                .strafeTo(new Vector2d(3, 32));

        // Goes to first sample
        trajs[1] = drive.actionBuilder(new Pose2d(2, 32, Math.toRadians(90)))
                .strafeTo(new Vector2d(-30, 43))
                .splineToConstantHeading(new Vector2d(-45, 10), Math.toRadians(180));

        // Goes to observation zone
        trajs[2] = drive.actionBuilder(new Pose2d(-45, 10, Math.toRadians(90)))
                .strafeTo(new Vector2d(-45, 56.5));

        // Goes to hang specimen
        trajs[3] = drive.actionBuilder(new Pose2d(-45, 56.5, Math.toRadians(90)))
                .strafeTo(new Vector2d(2, 32));

        // Goes to second sample
        trajs[4] = drive.actionBuilder(new Pose2d(1, 32, Math.toRadians(90)))
                .strafeTo(new Vector2d(-30, 43))
                .splineToConstantHeading(new Vector2d(-55, 10), Math.toRadians(180));

        // Goes to observation zone
        trajs[5] = drive.actionBuilder(new Pose2d(-55, 10, Math.toRadians(90)))
                .strafeTo(new Vector2d(-55, 56.5));

        // Goes to hang specimen
        trajs[6] = drive.actionBuilder(new Pose2d(-55, 56.5, Math.toRadians(90)))
                .strafeTo(new Vector2d(1, 32));

        // Goes to third sample
        trajs[7] = drive.actionBuilder(new Pose2d(0, 32, Math.toRadians(90)))
                .strafeTo(new Vector2d(-30, 43))
                .splineToConstantHeading(new Vector2d(-60, 10), Math.toRadians(180));

        // Goes to observation zone
        trajs[8] = drive.actionBuilder(new Pose2d(-60, 10, Math.toRadians(90)))
                .strafeTo(new Vector2d(-61, 56.5));

        // Goes to hang specimen
        trajs[9] = drive.actionBuilder(new Pose2d(-61, 56.5, Math.toRadians(90)))
                .strafeTo(new Vector2d(1, 32));

        // Goes to observation zone
        trajs[10] = drive.actionBuilder(new Pose2d(0, 32, Math.toRadians(90)))
                .strafeTo(new Vector2d(-45, 56.5));

        // Goes to hang specimen
        trajs[11] = drive.actionBuilder(new Pose2d(-45, 56.5, Math.toRadians(90)))
                .strafeTo(new Vector2d(0, 32));

        // Goes to park
        trajs[12] = drive.actionBuilder(new Pose2d(-1, 32, Math.toRadians(90)))
                .strafeTo(new Vector2d(-45, 55));

        // Trajectories 0 - 8
        for (int i = 0; i < 3; i++) {
            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    trajs[i * 3].build(),
                                    verticalClaw.hingeTo(Positions.VerticalHingePosition.SPECIMEN),
                                    extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_1)
                            ),
                            sleepAction(250),
                            extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_2),
                            verticalClaw.open(),
                            extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_1),
                            verticalClaw.close(),
                            new ParallelAction(
                                    trajs[i * 3 + 1].build(),
                                    extension.moveTo(Positions.VerticalExtPosition.BOTTOM),
                                    verticalClaw.hingeTo(Positions.VerticalHingePosition.DOWN)
                            ),
                            sleepAction(500),
                            verticalClaw.open(),
                            horizontalClaw.open(),
                            horizontalClaw.hingeTo(Positions.HorizontalHingePosition.MID),
                            trajs[i * 3 + 2].build(),
                            horizontalClaw.close(),
                            sleepAction(500),
                            horizontalClaw.hingeTo(Positions.HorizontalHingePosition.UP),
                            verticalClaw.close(),
                            sleepAction(500),
                            horizontalClaw.open()
                    )
            );
        }

        // Trajectories 9 - 12
        for (int i = 0; i < 4; i += 2) {
            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    trajs[9 + i].build(),
                                    verticalClaw.hingeTo(Positions.VerticalHingePosition.SPECIMEN),
                                    extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_1)
                            ),
                            extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_2),
                            verticalClaw.open(),
                            extension.moveTo(Positions.VerticalExtPosition.SPECIMEN_1),
                            verticalClaw.close(),
                            new ParallelAction(
                                    trajs[9 + i + 1].build(),
                                    extension.moveTo(Positions.VerticalExtPosition.BOTTOM),
                                    verticalClaw.hingeTo(Positions.VerticalHingePosition.DOWN)
                            )
                    )
            );

            if (i == 0) {
                Actions.runBlocking(
                        new SequentialAction(
                                sleepAction(500),
                                verticalClaw.open(),
                                horizontalClaw.open(),
                                sleepAction(250),
                                horizontalClaw.hingeTo(Positions.HorizontalHingePosition.MID),
                                sleepAction(250),
                                horizontalClaw.close(),
                                sleepAction(250),
                                horizontalClaw.hingeTo(Positions.HorizontalHingePosition.UP),
                                verticalClaw.close(),
                                sleepAction(500),
                                horizontalClaw.open()
                        )
                );
            }
        }
    }

    public Action sleepAction(int milliseconds) {
        return telemetryPacket -> {
            sleep(milliseconds);
            return false;
        };
    }
}
