package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
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

@Autonomous(name = "Sample W/O Specimen Auto")
public class SampleWithoutSpecimen extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(11.25, 35.04, Math.toRadians(270)));
        VerticalExtension extension = new VerticalExtension(hardwareMap);
        HorizontalClaw horizontalClaw = new HorizontalClaw(hardwareMap);
        VerticalClaw verticalClaw = new VerticalClaw(hardwareMap);

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

            trajs[0] = drive.actionBuilder(new Pose2d(10, 64, Math.toRadians(270)))
                    .strafeTo(new Vector2d(10, 60))
                    .strafeTo(new Vector2d(48, 60))
                    .turn(Math.toRadians(-30));

            trajs[1] = drive.actionBuilder(new Pose2d(48, 60, Math.toRadians(240)))
                    .strafeTo(new Vector2d(38, 47))
                    .turn(Math.toRadians(30))
                    .strafeTo(new Vector2d(38, 13))
                    .turn(Math.toRadians(90))
                    .strafeTo(new Vector2d(0, 13));

            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    trajs[0].build(),
                                    verticalClaw.hingeTo(VerticalClaw.HingePosition.SPECIMEN),
                                    extension.moveTo(VerticalExtension.Position.TOP)
                            ),
                            verticalClaw.hingeTo(VerticalClaw.HingePosition.UP),
                            new SleepAction(0.25),
                            verticalClaw.open(),
                            new SleepAction(0.25),
                            verticalClaw.close(),
                            new SleepAction(0.25),
                            verticalClaw.hingeTo(VerticalClaw.HingePosition.DOWN),
                            new SleepAction(5),
                            new ParallelAction(
                                    trajs[1].build(),
                                    extension.moveTo(VerticalExtension.Position.PARK)
                            ),
                            verticalClaw.hingeTo(VerticalClaw.HingePosition.UP),
                            sleepAction(10000)
                    )
            );
        }
    }

    public Action sleepAction(int milliseconds) {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                sleep(milliseconds);
                return false;
            }
        };
    }
}
