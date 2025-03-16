package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Other Walking Juliette (PS4)")
public class WalkingJulietteOther extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot juliette = new Robot(hardwareMap, telemetry);
        juliette.init();

        boolean verticalClawPrev = false;
        // boolean verticalClawChanged = false;
        boolean horizontalClawPrev = gamepad2.circle;
        boolean horizontalWristPrev = gamepad2.right_trigger > 0.5;
        boolean verticalHingePrev = gamepad2.square;
        boolean horizontalHingePrev = gamepad2.triangle;

        boolean verticalClawCurrent = false;
        boolean hangArmGoing = false;

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (opModeIsActive()) {
                verticalClawCurrent = gamepad2.cross;

                // Drivetrain controls
                if (gamepad1.right_bumper) juliette.drive(1, gamepad1); // Standard - 0.84
                else if (gamepad1.left_bumper) juliette.drive(0.7, gamepad1); // Standard - 0.4
                else juliette.drive(0.9, gamepad1); // May make slower
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

                if (gamepad1.circle) juliette.closeHorizontalClaw();
                if (gamepad2.circle && gamepad2.circle != horizontalClawPrev) {
                    if (juliette.getHorizontalClawPosition() == 0.87) juliette.openHorizontalClaw();
                    else juliette.closeHorizontalClaw();
                }

                // Wrist Controls
                if (gamepad2.right_trigger > 0.5 & (gamepad2.right_bumper || gamepad2.left_bumper)) juliette.horizontalWristMid();
                else if (gamepad2.right_trigger > 0.5 && gamepad2.right_trigger > 0.5 != horizontalWristPrev) {
                    if (juliette.getHorizontalWristPosition() == 0.87) juliette.horizontalWristDown();
                    else juliette.horizontalWristUp();
                }

                // Claw Hinge Controls
                if (gamepad2.square && (gamepad2.right_bumper || gamepad2.left_bumper)) {
                    juliette.closeVerticalClaw();
                    sleep(250);
                    juliette.verticalHingeMid();
                } else if (gamepad2.square && gamepad2.square != verticalHingePrev) {
                    juliette.drive(0, gamepad1);
                    juliette.closeVerticalClaw();
                    sleep(250);
                    if (0.02 <= juliette.getVerticalHingePosition() && juliette.getVerticalHingePosition() <= 0.03) {
                        juliette.verticalHingeUp();
                    } else juliette.verticalHingeDown();
                }

                if (gamepad2.triangle && (gamepad2.right_bumper || gamepad2.left_bumper)) juliette.horizontalHingeMid();
                else if (gamepad2.triangle && gamepad2.triangle != horizontalHingePrev) {
                    if (juliette.getHorizontalHingePosition() == 0) juliette.horizontalHingeDown();
                    else {
                        juliette.drive(0, gamepad1);
                        juliette.loosenHorizontalClaw();
                        juliette.horizontalHingeUp();
                        sleep(900);
                        juliette.closeHorizontalClaw();
                    }
                }

                // Driver Automation
                if (gamepad1.left_trigger > 0.5) {
                    juliette.drive(0, gamepad1);

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
                    juliette.drive(0, gamepad1);

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
                horizontalClawPrev = gamepad2.circle;
                horizontalWristPrev = gamepad2.right_trigger > 0.5;
                verticalHingePrev = gamepad2.square;
                horizontalHingePrev = gamepad2.triangle;

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
