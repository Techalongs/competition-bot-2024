package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

// MUST TEST - New Bot
@Autonomous(name = "Parking Specimen Autonomous")
public class SimpleSpecimen extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(11.25, 35.04, Math.toRadians(270)));
        Extension extension = new Extension(hardwareMap);
        VerticalClaw claw = new VerticalClaw(hardwareMap);

        Actions.runBlocking(new SequentialAction(
                claw.hingeTo(VerticalClaw.HingePosition.UP),
                claw.close()
        ));

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        TrajectoryActionBuilder toPark = drive.actionBuilder(new Pose2d(-16, 35.04, Math.toRadians(270)))
                .strafeTo(new Vector2d(-40, 35));

        Actions.runBlocking(toPark.build());
    }
}
