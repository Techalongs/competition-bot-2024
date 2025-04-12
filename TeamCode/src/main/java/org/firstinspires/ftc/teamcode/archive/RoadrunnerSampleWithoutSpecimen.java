package org.firstinspires.ftc.teamcode.archive;

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

import org.firstinspires.ftc.teamcode.HorizontalClaw;
import org.firstinspires.ftc.teamcode.Positions;
import org.firstinspires.ftc.teamcode.VerticalClaw;
import org.firstinspires.ftc.teamcode.VerticalExtension;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Roadrunner Sample W/O Specimen Auto")
@Disabled
@Deprecated
public class RoadrunnerSampleWithoutSpecimen extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(10, 64, Math.toRadians(270)));
        VerticalExtension extension = new VerticalExtension(hardwareMap);
        HorizontalClaw horizontalClaw = new HorizontalClaw(hardwareMap);
        VerticalClaw verticalClaw = new VerticalClaw(hardwareMap);

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

            trajs[0] = drive.actionBuilder(new Pose2d(10, 64, Math.toRadians(270)))
                    .strafeTo(new Vector2d(10, 50))
                    .strafeTo(new Vector2d(48, 55))
                    .turn(Math.toRadians(-30));

            trajs[1] = drive.actionBuilder(new Pose2d(48, 55, Math.toRadians(240)))
                    .strafeTo(new Vector2d(48, 37))
                    .turn(Math.toRadians(30));
//                    .strafeTo(new Vector2d(38, 13))
//                    .turn(Math.toRadians(90))
//                    .strafeTo(new Vector2d(0, 13));

            telemetry.addData("Before", drive.pose);
            telemetry.update();

            sleep(2000);

            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    trajs[0].build(),
                                    verticalClaw.hingeTo(Positions.VerticalHingePosition.SPECIMEN),
                                    extension.moveTo(Positions.VerticalExtPosition.TOP)
                            ),
                            verticalClaw.hingeTo(Positions.VerticalHingePosition.UP),
                            new SleepAction(0.25),
                            verticalClaw.open(),
                            new SleepAction(0.75),
                            verticalClaw.close(),
                            new SleepAction(0.25),
                            verticalClaw.hingeTo(Positions.VerticalHingePosition.DOWN),
                            new SleepAction(0.25),
                            new ParallelAction(
                                    trajs[1].build(),
                                    extension.moveTo(Positions.VerticalExtPosition.BOTTOM)
                                    // horizontalClaw.hingeTo(HorizontalClaw.HingePosition.DOWN)
                            ),
                            horizontalClaw.wristTo(Positions.HorizontalWristPosition.MID),
                            new SleepAction(0.25),
                            horizontalClaw.close(),
                            new ParallelAction(
                                    trajs[1].build(),
                                    extension.moveTo(Positions.VerticalExtPosition.PARK)
                            ),
                            verticalClaw.hingeTo(Positions.VerticalHingePosition.UP),
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
