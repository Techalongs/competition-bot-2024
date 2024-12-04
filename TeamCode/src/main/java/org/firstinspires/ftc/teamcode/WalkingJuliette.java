package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Walking Juliette")
public class WalkingJuliette extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot juliette = new Robot(hardwareMap, telemetry, this);
        juliette.init();

        MecanumDrivetrain drivetrain = new MecanumDrivetrain(hardwareMap);
        Extension extension = new Extension(hardwareMap);
        Claw claw = new Claw(hardwareMap);
        Hooks hooks = new Hooks(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (opModeIsActive()) {
                // Drivetrain controls
                if (gamepad1.right_bumper) drivetrain.drive(0.8, gamepad1); // Standard - 0.84
                else if (gamepad1.left_bumper) drivetrain.drive(0.4, gamepad1); // Standard - 0.4
                else drivetrain.drive(0.6, gamepad1);
                // Set normal speed to 0.5 at beginning of new season - for practice

                // Pickup
                if (gamepad2.a) {
                    Actions.runBlocking(claw.hingeUp());
                    extension.hingePIDControl(Extension.HingePosition.BOTTOM);
                    extension.extensionPIDControl(Extension.ExtensionPosition.PICKUP);
                    Actions.runBlocking(new SequentialAction(
                            claw.openClaw(),
                            sleepAction(100),
                            claw.hingeDown()
                    ));
                }

                // Specimen
                if (gamepad2.b && gamepad2.right_trigger > 0.5) { // Part 1
                    extension.hingePIDControl(Extension.HingePosition.HANG_FLEX);
                } else if (gamepad2.b) { // Part 2
                    Actions.runBlocking(new SequentialAction(
                            claw.closeClaw(),
                            claw.hingeUp()
                    ));

                    extension.hingePIDControl(Extension.HingePosition.TOP);
                    sleep(100);
                    extension.extensionPIDControl(Extension.ExtensionPosition.SPECIMEN);
                }

                // Sample
                if (gamepad2.x && gamepad2.right_trigger > 0.5) { // Part 2
                    Actions.runBlocking(new SequentialAction(
                            claw.hingeUp(),
                            sleepAction(250),
                            claw.openClaw(),
                            sleepAction(250),
                            claw.hingeDown(),
                            sleepAction(250)
                    ));

                    extension.hingePIDControl(Extension.HingePosition.HANG_FLEX);
                    extension.extensionPIDControl(Extension.ExtensionPosition.BOTTOM);
                    extension.hingePIDControl(Extension.HingePosition.BOTTOM);

                    Actions.runBlocking(claw.hingeUp());
                } else if (gamepad2.x) { // Part 1
                    Actions.runBlocking(claw.hingeDown());
                    extension.hingePIDControl(Extension.HingePosition.TOP);
                    extension.extensionPIDControl(Extension.ExtensionPosition.BUCKET);
                }

                // Base Position
                if (gamepad2.y) {
                    Actions.runBlocking(claw.hingeUp());
                    extension.extensionPIDControl(Extension.ExtensionPosition.BOTTOM);
                    extension.hingePIDControl(Extension.HingePosition.BOTTOM);
                }

                // Claw Toggle
                if (gamepad2.left_bumper && gamepad2.right_trigger > 0.5) {
                    if (claw.getPosition() == Claw.ClawPosition.OPEN)
                        Actions.runBlocking(claw.closeClaw());
                    else Actions.runBlocking(claw.openClaw());
                    sleep(10);
                }

                // Claw Hinge Toggle
                if (gamepad2.right_bumper && gamepad2.left_trigger > 0.5) {
                    if (claw.getHingePosition() == Claw.ClawHingePosition.UP)
                        Actions.runBlocking(claw.hingeDown());
                    else Actions.runBlocking(claw.hingeUp());
                    sleep(10);
                }

                // Hang
                if (gamepad2.left_trigger > 0.7 && gamepad2.right_trigger > 0.7 && gamepad2.dpad_up) {
                    extension.extensionPIDControl(Extension.ExtensionPosition.HANG_1);
                    sleep(100);
                    hooks.hooksPIDControl(Hooks.HookPosition.READY);
                    sleep(100);
                    extension.hingePIDControl(Extension.HingePosition.TOP);
                    sleep(100);
                    extension.extensionPIDControl(Extension.ExtensionPosition.HANG_2);
                    sleep(100);
                    extension.hingePIDControl(Extension.HingePosition.HANG_FLEX);
                    sleep(100);
                    extension.extensionPIDControl(Extension.ExtensionPosition.HANG_3);
                    sleep(100);
                    hooks.hooksPIDControl(Hooks.HookPosition.HOOK);
                    sleep(100);
                    extension.extensionPIDControl(Extension.ExtensionPosition.HANG_1);
                    sleep(100);
                    extension.hingePIDControl(Extension.HingePosition.BOTTOM);
                }

                juliette.displayData();
            }
        }
    }

    public Action sleepAction(int milliseconds) {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                sleep(milliseconds);
                return false;
            }
        };
    }
}