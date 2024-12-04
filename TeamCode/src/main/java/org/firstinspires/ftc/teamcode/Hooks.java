package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Hooks {
    private final DcMotorEx leftHook;
    private final DcMotorEx rightHook;
    private final PIDFController leftHookPID;
    private final PIDFController rightHookPID;

    public enum HookPosition {
        STARTING,
        READY,
        HOOK
    }

    public Hooks(HardwareMap hardwareMap) {
        leftHookPID = new PIDFController(10,0.1,0.1,0);
        rightHookPID = new PIDFController(10,0.1,0.1,0);

        leftHook = hardwareMap.get(DcMotorEx.class, "leftHookHinge");
        rightHook = hardwareMap.get(DcMotorEx.class, "rightHookHinge");

        leftHook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightHook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftHook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightHook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftHook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightHook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Action hooksPIDControl(HookPosition pos) {
        return new Action() {
            boolean init = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!init) {
                    int target1 = 5;
                    int target2 = 5;
                    switch (pos) {
                        case STARTING:
                            target1 = 0;
                            target2 = 0;
                            break;
                        case READY:
                            target1 = -400;
                            target2 = 130;
                            break;
                        case HOOK:
                            target1 = -165;
                            target2 = 300;
                            break;
                    }

                    leftHookPID.setSetPoint(target1);
                    rightHookPID.setSetPoint(target2);
                    init = true;
                }

                if (!leftHookPID.atSetPoint() && !rightHookPID.atSetPoint()) {
                    double output1 = leftHookPID.calculate(leftHook.getCurrentPosition(), leftHookPID.getSetPoint());
                    double output2 = rightHookPID.calculate(rightHook.getCurrentPosition(), rightHookPID.getSetPoint());
                    leftHook.setVelocity(output1);
                    rightHook.setVelocity(output2);
                    return true;
                }

                leftHook.setVelocity(0);
                rightHook.setVelocity(0);
                return false;
            }
        };
    }
}
