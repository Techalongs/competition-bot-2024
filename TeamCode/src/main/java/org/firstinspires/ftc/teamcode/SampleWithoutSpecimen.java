package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Sample W/O Specimen Auto")
public class SampleWithoutSpecimen extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(11.25, 35.04, Math.toRadians(270)));
        Extension extension = new Extension(hardwareMap);
        Claw claw = new Claw(hardwareMap);

        Actions.runBlocking(new SequentialAction(
                claw.hingeUp(),
                claw.closeClaw()
        ));

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            // Goes to bucket
            TrajectoryActionBuilder traj1 = drive.actionBuilder(new Pose2d(14.58, 62.66, Math.toRadians(270.00)))
                    .strafeTo(new Vector2d(59, 48))
                    .turn(Math.toRadians(-45));

            // Goes to first sample
            TrajectoryActionBuilder traj2 = drive.actionBuilder(new Pose2d(59, 48, Math.toRadians(225)))
                    .turn(Math.toRadians(45))
                    .strafeTo(new Vector2d(47, 36));

            // Goes to bucket again
            TrajectoryActionBuilder traj3 = drive.actionBuilder(new Pose2d(47, 36, Math.toRadians(270.00)))
                    .strafeTo(new Vector2d(59, 48))
                    .turn(Math.toRadians(-45));

            Actions.runBlocking(new SequentialAction(
                    traj1.build(),
                    new SequentialAction(
                            claw.hingeDown(),
                            sleepAction(100)
                    )
            ));

            extension.hingePIDControl(Extension.HingePosition.TOP);
            extension.extensionPIDControl(Extension.ExtensionPosition.BUCKET);

            Actions.runBlocking(
                    new SequentialAction(
                            claw.hingeUp(),
                            sleepAction(250),
                            claw.openClaw(), // Places sample
                            sleepAction(250),
                            claw.hingeDown(),
                            sleepAction(250)
                    )
            );
            extension.hingePIDControl(Extension.HingePosition.HANG_FLEX);
            sleep(250);
            extension.extensionPIDControl(Extension.ExtensionPosition.BOTTOM);
            extension.hingePIDControl(Extension.HingePosition.BOTTOM);

            Actions.runBlocking(new SequentialAction(
                    claw.hingeUp(),
                    traj2.build()
            ));

            extension.extensionPIDControl(Extension.ExtensionPosition.PICKUP);

            Actions.runBlocking(new SequentialAction(
                            claw.openClaw(),
                            sleepAction(250),
                            claw.hingeDown(),
                            sleepAction(1000),
                            claw.closeClaw(),
                            sleepAction(500),
                            claw.hingeUp()
            ));

            extension.extensionPIDControl(Extension.ExtensionPosition.BOTTOM);

            Actions.runBlocking(new SequentialAction(
                    traj3.build(),
                    claw.hingeDown(),
                    sleepAction(100)
            ));

            extension.hingePIDControl(Extension.HingePosition.TOP);
            extension.extensionPIDControl(Extension.ExtensionPosition.BUCKET);

            Actions.runBlocking(
                    new SequentialAction(
                            claw.hingeUp(),
                            sleepAction(250),
                            claw.openClaw(), // Places sample
                            sleepAction(250),
                            claw.hingeDown(),
                            sleepAction(250)
                    )
            );

            extension.hingePIDControl(Extension.HingePosition.HANG_FLEX);
            sleep(250);
            extension.extensionPIDControl(Extension.ExtensionPosition.BOTTOM);
            extension.hingePIDControl(Extension.HingePosition.BOTTOM);
            Actions.runBlocking(claw.hingeUp());
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
