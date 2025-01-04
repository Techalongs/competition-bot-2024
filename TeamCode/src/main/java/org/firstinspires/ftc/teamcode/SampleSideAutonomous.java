package org.firstinspires.ftc.teamcode;


import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// IN PROGRESS
@Autonomous(name = "Sample Autonomous")
@Disabled
public class SampleSideAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(11.25, 35.04, Math.toRadians(266.77)));
//            TrajectoryActionBuilder traj1 = drive.actionBuilder(new Pose2d(15.11, 64.93, Math.toRadians(-90.00)))
//                    .splineTo(new Vector2d(6.75, 34.07), Math.toRadians(270)); // Move robot back bit


//            Actions.runBlocking(new SequentialAction(
//                    traj1.build()
//            ));

            TrajectoryActionBuilder traj2 = drive.actionBuilder(new Pose2d(14.35, 63.11, Math.toRadians(270.00)))
                    .splineTo(new Vector2d(47.85, 38.51), Math.toRadians(270.00));

            Actions.runBlocking(new SequentialAction(
                    traj2.build()
            ));
        }
    }
}
