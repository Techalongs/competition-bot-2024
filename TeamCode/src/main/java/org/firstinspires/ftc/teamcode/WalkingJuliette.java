//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//@TeleOp(name = "Walking Juliette")
//public class WalkingJuliette extends LinearOpMode {
//    @Override
//    public void runOpMode() {
//        Robot juliette = new Robot(hardwareMap, telemetry, this);
//        juliette.init();
//
//        MecanumDrivetrain drivetrain = new MecanumDrivetrain(hardwareMap);
//        Extension extension = new Extension(hardwareMap);
//        Claw claw = new Claw(hardwareMap);
//        Hooks hooks = new Hooks(hardwareMap);
//
//        telemetry.addData("Status", "Initialized");
//        telemetry.update();
//        waitForStart();
//
//        while (opModeIsActive()) {
//            if (opModeIsActive()) {
//                // Drivetrain controls
//                double y = 0;
//
//                if (gamepad1.right_bumper) drivetrain.drive(0.8, gamepad1, y); // Standard - 0.84
//                else if (gamepad1.left_bumper) drivetrain.drive(0.4, gamepad1, y); // Standard - 0.4
//                else drivetrain.drive(0.6, gamepad1, y);
//                // Set normal speed to 0.5 at beginning of next season - for practice
//
//                // Arm Controls
//                extension.hinge(-gamepad2.right_stick_y * 0.4);
//
//                // Extension Controls
//                extension.moveExtension(-gamepad2.left_stick_y);
//
//                // Claw Hinge Controls
//                if (gamepad2.left_bumper) claw.hingeUp();
//                else if (gamepad2.right_bumper) claw.hingeDown();
//
//                // Claw Controls
//                if (gamepad2.a) claw.closeClaw();
//                else if (gamepad2.b) claw.openClaw();
//
//                // Hook Controls
//                if (gamepad2.dpad_up) hooks.hingeUp();
//                else if (gamepad2.dpad_down) hooks.hingeDown();
//
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
//
//                juliette.displayData();
//            }
//        }
//    }
//}
