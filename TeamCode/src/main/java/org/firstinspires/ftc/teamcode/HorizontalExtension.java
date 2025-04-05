package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class HorizontalExtension {
    private final DcMotor extension;
    private final TouchSensor bottomExtensionLimit;

    public static final int MAX_EXTENSION_POSITION = 1500;
    public static final int MIN_EXTENSION_POSITION = 100;

    public enum Position {
        BOTTOM(-10),
        TOP(-1500);

        private final int ticks;

        Position(int ticks) {
            this.ticks = ticks;
        }
    }

    public HorizontalExtension(HardwareMap hardwareMap) {
        extension = hardwareMap.get(DcMotor.class, "horizontalExtension");
        bottomExtensionLimit = hardwareMap.get(TouchSensor.class, "bottomExtensionLimit");

        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Action moveTo(Position pos) {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    extension.setTargetPosition(pos.ticks);
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

    public Action move(double power) {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if ((!isBottomExtensionLimitPressed() || power > 0)
                        && (extension.getCurrentPosition() < MAX_EXTENSION_POSITION || power < 0))
                    extension.setPower(power);
                else
                    extension.setPower(0);

                if (isBottomExtensionLimitPressed() && extension.getCurrentPosition() > MIN_EXTENSION_POSITION) {
                    extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
                return false;
            }
        };
    }

    public boolean isBottomExtensionLimitPressed() {
        return bottomExtensionLimit.isPressed();
    }
}
