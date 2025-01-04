package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Walking Juliette Temp")
public class WalkingJulietteTemp extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot juliette = new Robot(hardwareMap, telemetry);
        juliette.init();

        boolean verticalClawPrev = gamepad2.a;
        boolean horizontalClawPrev = gamepad2.b;
        boolean horizontalWristPrev = gamepad2.right_trigger > 0.5;
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
                    if (juliette.getVerticalClawPosition() == 1) juliette.closeVerticalClaw();
                    else juliette.openVerticalClaw();
                }

                if (gamepad2.b && gamepad2.b != horizontalClawPrev) {
                    if (juliette.getHorizontalClawPosition() == 1) juliette.closeHorizontalClaw();
                    else juliette.openHorizontalClaw();
                }

                // Wrist Controls
                if (gamepad2.right_trigger > 0.5 && gamepad2.right_trigger > 0.5 != horizontalWristPrev) {
                    if (juliette.getHorizontalWristPosition() == 1) juliette.horizontalWristDown();
                    else {
                        juliette.loosenHorizontalClaw();
                        juliette.horizontalWristUp();
                        sleep(100);
                        juliette.closeHorizontalClaw();
                    }
                }

                // Claw Hinge Controls
                if (gamepad2.x && (gamepad2.right_bumper || gamepad2.left_bumper)) {
                    juliette.closeVerticalClaw();
                    sleep(250);
                    juliette.verticalHingeMid();
                } else if (gamepad2.x && gamepad2.x != verticalHingePrev) {
                    juliette.drive(0, gamepad1);
                    juliette.closeVerticalClaw();
                    sleep(250);
                    if (juliette.getVerticalHingePosition() == 0) {
                        juliette.verticalHingeUp();
                    } else juliette.verticalHingeDown();
                }

                if (gamepad2.y && (gamepad2.right_bumper || gamepad2.left_bumper)) juliette.horizontalHingeMid();
                else if (gamepad2.y && gamepad2.y != horizontalHingePrev) {
                    if (juliette.getHorizontalHingePosition() == 0) juliette.horizontalHingeDown();
                    else {
                        juliette.drive(0, gamepad1);
                        juliette.horizontalHingeUp();
                        sleep(850);
                        juliette.loosenHorizontalClaw();
                        juliette.closeHorizontalClaw();
                    }
                }

                // Driver Automation
                if (gamepad2.right_bumper && gamepad2.left_bumper && gamepad2.dpad_down) {
                    juliette.drive(0, gamepad1);

                    while (juliette.getExtensionPosition() < 10) {
                        juliette.moveExtension(-1);
                    }
                    juliette.moveExtension(0);

                    juliette.closeVerticalClaw();
                    sleep(500);
                    juliette.openHorizontalClaw();
                    sleep(500);
                    juliette.verticalHingeMid();
                }

                verticalClawPrev = gamepad2.a;
                horizontalClawPrev = gamepad2.b;
                horizontalWristPrev = gamepad2.right_trigger > 0.5;
                verticalHingePrev = gamepad2.x;
                horizontalHingePrev = gamepad2.y;

                juliette.displayData();
            }
        }
    }
}
