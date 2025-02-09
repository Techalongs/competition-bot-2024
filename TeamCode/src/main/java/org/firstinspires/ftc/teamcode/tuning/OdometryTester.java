package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HorizontalClaw;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.VerticalClaw;

@Autonomous (name = "Odometry Tester")
public class OdometryTester extends LinearOpMode {

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(-16, 64, Math.toRadians(90)));

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            TrajectoryActionBuilder test = drive.actionBuilder(new Pose2d(-40, 64, Math.toRadians(90)))
                    .strafeTo(new Vector2d(0, 64));

            Actions.runBlocking(
                    new SequentialAction(
                            test.build()
                    )
            );

            sleep(5000);
        }
    }
}
