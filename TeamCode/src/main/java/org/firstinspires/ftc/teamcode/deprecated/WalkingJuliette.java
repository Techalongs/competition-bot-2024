package org.firstinspires.ftc.teamcode.deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrivetrain;

@TeleOp(name = "Walking Juliette")
@Deprecated
@Disabled
public class WalkingJuliette extends LinearOpMode {
    @Override
    public void runOpMode() {
//        Robot juliette = new Robot(hardwareMap, telemetry, this);
//        juliette.init();

        MecanumDrivetrain drivetrain = new MecanumDrivetrain(hardwareMap);
        // Extension extension = new Extension(hardwareMap);
        // VerticalClaw claw = new VerticalClaw(hardwareMap);
        // Hooks hooks = new Hooks(hardwareMap);

//        boolean clawHingePrev = gamepad2.right_bumper;
//        boolean clawPrev = gamepad2.left_bumper;

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

//        Actions.runBlocking(new ParallelAction(
//                claw.hingeUp(),
//                claw.close()
//        ));

        while (opModeIsActive()) {
            if (opModeIsActive()) {
                // Drivetrain controls
                if (gamepad1.right_bumper) drivetrain.driveBlocking(gamepad1, 0.8); // Standard - 0.84
                else if (gamepad1.left_bumper) drivetrain.driveBlocking(gamepad1, 0.4); // Standard - 0.4
                else drivetrain.driveBlocking(gamepad1, 0.6);
                // Set normal speed to 0.5 at beginning of new season - for practice

//                // Pickup
//                if (gamepad2.a) {
//                    Actions.runBlocking(
//                            new SequentialAction(
//                                    claw.hingeUp(),
//                                    extension.hingePIDControl(Extension.HingePosition.BOTTOM),
//                                    extension.extensionPIDControl(Extension.ExtensionPosition.MID),
//                                    claw.openClaw(),
//                                    sleepAction(100),
//                                    claw.hingeDown()
//                            )
//                    );
//                }
//
//                // Specimen
//                if (gamepad2.b && gamepad2.right_trigger > 0.5) { // Part 1
//                    Actions.runBlocking(extension.hingePIDControl(Extension.HingePosition.HANG_FLEX));
//                } else if (gamepad2.b) { // Part 2
//                    Actions.runBlocking(
//                            new SequentialAction(
//                                    claw.close(),
//                                    claw.hingeUp(),
//                                    extension.hingePIDControl(Extension.HingePosition.TOP),
//                                    sleepAction(100),
//                                    extension.extensionPIDControl(Extension.ExtensionPosition.SPECIMEN)
//                            )
//                    );
//                }
//
//                // Sample
//                if (gamepad2.x && gamepad2.right_trigger > 0.5) { // Part 2
//                    Actions.runBlocking(new SequentialAction(
//                            claw.hingeUp(),
//                            sleepAction(250),
//                            claw.openClaw(),
//                            sleepAction(250),
//                            claw.hingeDown(),
//                            sleepAction(250),
//                            extension.hingePIDControl(Extension.HingePosition.HANG_FLEX),
//                            extension.extensionPIDControl(Extension.ExtensionPosition.BOTTOM),
//                            extension.hingePIDControl(Extension.HingePosition.BOTTOM),
//                            claw.hingeUp()
//                    ));
//                } else if (gamepad2.x) { // Part 1
//                    Actions.runBlocking(
//                            new SequentialAction(
//                                    claw.hingeDown(),
//                                    extension.hingePIDControl(Extension.HingePosition.TOP),
//                                    extension.extensionPIDControl(Extension.ExtensionPosition.BUCKET)
//                            )
//                    );
//                }
//
//                // Base Position
//                if (gamepad2.y) {
//                    Actions.runBlocking(
//                            new SequentialAction(
//                                    claw.hingeUp(),
//                                    extension.extensionPIDControl(Extension.ExtensionPosition.BOTTOM),
//                                    extension.hingePIDControl(Extension.HingePosition.BOTTOM)
//                            )
//                    );
//                }
//
//                // VerticalClaw Toggle
//                if (gamepad2.left_bumper && gamepad2.left_bumper != clawPrev) {
//                    if (claw.getPosition() == VerticalClaw.Position.OPEN)
//                        Actions.runBlocking(claw.close());
//                    else Actions.runBlocking(claw.openClaw());
//                    sleep(250);
//                }
//
//                // VerticalClaw Hinge Toggle
//                if (gamepad2.right_bumper && gamepad2.right_bumper != clawHingePrev) {
//                    if (claw.getHingePosition() == VerticalClaw.HingePosition.UP)
//                        Actions.runBlocking(claw.hingeDown());
//                    else Actions.runBlocking(claw.hingeUp());
//                    sleep(250);
//                }
//
//                // Hang
//                if (gamepad2.left_trigger > 0.7 && gamepad2.right_trigger > 0.7 && gamepad2.dpad_up) {
//                    Actions.runBlocking(
//                            new SequentialAction(
//                                    extension.extensionPIDControl(Extension.ExtensionPosition.HANG_1),
//                                    sleepAction(100),
//                                    // hooks.hooksPIDControl(Hooks.HookPosition.READY),
//                                    // sleepAction(100),
//                                    extension.hingePIDControl(Extension.HingePosition.TOP),
//                                    sleepAction(250),
//                                    extension.extensionPIDControl(Extension.ExtensionPosition.HANG_2),
//                                    sleepAction(100),
//                                    new ParallelAction(
//                                            extension.hingePIDControl(Extension.HingePosition.BOTTOM),
//                                            extension.extensionPIDControl(Extension.ExtensionPosition.BOTTOM)
//                                    )
////                                    sleepAction(100),
////                                    new ParallelAction(
////                                            extension.hingePIDControl(Extension.HingePosition.BOTTOM),
////                                            extension.extensionPIDControl(Extension.ExtensionPosition.HANG_3)
////                                    ),
////                                    sleepAction(100),
////                                    extension.extensionPIDControl(Extension.ExtensionPosition.HANG_1),
////                                    sleepAction(100)
//                                    // extension.hingePIDControl(Extension.HingePosition.HANG_FLEX)
//                            )
//                    );
                    // extend to hang_2??
                    // hinge to top
                    // extend hang_1
                    // hooks to ready
                    // extend bottom
                    // hooks to hook
                    // hinge to bottom
                }

                // juliette.displayData();
        }
    }
}

//    public Action sleepAction(int milliseconds) {
//        return new Action() {
//            @Override
//            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//                sleep(milliseconds);
//                return false;
//            }
//        };
//    }