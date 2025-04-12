package org.firstinspires.ftc.teamcode.async;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Positions;

public class VerticalArm {

    private final DcMotor extension;
    private final DigitalChannel extensionBottomLimit;
    private final Servo claw;
    private final Servo hinge;

    private static final double DEFAULT_EXTENSION_POWER = 1.0;

    public VerticalArm(HardwareMap hardwareMap) {
        extension = hardwareMap.get(DcMotor.class, "verticalArm");
        extensionBottomLimit = hardwareMap.get(DigitalChannel.class, "bottomArmLimit");
        claw = hardwareMap.get(Servo.class, "verticalClaw");
        hinge = hardwareMap.get(Servo.class, "verticalClawHinge");

        extension.setDirection(DcMotorSimple.Direction.REVERSE);
        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Action moveExtensionTo(Positions.VerticalExtPosition pos) {
        return this.moveExtensionTo(pos, DEFAULT_EXTENSION_POWER);
    }

    public Action moveExtensionTo(Positions.VerticalExtPosition pos, double power) {
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
                    if (extensionBottomLimit.getState()) {
                        extension.setPower(power);
                        return true;
                    }

                    return false;
                } else if (extension.isBusy()) {
                    extension.setPower(power);
                    return true;
                } else {
                    extension.setPower(0);
                    extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    return false;
                }
            }
        };
    }

    public Action openClaw() {
        return telemetryPacket -> {
            claw.setPosition(Positions.VerticalClawPosition.OPEN.pos);
            return false;
        };
    }

    public Action closeClaw() {
        return telemetryPacket -> {
            claw.setPosition(Positions.VerticalClawPosition.CLOSE.pos);
            return false;
        };
    }

    public Action hingeTo(Positions.VerticalHingePosition pos) {
        return new Action() {
            int time = 0;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                Actions.runBlocking(closeClaw());
                if (time++ < 10000) return true;
                hinge.setPosition(pos.pos);
                return false;
            }
        };
    }

    public int getExtensionPosition() {
        return extension.getCurrentPosition();
    }

    public boolean isExtensionAtBottomLimit() {
        return extensionBottomLimit.getState();
    }

    public void logTelemetry(Telemetry telemetry) {
        telemetry.addLine("Vertical Arm - Extension |");
        telemetry.addData("  Power", extension.getPower());
        telemetry.addData("  CurrentPosition", extension.getCurrentPosition());
        telemetry.addData("  TargetPosition", extension.getTargetPosition());
        telemetry.addData("  RunMode", extension.getMode());
        telemetry.addData("  ExtBottomLimit", extensionBottomLimit.getState());
    }
}
