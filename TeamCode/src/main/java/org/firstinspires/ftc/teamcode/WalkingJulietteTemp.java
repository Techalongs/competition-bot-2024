package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Walking Juliette Temp")
public class WalkingJulietteTemp extends LinearOpMode {
    private int stayPosition;
    @Override
    public void runOpMode() {
        Robot juliette = new Robot(hardwareMap, telemetry, 0.9);
        juliette.init();

        boolean verticalClawPrev = gamepad2.a;
        boolean horizontalClawPrev = gamepad2.b;
        boolean verticalHingePrev = gamepad2.x;
        boolean horizontalHingePrev = gamepad2.y;

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (opModeIsActive()) {
                // Drivetrain controls

                if (gamepad1.right_bumper) juliette.drive(0.8, gamepad1); // Standard - 0.84
                else if (gamepad1.left_bumper) juliette.drive(0.4, gamepad1); // Standard - 0.4
                else juliette.drive(0.6, gamepad1);
                // Set normal speed to 0.5 at beginning of next season - for practice

                // Arm and Extension Controls
                juliette.moveArm(gamepad2.left_stick_y);
                juliette.moveExtension(gamepad2.right_stick_y);

                // Claw Controls
                if (gamepad2.a && gamepad2.a != verticalClawPrev) {
                    if (juliette.getVerticalClawPosition() == 1) {
                        juliette.closeVerticalClaw();
                    } else juliette.openVerticalClaw();
                }

                if (gamepad2.b && gamepad2.b != horizontalClawPrev) {
                    if (juliette.getHorizontalClawPosition() == 1) {
                        juliette.closeHorizontalClaw();
                    } else juliette.openHorizontalClaw();
                }

                // Claw Hinge Controls
                if (gamepad2.x && gamepad2.x != verticalHingePrev) {
                    if (juliette.getVerticalHingePosition() == 1) {
                        juliette.verticalHingeUp();
                    } else juliette.verticalHingeDown();
                }

                if (gamepad2.y && gamepad2.y != horizontalHingePrev) {
                    if (juliette.getHorizontalHingePosition() == 1) {
                        juliette.horizontalHingeUp();
                    } else juliette.horizontalHingeDown();
                }

                juliette.displayData();
            }
        }
    }
}
