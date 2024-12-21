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
        Hooks hooks = new Hooks(hardwareMap);
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
            TrajectoryActionBuilder toBucket1 = drive.actionBuilder(new Pose2d(14.58, 62.66, Math.toRadians(270.00)))
                    .strafeTo(new Vector2d(58, 38))
                    .turn(Math.toRadians(-45));

            // Backs away from bucket
            TrajectoryActionBuilder backFromBucket = drive.actionBuilder(new Pose2d(58, 38, Math.toRadians(225)))
                    .turn(Math.toRadians(45));

            // Goes to first sample
            TrajectoryActionBuilder toFirstSample = drive.actionBuilder(new Pose2d(56, 37, Math.toRadians(270)))
                    .strafeTo(new Vector2d(43, 31));

            // Goes to bucket again
            TrajectoryActionBuilder toBucket2 = drive.actionBuilder(new Pose2d(43, 31, Math.toRadians(270.00)))
                    .turn(Math.toRadians(-45))
                    .strafeTo(new Vector2d(50, 30));

            TrajectoryActionBuilder toPark = drive.actionBuilder(new Pose2d(58, 38, Math.toRadians(270)))
                    .splineTo(new Vector2d(20, -20), Math.toRadians(180))
                    .turn(Math.toRadians(180));

            Action placeInBucket = new SequentialAction(
                    claw.hingeDown(),
                    extension.hingePIDControl(Extension.HingePosition.TOP),
                    extension.extensionPIDControl(Extension.ExtensionPosition.BUCKET),
                    claw.hingeUp(),
                    sleepAction(100),
                    claw.openClaw(),
                    sleepAction(500),
                    claw.hingeDown()
            );

            Action pickUpSample = new SequentialAction(
                    claw.hingeUp(),
                    extension.hingePIDControl(Extension.HingePosition.BOTTOM),
                    extension.extensionPIDControl(Extension.ExtensionPosition.PICKUP),
                    claw.openClaw(),
                    sleepAction(250),
                    claw.hingeDown(),
                    sleepAction(250),
                    claw.closeClaw(),
                    sleepAction(500),
                    claw.hingeUp()
            );

            Action returnToReady = new SequentialAction(
                    claw.hingeUp(),
                    extension.extensionPIDControl(Extension.ExtensionPosition.BOTTOM),
                    extension.hingePIDControl(Extension.HingePosition.BOTTOM)
            );

            Actions.runBlocking(
                    new SequentialAction(
                            toBucket1.build(),
                            placeInBucket,
                            backFromBucket.build(),
                            returnToReady,
                            toPark.build(),
                            drive.actionBuilder(new Pose2d(-20, 20, Math.toRadians(0)))
                                    .lineToX(-30)
                                    .build(),
                            hooks.hooksPIDControl(Hooks.HookPosition.HOOK)
//                            toFirstSample.build(),
//                            pickUpSample,
//                            returnToReady,
//                            toBucket2.build(),
//                            placeInBucket,
//                            returnToReady
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
