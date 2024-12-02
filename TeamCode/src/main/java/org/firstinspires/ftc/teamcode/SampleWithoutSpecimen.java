package org.firstinspires.ftc.teamcode;

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
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(11.25, 35.04, Math.toRadians(270)));
            Extension extension = new Extension(hardwareMap);
            Claw claw = new Claw(hardwareMap);

            TrajectoryActionBuilder traj1 = drive.actionBuilder(new Pose2d(14.58, 62.66, Math.toRadians(270.00)))
                    .strafeTo(new Vector2d(58, 51))
                    .turn(Math.toRadians(-45));

            Actions.runBlocking(new SequentialAction(
                    claw.hingeUp(),
                    claw.closeClaw(),
                    traj1.build(),
                    claw.hingeDown()
            ));

            extension.hingePIDControl(Extension.HingePosition.TOP);
            extension.extensionPIDControl(Extension.ExtensionPosition.BUCKET);

            Actions.runBlocking(claw.hingeUp());
            sleep(250);
            Actions.runBlocking(claw.openClaw());
            sleep(250);
            Actions.runBlocking(claw.hingeDown());
            sleep(250);

            extension.hingePIDControl(Extension.HingePosition.HANG_FLEX);
            extension.extensionPIDControl(Extension.ExtensionPosition.BOTTOM);
            extension.hingePIDControl(Extension.HingePosition.BOTTOM);
            Actions.runBlocking(claw.hingeUp());
        }
    }
}
