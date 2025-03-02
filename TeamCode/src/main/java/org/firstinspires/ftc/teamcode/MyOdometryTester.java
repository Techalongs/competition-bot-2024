package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "My Odometry Tester")
public class MyOdometryTester extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(11.25, 35.04, Math.toRadians(270)));

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            TrajectoryActionBuilder traj = drive.actionBuilder(new Pose2d(10, 64, Math.toRadians(270)))
                    .strafeTo(new Vector2d(10, 70))
                    .strafeTo(new Vector2d(10, 58))
                    .turn(Math.toRadians(-30));

            Actions.runBlocking(
                    new SequentialAction(
                            traj.build()
                    )
            );
        }
    }
}
