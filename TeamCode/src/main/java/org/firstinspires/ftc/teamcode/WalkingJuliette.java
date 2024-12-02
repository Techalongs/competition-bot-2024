package org.firstinspires.ftc.teamcode;

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
                // Set normal speed to 0.5 at beginning of next season - for practice

//                // Arm Controls
//                extension.hinge(-gamepad2.right_stick_y * 0.4);
//
//                // Extension Controls
//                extension.moveExtension(-gamepad2.left_stick_y);

//                // Claw Hinge Controls
//                if (gamepad2.left_bumper) Actions.runBlocking(claw.hingeUp());
//                else if (gamepad2.right_bumper) Actions.runBlocking(claw.hingeDown());
//
//                // Claw Controls
//                if (gamepad2.a) Actions.runBlocking(claw.closeClaw());
//                else if (gamepad2.b) Actions.runBlocking(claw.openClaw());
//
//                // Hook Controls
//                if (gamepad2.dpad_up) Actions.runBlocking(hooks.hingeUp());
//                else if (gamepad2.dpad_down) Actions.runBlocking(hooks.hingeDown());

//                // Driver Automation - Hang
//                if (gamepad2.right_trigger > 0.7 && gamepad2.left_trigger > 0.7 && gamepad1.y) {
//                    extension.hingeDownAll();
//                    extension.retractAll();
//                    extension.hingeUpAll();
//                    extension.extend(850);
//                    extension.hingeDown(200);
//                    extension.retract(750);
//
//                }

                // Pickup
                if (gamepad2.a) {
                    Actions.runBlocking(claw.hingeUp());
                    extension.hingePIDControl(Extension.HingePosition.BOTTOM);
                    extension.extensionPIDControl(Extension.ExtensionPosition.PICKUP);
                    Actions.runBlocking(claw.openClaw());
                    sleep(100);
                    Actions.runBlocking(claw.hingeDown());
                }

                // Specimen
                if (gamepad2.b && gamepad2.right_trigger > 0.5) { // Part 1
                    extension.hingePIDControl(Extension.HingePosition.HANG_FLEX);
                } else if (gamepad2.b && gamepad2.left_trigger > 0.5) {

                } else if (gamepad2.b) { // Part 2
                    Actions.runBlocking(claw.closeClaw());
                    Actions.runBlocking(claw.hingeUp());
                    extension.hingePIDControl(Extension.HingePosition.TOP);
                    sleep(100);
                    extension.extensionPIDControl(Extension.ExtensionPosition.SPECIMEN);
                }

                // Sample
                if (gamepad2.x && gamepad2.right_trigger > 0.5) { // Part 2
                    Actions.runBlocking(claw.hingeUp());
                    sleep(250);
                    Actions.runBlocking(claw.openClaw());
                    sleep(250);
                    Actions.runBlocking(claw.hingeDown());
                    sleep(250);
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
                    if (claw.getPosition() == Claw.ClawPosition.OPEN) Actions.runBlocking(claw.closeClaw());
                    else Actions.runBlocking(claw.openClaw());
                    sleep(10);
                }

                // Claw Hinge Toggle
                if (gamepad2.right_bumper && gamepad2.right_trigger > 0.5) {
                    if (claw.getHingePosition() == Claw.ClawHingePosition.UP) Actions.runBlocking(claw.hingeDown());
                    else Actions.runBlocking(claw.hingeUp());
                    sleep(10);
                }

                // Hang
                if (gamepad2.left_trigger > 0.7 && gamepad2.right_trigger > 0.7 && gamepad2.dpad_up) {
                    extension.extensionPIDControl(Extension.ExtensionPosition.HANG_1);
                    sleep(100);
                    extension.hingePIDControl(Extension.HingePosition.TOP);
                    sleep(100);
                    hooks.hooksPIDControl(Hooks.HookPosition.READY);
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
}
