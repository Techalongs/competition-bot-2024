package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous (name = "Autonomous")
public class Autonomous extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot juliette = new Robot(hardwareMap, telemetry, this, 0.7);
        juliette.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (opModeIsActive()) {
                juliette.moveForward(150);
                sleep(1000);
                juliette.strafeRight(1400);
                break;
            }
        }
    }
}
