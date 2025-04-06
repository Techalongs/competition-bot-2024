package org.firstinspires.ftc.teamcode.deprecated;

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
import org.firstinspires.ftc.teamcode.VerticalClaw;
import org.firstinspires.ftc.teamcode.VerticalExtension;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Roadrunner Specimen Autonomous")
@Deprecated
@Disabled
public class RoadrunnerSpecimenAutonomous extends LinearOpMode {
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

            // To Chamber
            trajs[0] = drive.actionBuilder(new Pose2d(-16, 64, Math.toRadians(90)))
                    .strafeTo(new Vector2d(1, 64))
                    .strafeTo(new Vector2d(1, 10));

            // To Observation Zone
            trajs[1] = drive.actionBuilder(new Pose2d(1, 10, Math.toRadians(90)))
                    .strafeTo(new Vector2d(1, 40))
                    .strafeTo(new Vector2d(-55, 40))
                    .turn(Math.toRadians(-5))
                    .strafeTo(new Vector2d(-55, 75));

            // Back up
            trajs[2] = drive.actionBuilder(new Pose2d(-55, 75, Math.toRadians(90)))
                    .strafeTo(new Vector2d(-55, 55));

            // To Chamber
            trajs[3] = drive.actionBuilder(new Pose2d(-55, 55, Math.toRadians(90)))
                    .strafeTo(new Vector2d(1, 55))
                    .strafeTo(new Vector2d(1, 10));

            // Scoop One Sample
            trajs[4] = drive.actionBuilder(new Pose2d(1, 10, Math.toRadians(90)))
                    .strafeTo(new Vector2d(1, 30))
                    .strafeTo(new Vector2d(-38, 30))
                    .strafeTo(new Vector2d(-38, -10))
                    .strafeTo(new Vector2d(-53, -10))
                    .strafeTo(new Vector2d(-53, 60));

            // trajs[2] & trajs[3]
            trajs[5] = drive.actionBuilder(new Pose2d(-55, 75, Math.toRadians(90)))
                    .strafeTo(new Vector2d(-55, 55))
                    .strafeTo(new Vector2d(1, 55))
                    .strafeTo(new Vector2d(1, 10));

            // Final travel to Observation Zone
            trajs[6] = drive.actionBuilder(new Pose2d(1, 10, Math.toRadians(90)))
                    .strafeTo(new Vector2d(1, 40))
                    .strafeTo(new Vector2d(-55, 75));

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
                            ), // Comment to here
                            new SleepAction(0.5),
                            extension.moveTo(VerticalExtension.Position.SPECIMEN_2),
                            verticalClaw.open(),
                            extension.moveTo(VerticalExtension.Position.SPECIMEN_1),
                            verticalClaw.close(),
                            new ParallelAction(
                                    trajs[6].build(),
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
