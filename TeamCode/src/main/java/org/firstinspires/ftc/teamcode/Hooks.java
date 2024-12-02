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

    enum HookPosition {
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

    public void hooksPIDControl(HookPosition pos) {
        int target1 = 5;
        int target2 = 5;
        switch (pos) {
            case STARTING:
                target1 = 0;
                target2 = 0;
                break;
            case READY:
                target1 = -330;
                target2 = -315;
                break;
            case HOOK:
                target1 = -180;
                target2 = -160;
                break;
        }

        leftHookPID.setSetPoint(target1);
        rightHookPID.setSetPoint(target2);
        while (!leftHookPID.atSetPoint() && !rightHookPID.atSetPoint()) {
            double output1 = leftHookPID.calculate(leftHook.getCurrentPosition(), leftHookPID.getSetPoint());
            double output2 = rightHookPID.calculate(rightHook.getCurrentPosition(), rightHookPID.getSetPoint());
            leftHook.setVelocity(output1);
            rightHook.setVelocity(output2);
        }
        leftHook.setVelocity(0);
        rightHook.setVelocity(0);
    }
}
