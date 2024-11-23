package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Extension {
    private DcMotor extensionHinge;
    private DcMotor extension;

    public Extension(HardwareMap hardwareMap) {
        extensionHinge = hardwareMap.get(DcMotor.class, "armHinge");
        extension = hardwareMap.get(DcMotor.class, "extension");
    }

    /*
    public Action hingeArmUp(int ticks) {
        extensionHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionHinge.setTargetPosition(ticks);

        extensionHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extensionHinge.setPower(speed);

        while (extensionHinge.isBusy() && opMode.opModeIsActive()) {
            displayData();
        }

        stopHinge();
    }
     */
}
