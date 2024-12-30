package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class SpecimenAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(11.25, 35.04, Math.toRadians(270)));
        Extension extension = new Extension(hardwareMap);
        Claw claw = new Claw(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        // Goes to specimen
        TrajectoryActionBuilder traj1 = drive.actionBuilder(new Pose2d(-16, 64, Math.toRadians(90)))
                .strafeTo(new Vector2d(-5, 33));


    }
}
