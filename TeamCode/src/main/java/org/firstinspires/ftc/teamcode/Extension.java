package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Extension {
    private final DcMotor extension;
    private final DcMotor extensionHinge;

    public Extension(HardwareMap hardwareMap) {
        extension = hardwareMap.get(DcMotor.class, "extension");
        extensionHinge = hardwareMap.get(DcMotor.class, "armHinge");
    }

    public Action extend() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                //extension.setTargetPosition(ticks);

                extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                extension.setPower(0.8);
                extension.setPower(0);

                return false;
            }
        };
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
