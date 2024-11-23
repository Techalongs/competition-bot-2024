package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Walking Juliette")
public class WalkingJuliette extends LinearOpMode {
    private int stayPosition;
    @Override
    public void runOpMode() {
        Robot juliette = new Robot(hardwareMap, telemetry, this, 0.9);
        juliette.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (opModeIsActive()) {
                // Drivetrain controls
                double y = 0;

                if (gamepad1.right_bumper) juliette.drive(0.5, gamepad1, y); // Standard - 0.84
                else if (gamepad1.left_bumper) juliette.drive(0.2, gamepad1, y); // Standard - 0.4
                else juliette.drive(0.4, gamepad1, y);
                // Set normal speed to 0.5 at beginning of next season - for practice

                // Arm Controls
                //juliette.hingeArm(-gamepad2.left_stick_y * 0.6);
                if (gamepad2.left_stick_y == 0 && juliette.getExtensionHingePosition() != stayPosition && juliette.getExtensionHingePosition() < 200) {
                    if (stayPosition == 0) stayPosition = juliette.getExtensionPosition();
                    juliette.stayHinge(stayPosition);
                } else if (gamepad2.left_stick_y == 0) {
                    juliette.stopHinge();
                } else {
                    stayPosition = 0;

                    double armPower = -gamepad2.left_stick_y;
                    if (armPower > 0) juliette.hingeArm(armPower * 0.6);
                    else juliette.hingeArm(armPower);
                }

                // Extension Controls
                juliette.moveExtension(-gamepad2.right_stick_y * 0.6);

                // Claw Hinge Controls
                if (gamepad2.left_bumper) juliette.clawHingeUp();
                else if (gamepad2.right_bumper) juliette.clawHingeDown();

                // Claw Controls
                if (gamepad2.a) juliette.closeClaws();
                else if (gamepad2.b) juliette.openClaws();

                juliette.displayData();
            }
        }
    }
}
