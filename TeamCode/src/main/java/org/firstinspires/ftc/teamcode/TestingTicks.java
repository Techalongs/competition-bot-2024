package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Tick Tester")
public class TestingTicks extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot juliette = new Robot(hardwareMap, telemetry, this);
        juliette.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            juliette.displayData();
        }
    }
}
