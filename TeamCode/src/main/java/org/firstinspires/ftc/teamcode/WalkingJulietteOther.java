package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;

@TeleOp(name = "Other Walking Juliette (PS4)")
public class WalkingJulietteOther extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot juliette = new Robot(hardwareMap, telemetry);
        juliette.init();
        HorizontalClaw horizontalClaw = new HorizontalClaw(hardwareMap);
        HorizontalExtension horizontalExtension = new HorizontalExtension(hardwareMap);
        VerticalClaw verticalClaw = new VerticalClaw(hardwareMap);
        VerticalArm verticalArm = new VerticalArm(hardwareMap);

        Action driveTrainActions = new SleepAction(0);
        Action armActions = new SleepAction(0);
        Action verticalClawActions = new SleepAction(0);
        Action horizontalClawActions = new SleepAction(0);

        boolean verticalClawPrev = false;
        // boolean verticalClawChanged = false;
        boolean horizontalClawPrev = gamepad2.circle;
        boolean horizontalWristPrev = gamepad2.right_trigger > 0.5;
        boolean verticalHingePrev = gamepad2.square;
        boolean horizontalHingePrev = gamepad2.triangle;

        boolean verticalClawCurrent;
        boolean hangArmGoing = false;

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (opModeIsActive()) {
                verticalClawCurrent = gamepad2.cross;

                // Drivetrain controls
                if (gamepad1.right_bumper)
                    driveTrainActions = juliette.drivetrain.drive(1, gamepad1); // Standard - 0.84
                else if (gamepad1.left_bumper)
                    driveTrainActions = juliette.drivetrain.drive(0.7, gamepad1); // Standard - 0.4
                else
                    driveTrainActions = juliette.drivetrain.drive(0.9, gamepad1); // May make slower
                // Set normal speed to 0.5 at beginning of next season - for practice

                // Arm and Extension Controls
                armActions = new ParallelAction(
                        verticalArm.moveArm(-gamepad2.left_stick_y),
                        horizontalExtension.move(-gamepad2.right_stick_y));

                if (!hangArmGoing)
                    juliette.moveHangArm((gamepad2.dpad_up) ? 1 : (gamepad2.dpad_down) ? -1 : 0);

                // Claw Controls
                if (verticalClawCurrent && verticalClawCurrent != verticalClawPrev) {
                    verticalClawActions = juliette.getVerticalClawPosition() == 0.9 ? verticalClaw.close()
                            : verticalClaw.open();
                }

                if (gamepad1.circle)
                    horizontalClawActions = horizontalClaw.close();
                if (gamepad2.circle && gamepad2.circle != horizontalClawPrev) {
                    horizontalClawActions = juliette.getHorizontalClawPosition() == 0.89 ? horizontalClaw.open()
                            : horizontalClaw.close();
                }

                // Wrist Controls
                if (gamepad2.right_trigger > 0.5 & (gamepad2.right_bumper || gamepad2.left_bumper))
                    horizontalClawActions = new SequentialAction(horizontalClawActions,
                            horizontalClaw.hingeTo(HorizontalClaw.HingePosition.MID));
                else if (gamepad2.right_trigger > 0.5 && gamepad2.right_trigger > 0.5 != horizontalWristPrev) {
                    horizontalClawActions = new SequentialAction(horizontalClawActions,
                            juliette.getHorizontalWristPosition() == 0.87
                                    ? horizontalClaw.hingeTo(HorizontalClaw.HingePosition.DOWN)
                                    : horizontalClaw.hingeTo(HorizontalClaw.HingePosition.UP));
                }

                // Claw Hinge Controls
                if (gamepad2.square && (gamepad2.right_bumper || gamepad2.left_bumper)) {
                    verticalClawActions = new SequentialAction(verticalClawActions,
                            verticalClaw.close(),
                            new SleepAction(250),
                            verticalClaw.hingeTo(VerticalClaw.HingePosition.SPECIMEN));
                } else if (gamepad2.square && gamepad2.square != verticalHingePrev) {
                    verticalClawActions = new SequentialAction(verticalClawActions,
                            new ParallelAction(
                                    juliette.drivetrain.drive(0,
                                            gamepad1),
                                    verticalClaw.close()),
                            new SleepAction(250),
                            (0.02 <= juliette.getVerticalHingePosition()
                                    && juliette.getVerticalHingePosition() <= 0.03)
                                            ? verticalClaw.hingeTo(VerticalClaw.HingePosition.UP)
                                            : verticalClaw.hingeTo(VerticalClaw.HingePosition.DOWN));

                }

                if (gamepad2.triangle && (gamepad2.right_bumper || gamepad2.left_bumper))
                    horizontalClawActions = new SequentialAction(horizontalClawActions,
                            horizontalClaw.hingeTo(HorizontalClaw.HingePosition.MID));
                else if (gamepad2.triangle && gamepad2.triangle != horizontalHingePrev) {
                    horizontalClawActions = new SequentialAction(horizontalClawActions,
                            juliette.getHorizontalHingePosition() == 0.5
                                    ? horizontalClaw.hingeTo(HorizontalClaw.HingePosition.DOWN)
                                    : new SequentialAction(
                                            new ParallelAction(
                                                    juliette.drivetrain.drive(0, gamepad1),
                                                    horizontalClaw.loosen(),
                                                    horizontalClaw.hingeTo(HorizontalClaw.HingePosition.UP)),
                                            new SleepAction(900),
                                            horizontalClaw.close()));
                }

                // This is where we run all the actions that were compiled above
                // TODO: This should really be partitioned out better. Probably have a separate
                // function
                // per group. I can imagine this would be pretty fragile and prone to errors.
                Actions.runBlocking(new SequentialAction(driveTrainActions, armActions,
                        verticalClawActions, horizontalClawActions));

                // Driver Automation
                // TODO: This should all be async as well
                if (gamepad1.left_trigger > 0.5) {
                    Actions.runBlocking(juliette.drivetrain.drive(0, gamepad1));

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
                    Actions.runBlocking(juliette.drivetrain.drive(0, gamepad1));

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
                // juliette.addData("Vertical Claw Changed",
                // String.valueOf(verticalClawChanged));
                juliette.addData("Horizontal Claw Prev", String.valueOf(horizontalClawPrev));
                juliette.addData("Horizontal Wrist Prev", String.valueOf(horizontalWristPrev));
                juliette.addData("Vertical Hinge Prev", String.valueOf(verticalHingePrev));
                juliette.addData("Horizontal Hinge Prev", String.valueOf(horizontalHingePrev));

                juliette.displayData();
            }
        }
    }
}
