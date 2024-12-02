package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Extension {
    private final DcMotorEx extension;
    private final DcMotorEx extensionHinge;
    private final TouchSensor hingeTopLimit;
    private final TouchSensor hingeBottomLimit;
    private final TouchSensor extensionTopLimit;
    private final TouchSensor extensionBottomLimit;
    private final PIDFController hingePID;
    private final PIDFController extensionPID;

    public Extension(HardwareMap hardwareMap) {
        hingePID = new PIDFController(10,0.1,0.1,0);
        extensionPID = new PIDFController(10,0.1,0.1,0);

        extension = hardwareMap.get(DcMotorEx.class, "extension");
        extensionHinge = hardwareMap.get(DcMotorEx.class, "armHinge");
        hingeTopLimit = hardwareMap.get(TouchSensor.class, "hingeTopLimit");
        hingeBottomLimit = hardwareMap.get(TouchSensor.class, "hingeBottomLimit");
        extensionTopLimit = hardwareMap.get(TouchSensor.class, "extensionTopLimit");
        extensionBottomLimit = hardwareMap.get(TouchSensor.class, "extensionBottomLimit");

        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extensionHinge.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extensionHinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

    public Action extend(int ticks) {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    extension.setTargetPosition(ticks);

                    extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    extension.setPower(0.8);
                    init = true;
                }

                if (extension.isBusy() && !extensionTopLimit.isPressed()) {
                    return true;
                }

                extension.setPower(0);
                return false;
            }
        };
    }

    public Action retract(int ticks) {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    extension.setTargetPosition(-ticks);

                    extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    extension.setPower(0.8);
                    init = true;
                }

                if (extension.isBusy() && !extensionBottomLimit.isPressed()) {
                    return true;
                }

                extension.setPower(0);
                return false;
            }
        };
    }

    public Action extendAll() {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    extension.setPower(0.8);
                    init = true;
                }

                if (!extensionTopLimit.isPressed()) {
                    return true;
                }

                extension.setPower(0);
                return false;
            }
        };
    }

    public Action retractAll() {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    extension.setPower(1);
                    init = true;
                }

                if (!extensionBottomLimit.isPressed()) {
                    return true;
                }

                extension.setPower(0);
                return false;
            }
        };
    }

    public void moveExtension(double power) {
        if (power < 0 && !extensionBottomLimit.isPressed()) {
            extension.setPower(power);
        } else if (power > 0 && !extensionTopLimit.isPressed()) {
            extension.setPower(power);
        } else {
            extension.setPower(0);
        }
    }

    public Action hingeUp(int ticks) {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    extensionHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    extensionHinge.setTargetPosition(ticks);

                    extensionHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    extensionHinge.setPower(0.3);
                    init = true;
                }

                if (extensionHinge.isBusy() && !hingeTopLimit.isPressed()) {
                    return true;
                }

                extensionHinge.setPower(0);
                return false;
            }
        };
    }

    public Action hingeDown(int ticks) {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    extensionHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    extensionHinge.setTargetPosition(-ticks);

                    extensionHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    extensionHinge.setPower(0.3);
                    init = true;
                }

                if (extensionHinge.isBusy() && !hingeBottomLimit.isPressed()) {
                    return true;
                }

                extensionHinge.setPower(0);
                return false;
            }
        };
    }

    public Action hingeUpAll() {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    extensionHinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    extensionHinge.setPower(0.3);
                    init = true;
                }

                if (!extensionTopLimit.isPressed()) {
                    return true;
                }

                extensionHinge.setPower(0);
                return false;
            }
        };
    }

    public Action hingeDownAll() {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    extensionHinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    extensionHinge.setPower(-0.3);
                    init = true;
                }

                if (!extensionBottomLimit.isPressed()) {
                    return true;
                }

                extensionHinge.setPower(0);
                return false;
            }
        };
    }

    public void hinge(double power) {
        if (power < 0 && !hingeBottomLimit.isPressed()) {
            extensionHinge.setPower(power);
        } else if (power > 0 && !hingeTopLimit.isPressed()) {
            extensionHinge.setPower(power);
        } else {
            extensionHinge.setPower(0);
        }
    }

    public void hingePIDControl(int target) {
        hingePID.setSetPoint(target);
        while (!hingePID.atSetPoint() && !hingeTopLimit.isPressed() && !hingeBottomLimit.isPressed()) {
            double output = hingePID.calculate(extensionHinge.getCurrentPosition(), hingePID.getSetPoint());
            extensionHinge.setVelocity(output);
        }
        extensionHinge.setVelocity(0);
    }

    public void extensionPIDControl(int target) {
        extensionPID.setSetPoint(target);
        while (!extensionPID.atSetPoint() && !extensionTopLimit.isPressed() && !extensionBottomLimit.isPressed()) {
            double output = extensionPID.calculate(extension.getCurrentPosition(), extensionPID.getSetPoint());
            extension.setVelocity(output);
        }
        extension.setVelocity(0);
    }

    public void stopAndReset() {
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extensionHinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
