package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HorizontalExtension {
    private final DcMotor extension;

    public HorizontalExtension(HardwareMap hardwareMap) {
        extension = hardwareMap.get(DcMotor.class, "horizontalExtension");

        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Action moveTo(Positions.HorizontalExtPosition p) {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    extension.setTargetPosition(p.ticks);
                    extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    init = true;
                }

                if (extension.isBusy()) {
                    extension.setPower(1);
                    return true;
                } else {
                    extension.setPower(0);
                    extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    return false;
                }
            }
        };
    }
}
