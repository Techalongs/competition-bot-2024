package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Specimen Autonomous")
public class SpecimenAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(-16, 64, Math.toRadians(90)));
        VerticalExtension extension = new VerticalExtension(hardwareMap);
        VerticalClaw verticalClaw = new VerticalClaw(hardwareMap);
        HorizontalClaw horizontalClaw = new HorizontalClaw(hardwareMap);

        Actions.runBlocking(
                new SequentialAction(
                        horizontalClaw.hingeTo(HorizontalClaw.HingePosition.UP),
                        horizontalClaw.open(),
                        horizontalClaw.wristUp(),
                        verticalClaw.close(),
                        verticalClaw.hingeTo(VerticalClaw.HingePosition.SPECIMEN)
                )
        );

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        // Goes to hang specimen
        TrajectoryActionBuilder traj1 = drive.actionBuilder(new Pose2d(-16, 64, Math.toRadians(90)))
                .strafeTo(new Vector2d(-5, 32));

        // Goes to first sample
        TrajectoryActionBuilder traj2 = drive.actionBuilder(new Pose2d(-5, 33, Math.toRadians(90)))
                .strafeTo(new Vector2d(-30, 40))
                .splineToConstantHeading(new Vector2d(-45, 10), Math.toRadians(180));

        // Goes to observation zone
        TrajectoryActionBuilder traj3 = drive.actionBuilder(new Pose2d(-45, 10, Math.toRadians(90)))
                .strafeTo(new Vector2d(-45, 57));

        // Goes to hang specimen
        TrajectoryActionBuilder traj4 = drive.actionBuilder(new Pose2d(-45, 57, Math.toRadians(90)))
                .strafeTo(new Vector2d(-5, 32));

        // Goes to second sample
        TrajectoryActionBuilder traj5 = drive.actionBuilder(new Pose2d(-5, 32, Math.toRadians(90)))
                .strafeTo(new Vector2d(-30, 40))
                .splineToConstantHeading(new Vector2d(-55, 10), Math.toRadians(180));

        // Goes to observation zone
        TrajectoryActionBuilder traj6 = drive.actionBuilder((new Pose2d(-55, 10, Math.toRadians(90))))
                .strafeTo(new Vector2d(-55, 57));

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                traj1.build(),
                                extension.moveTo(VerticalExtension.Position.SPECIMEN_1)
                        ),
                        extension.moveTo(VerticalExtension.Position.SPECIMEN_2),
                        verticalClaw.open(),
                        new ParallelAction(
                                traj2.build(),
                                extension.moveTo(VerticalExtension.Position.BOTTOM),
                                verticalClaw.hingeTo(VerticalClaw.HingePosition.DOWN)
                        ),
                        verticalClaw.open(),
                        horizontalClaw.open(),
                        horizontalClaw.hingeTo(HorizontalClaw.HingePosition.PICKUP),
                        traj3.build(),
                        horizontalClaw.close(),
                        sleepAction(1000),
                        horizontalClaw.hingeTo(HorizontalClaw.HingePosition.UP),
                        verticalClaw.close(),
                        horizontalClaw.open(),
                        new ParallelAction(
                                traj4.build(),
                                verticalClaw.hingeTo(VerticalClaw.HingePosition.SPECIMEN),
                                extension.moveTo(VerticalExtension.Position.SPECIMEN_1)
                        ),
                        extension.moveTo(VerticalExtension.Position.SPECIMEN_2),
                        verticalClaw.open(),
                        new ParallelAction(
                                traj5.build(),
                                extension.moveTo(VerticalExtension.Position.BOTTOM),
                                verticalClaw.hingeTo(VerticalClaw.HingePosition.DOWN)
                        ),
                        verticalClaw.open(),
                        horizontalClaw.open(),
                        horizontalClaw.hingeTo(HorizontalClaw.HingePosition.PICKUP),
                        traj6.build(),
                        horizontalClaw.close()
                )
        );

        sleep(5000);
    }

    public Action sleepAction(int milliseconds) {
        return telemetryPacket -> {
            sleep(milliseconds);
            return false;
        };
    }
}
