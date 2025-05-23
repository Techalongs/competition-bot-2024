package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VerticalExtension {
    private final DcMotor extension;
    private final DigitalChannel bottomArmLimit;

    public VerticalExtension(HardwareMap hardwareMap) {
        extension = hardwareMap.get(DcMotor.class, "verticalArm");
        bottomArmLimit = hardwareMap.get(DigitalChannel.class, "bottomArmLimit");

        extension.setDirection(DcMotorSimple.Direction.REVERSE);
        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Action moveTo(Positions.VerticalExtPosition pos) {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    extension.setTargetPosition(pos.ticks);
                    extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    init = true;
                }

                if (pos == Positions.VerticalExtPosition.BOTTOM) {
                    if (bottomArmLimit.getState()) {
                        extension.setPower(1);
                        return true;
                    }

                    return false;
                } else if (extension.isBusy()) {
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
