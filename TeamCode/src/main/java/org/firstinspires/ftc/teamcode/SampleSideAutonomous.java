package org.firstinspires.ftc.teamcode;


import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class SampleSideAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(11.25, 35.04, Math.toRadians(266.77)));
            TrajectoryActionBuilder traj1 = drive.actionBuilder(new Pose2d(15.11, 64.93, Math.toRadians(-90.00)))
                    .splineTo(new Vector2d(6.75, 34.07), Math.toRadians(270)); // Move robot back bit

            Actions.runBlocking(new SequentialAction(
                    traj1.build()
            ));
        }
    }
}
