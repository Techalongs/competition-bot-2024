package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Walking Juliette Temp")
public class WalkingJulietteTemp extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot juliette = new Robot(hardwareMap, telemetry);
        juliette.init();

        boolean verticalClawPrev = false;
        // boolean verticalClawChanged = false;
        boolean horizontalClawPrev = gamepad2.b;
        boolean horizontalWristPrev = gamepad2.right_trigger > 0.5;
        boolean verticalHingePrev = gamepad2.x;
        boolean horizontalHingePrev = gamepad2.y;

        boolean verticalClawCurrent = false;
        boolean hangArmGoing = false;

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (opModeIsActive()) {
                verticalClawCurrent = gamepad2.a;

                // Drivetrain controls
                if (gamepad1.right_bumper) juliette.drivetrain.driveBlocking(gamepad1, 1); // Standard - 0.84
                else if (gamepad1.left_bumper) juliette.drivetrain.driveBlocking(gamepad1, 0.7); // Standard - 0.4
                else juliette.drivetrain.driveBlocking(gamepad1, 0.9); // May make slower
                // Set normal speed to 0.5 at beginning of next season - for practice

                // Arm and Extension Controls
                juliette.moveArm(-gamepad2.left_stick_y);
                juliette.moveExtension(-gamepad2.right_stick_y);

                if (!hangArmGoing)
                    juliette.moveHangArm((gamepad2.dpad_up) ? 1 : (gamepad2.dpad_down) ? -1 : 0);

                // Claw Controls
                if (verticalClawCurrent && verticalClawCurrent != verticalClawPrev) {
                    if (juliette.getVerticalClawPosition() == 1) juliette.closeVerticalClaw();
                    else juliette.openVerticalClaw();
                }

                if (gamepad1.b) juliette.closeHorizontalClaw();
                if (gamepad2.b && gamepad2.b != horizontalClawPrev) {
                    if (juliette.getHorizontalClawPosition() == 0.87) juliette.openHorizontalClaw();
                    else juliette.closeHorizontalClaw();
                }

                // Wrist Controls
                if (gamepad2.right_trigger > 0.5 & (gamepad2.right_bumper || gamepad2.left_bumper)) juliette.horizontalWristMid();
                else if (gamepad2.right_trigger > 0.5 && gamepad2.right_trigger > 0.5 != horizontalWristPrev) {
                    if (juliette.getHorizontalWristPosition() == 0.9) juliette.horizontalWristDown();
                    else juliette.horizontalWristUp();
                }

                // Claw Hinge Controls
                if (gamepad2.x && (gamepad2.right_bumper || gamepad2.left_bumper)) {
                    juliette.closeVerticalClaw();
                    sleep(250);
                    juliette.verticalHingeMid();
                } else if (gamepad2.x && gamepad2.x != verticalHingePrev) {
                    juliette.drivetrain.driveBlocking(gamepad1, 0);
                    juliette.closeVerticalClaw();
                    if (0.02 <= juliette.getVerticalHingePosition() && juliette.getVerticalHingePosition() <= 0.03) {
                        juliette.verticalHingeUp();
                    } else juliette.verticalHingeDown();
                }

                if (gamepad2.y && (gamepad2.right_bumper || gamepad2.left_bumper)) juliette.horizontalHingeMid();
                else if (gamepad2.y && gamepad2.y != horizontalHingePrev) {
                    if (juliette.getHorizontalHingePosition() == 0) juliette.horizontalHingeDown();
                    else {
                        juliette.drivetrain.driveBlocking(gamepad1, 0);
                        juliette.loosenHorizontalClaw();
                        juliette.horizontalHingeUp();
                        sleep(900);
                        juliette.closeHorizontalClaw();
                    }
                }

                // Driver Automation
                if (gamepad1.left_trigger > 0.5) {
                    juliette.drivetrain.driveBlocking(gamepad1, 0);

                    juliette.closeHorizontalClaw();
                    juliette.horizontalHingeUp();
                    juliette.horizontalWristDown();
                    juliette.openHorizontalClaw();
                    sleep(500);
                    juliette.closeVerticalClaw();
                    sleep(500);
                    juliette.verticalHingeDown();
                    sleep(250);
                    juliette.openVerticalClaw();
                }

                if (gamepad2.right_bumper && gamepad2.left_bumper && gamepad2.dpad_down) {
                    juliette.drivetrain.driveBlocking(gamepad1, 0);

                    while (juliette.getArmPosition() > 10) {
                        juliette.moveArm(-1);
                    }
                    juliette.moveArm(0);

                    while (juliette.getExtensionPosition() > 10) {
                        juliette.moveExtension(-1);
                    }
                    juliette.moveExtension(0);

                    juliette.horizontalWristDown();
                    sleep(500);
                    juliette.closeHorizontalClaw();
                    sleep(500);
                    juliette.horizontalWristUp();
                    sleep(500);
                    juliette.horizontalWristDown();
                    sleep(750);
                    juliette.horizontalWristUp();
                    sleep(500);
                    juliette.closeVerticalClaw();
                    sleep(500);
                    juliette.openHorizontalClaw();
                    sleep(500);
                    juliette.verticalHingeMid();
                }

                if (gamepad2.dpad_right && gamepad2.left_bumper && gamepad2.right_bumper) {
                    hangArmGoing = juliette.moveHangArmTo(24300);
                }

                verticalClawPrev = verticalClawCurrent;
                horizontalClawPrev = gamepad2.b;
                horizontalWristPrev = gamepad2.right_trigger > 0.5;
                verticalHingePrev = gamepad2.x;
                horizontalHingePrev = gamepad2.y;

                juliette.addData("Vertical Claw Prev", String.valueOf(verticalClawPrev));
                // juliette.addData("Vertical Claw Changed", String.valueOf(verticalClawChanged));
                juliette.addData("Horizontal Claw Prev", String.valueOf(horizontalClawPrev));
                juliette.addData("Horizontal Wrist Prev", String.valueOf(horizontalWristPrev));
                juliette.addData("Vertical Hinge Prev", String.valueOf(verticalHingePrev));
                juliette.addData("Horizontal Hinge Prev", String.valueOf(horizontalHingePrev));

                juliette.displayData();
            }
        }
    }
}
