package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Extension {
    private final DcMotorEx extension;
    private final DcMotorEx extensionHinge;
    private final PIDFController hingePID;
    private final PIDFController extensionPID;

    public enum ExtensionPosition {
        BOTTOM,
        BUCKET,
        SPECIMEN,
        PICKUP,
        HANG_1,
        HANG_2,
        HANG_3
    }

    public enum HingePosition {
        BOTTOM,
        TOP,
        SPECIMEN,
        HANG_FLEX
    }

    public Extension(HardwareMap hardwareMap) {
        hingePID = new PIDFController(5, 0.1, 0.01, 0); // 5, 0.05, 0.01
        extensionPID = new PIDFController(10, 0.1, 0.1, 0);

        extension = hardwareMap.get(DcMotorEx.class, "extension");
        extensionHinge = hardwareMap.get(DcMotorEx.class, "armHinge");

        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extensionHinge.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extensionHinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Action hingePIDControl(HingePosition pos) {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    int target = 5;
                    switch (pos) {
                        case TOP:
                            target = 800;
                            break;
                        case BOTTOM:
                            target = 10;
                            break;
                        case SPECIMEN:
                            target = 540;
                            break;
                        case HANG_FLEX:
                            target = 460;
                            break;
                    }

                    hingePID.setSetPoint(target);
                    init = true;
                }

                if (!hingePID.atSetPoint()) {
                    double output = hingePID.calculate(extensionHinge.getCurrentPosition(), hingePID.getSetPoint());
                    extensionHinge.setVelocity(output);
                    return true;
                }

                extensionHinge.setVelocity(0.01);
                return false;
            }
        };
    }

    public Action extensionPIDControl(ExtensionPosition pos) {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    int target = 5;
                    switch (pos) {
                        case BOTTOM:
                            target = 15;
                            break;
                        case PICKUP:
                            target = 1150;
                            break;
                        case BUCKET:
                            target = 2800;
                            break;
                        case SPECIMEN:
                            target = 200;
                            break;
                        case HANG_1:
                            target = 1000;
                            break;
                        case HANG_2:
                            target = 325;
                            break;
                        case HANG_3:
                            target = 300;
                            break;
                    }

                    extensionPID.setSetPoint(target);
                    init = false;
                }

                if (!extensionPID.atSetPoint()) {
                    double output = extensionPID.calculate(extension.getCurrentPosition(), extensionPID.getSetPoint());
                    extension.setVelocity(output);
                    return true;
                }

                extension.setVelocity(0.01);
                return false;
            }
        };
    }
}
