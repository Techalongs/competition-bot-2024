package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Specimen Autonomous")
public class SpecimenAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(-16, 64, Math.toRadians(90)));
        VerticalExtension extension = new VerticalExtension(hardwareMap);
        VerticalClaw verticalClaw = new VerticalClaw(hardwareMap);
        HorizontalClaw horizontalClaw = new HorizontalClaw(hardwareMap);

        Actions.runBlocking(
                new SequentialAction(
                        horizontalClaw.hingeTo(HorizontalClaw.HingePosition.UP),
                        horizontalClaw.open(),
                        horizontalClaw.wristTo(HorizontalClaw.WristPosition.UP),
                        verticalClaw.close()
                )
        );

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            TrajectoryActionBuilder[] trajs = new TrajectoryActionBuilder[10]; // change size

            trajs[0] = drive.actionBuilder(new Pose2d(-16, 64, Math.toRadians(90)))
                    .strafeTo(new Vector2d(1, 64))
                    .strafeTo(new Vector2d(1, 10));

            trajs[1] = drive.actionBuilder(new Pose2d(1, 10, Math.toRadians(90)))
                    .strafeTo(new Vector2d(1, 30))
                    .strafeTo(new Vector2d(-55, 30))
                    .strafeTo(new Vector2d(-55, 64));

            trajs[2] = drive.actionBuilder(new Pose2d(-55, 64, Math.toRadians(90)))
                    .strafeTo(new Vector2d(-55, 55));

            trajs[3] = drive.actionBuilder(new Pose2d(-55, 55, Math.toRadians(90)))
                    .strafeTo(new Vector2d(1, 55))
                    .strafeTo(new Vector2d(1, 10));

            trajs[4] = drive.actionBuilder(new Pose2d(1, 10, Math.toRadians(90)))
                    .strafeTo(new Vector2d(1, 30))
                    .strafeTo(new Vector2d(-38, 30))
                    .strafeTo(new Vector2d(-38, -10))
                    .strafeTo(new Vector2d(-53, -10))
                    .strafeTo(new Vector2d(-53, 60));

            trajs[5] = drive.actionBuilder(new Pose2d(-55, 64, Math.toRadians(90)))
                    .strafeTo(new Vector2d(-55, 55))
                    .strafeTo(new Vector2d(1, 55))
                    .strafeTo(new Vector2d(1, 10));

            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    trajs[0].build(),
                                    verticalClaw.hingeTo(VerticalClaw.HingePosition.SPECIMEN),
                                    extension.moveTo(VerticalExtension.Position.SPECIMEN_1)
                            ),
                            new SleepAction(1),
                            extension.moveTo(VerticalExtension.Position.SPECIMEN_2),
                            verticalClaw.open(),
                            extension.moveTo(VerticalExtension.Position.SPECIMEN_1),
                            verticalClaw.close(),
                            new ParallelAction(
                                    trajs[1].build(),
                                    verticalClaw.hingeTo(VerticalClaw.HingePosition.DOWN),
                                    extension.moveTo(VerticalExtension.Position.BOTTOM),
                                    horizontalClaw.wristTo(HorizontalClaw.WristPosition.DOWN)
                            ),
                            horizontalClaw.open(),
                            new SleepAction(0.25),
                            verticalClaw.open(),
                            new SleepAction(0.5),
                            horizontalClaw.close(),
                            new SleepAction(0.5),
                            horizontalClaw.wristTo(HorizontalClaw.WristPosition.UP),
//                            new ParallelAction(
//                                    trajs[5].build(), // trajs[2] & trajs[3]
//                                    new SequentialAction(
//                                            horizontalClaw.wristTo(HorizontalClaw.WristPosition.DOWN),
//                                            new SleepAction(0.75),
//                                            horizontalClaw.wristTo(HorizontalClaw.WristPosition.UP),
//                                            new SleepAction(0.75),
//                                            verticalClaw.close(),
//                                            new SleepAction(0.75),
//                                            horizontalClaw.open(),
//                                            new ParallelAction(
//                                                    verticalClaw.hingeTo(VerticalClaw.HingePosition.SPECIMEN),
//                                                    extension.moveTo(VerticalExtension.Position.SPECIMEN_1)
//                                            )
//                                    )
//                            ),
                            trajs[2].build(),
                            horizontalClaw.wristTo(HorizontalClaw.WristPosition.DOWN),
                            new SleepAction(0.75),
                            horizontalClaw.wristTo(HorizontalClaw.WristPosition.UP),
                            new SleepAction(0.75),
                            verticalClaw.close(),
                            new SleepAction(0.75),
                            horizontalClaw.open(),
                            new SleepAction(0.25),
                            new ParallelAction(
                                    trajs[3].build(),
                                    verticalClaw.hingeTo(VerticalClaw.HingePosition.SPECIMEN),
                                    extension.moveTo(VerticalExtension.Position.SPECIMEN_1)
                            ),
                            new SleepAction(0.5),
                            extension.moveTo(VerticalExtension.Position.SPECIMEN_2),
                            verticalClaw.open(),
                            extension.moveTo(VerticalExtension.Position.SPECIMEN_1),
                            verticalClaw.close(),
                            new ParallelAction(
                                    trajs[1].build(),
                                    verticalClaw.hingeTo(VerticalClaw.HingePosition.DOWN),
                                    extension.moveTo(VerticalExtension.Position.BOTTOM),
                                    horizontalClaw.wristTo(HorizontalClaw.WristPosition.DOWN),
                                    horizontalClaw.open()
                            )
                    )
            );

            sleep(10000);
        }
    }
}
