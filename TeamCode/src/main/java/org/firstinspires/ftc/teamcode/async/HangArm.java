package org.firstinspires.ftc.teamcode.async;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Positions;

public class HangArm {

    private final DcMotor extension;
    private static final double DEFAULT_EXTENSION_POWER = 1.0;

    public HangArm(HardwareMap hardwareMap) {
        extension = hardwareMap.get(DcMotor.class, "hangArm");
        // extension.setDirection(DcMotorSimple.Direction.REVERSE);
        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveExtensionBlocking(double power) {
        extension.setPower(power);
    }

    public boolean moveExtensionToBlocking(Positions.HangExtPosition pos) {
        return this.moveExtensionToBlocking(pos.ticks);
    }

    public boolean moveExtensionToBlocking(int pos) {
        extension.setTargetPosition(pos / 2);
        extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (extension.getCurrentPosition() > extension.getTargetPosition()) {
            extension.setPower(1);
            return true;
        } else {
            extension.setPower(0);
            return false;
        }
    }

    public double getExtensionPower() {
        return extension.getPower();
    }

    public double getExtensionCurrentPosition() {
        return extension.getCurrentPosition();
    }

    public double getExtensionTargetPosition() {
        return extension.getTargetPosition();
    }

}
