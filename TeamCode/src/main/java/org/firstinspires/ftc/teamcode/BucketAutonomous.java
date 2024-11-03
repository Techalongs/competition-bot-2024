package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name = "Bucket Autonomous")
public class BucketAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot juliette = new Robot(hardwareMap, telemetry, this, 0.7);
        juliette.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (opModeIsActive()) {
                juliette.moveForward(150);
                sleep(500);
                juliette.strafeLeft(1350);
                sleep(500);
                juliette.turnLeft(1050);
                sleep(500);
                juliette.hingeArm(1500); // TODO: EDIT
                sleep(500);
                juliette.extendExtension(750); // TODO: EDIT
                sleep(500);
                juliette.clawHingeDown();
                sleep(500);
                juliette.openClaws();
                sleep(500);
                juliette.closeClaws();
                juliette.clawHingeUp();
                sleep(500);
                juliette.retractExtension(750); // TODO: EDIT - Same as Line 27
                sleep(500);
                juliette.hingeArm(-1300); // TODO: EDIT - Same as Line 25
                sleep(500);
                juliette.moveBackward(4800); // TODO: EDIT
                break;
            }
        }
    }
}
