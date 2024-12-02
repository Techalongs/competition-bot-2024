package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Hooks {
    private final DcMotor leftHook;
    private final DcMotor rightHook;

    public Hooks(HardwareMap hardwareMap) {
        leftHook = hardwareMap.get(DcMotor.class, "leftHookHinge");
        rightHook = hardwareMap.get(DcMotor.class, "rightHookHinge");

        leftHook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightHook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftHook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightHook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftHook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightHook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Action hingeDown() {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    leftHook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightHook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                    leftHook.setTargetPosition(-200);
                    rightHook.setTargetPosition(-200);

                    leftHook.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rightHook.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    leftHook.setPower(0.1);
                    rightHook.setPower(0.1);

                    init = true;
                }

                if (leftHook.isBusy() && rightHook.isBusy()) {
                    return true;
                }

                leftHook.setPower(0);
                rightHook.setPower(0);
                return false;
            }
        };
    }

    public Action hingeUp() {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    leftHook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightHook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                    leftHook.setTargetPosition(200);
                    rightHook.setTargetPosition(200);

                    leftHook.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rightHook.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    leftHook.setPower(0.1);
                    rightHook.setPower(0.1);

                    init = true;
                }

                if (leftHook.isBusy() && rightHook.isBusy()) {
                    return true;
                }

                leftHook.setPower(0);
                rightHook.setPower(0);
                return false;
            }
        };
    }
}
