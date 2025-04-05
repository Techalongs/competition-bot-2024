package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

public class VerticalArm {
    private final DcMotor arm;
    private final DigitalChannel bottomArmLimit;

    public final static int ARM_MIN_POSITION = 100;
    public final static int ARM_MAX_POSITION = 2400;

    public VerticalArm(HardwareMap hardwareMap) {
        arm = hardwareMap.get(DcMotor.class, "verticalArm");
        bottomArmLimit = hardwareMap.get(DigitalChannel.class, "bottomArmLimit");

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Action moveArm(double power) {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if ((!isBottomArmLimitPressed() || power > 0)
                        && (arm.getCurrentPosition() < ARM_MAX_POSITION || power < 0))
                    arm.setPower(power);
                else
                    arm.setPower(0);

                if (isBottomArmLimitPressed() && arm.getCurrentPosition() > ARM_MIN_POSITION) {
                    arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }

                return false;
            }
        };
    }

    public boolean isBottomArmLimitPressed() {
        return !bottomArmLimit.getState();
    }
}
