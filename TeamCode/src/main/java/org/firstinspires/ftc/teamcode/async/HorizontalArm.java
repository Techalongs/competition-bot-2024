package org.firstinspires.ftc.teamcode.async;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.Positions;

public class HorizontalArm {

    private final DcMotor extension;
    private final TouchSensor extensionBottomLimit;
    private final Servo claw;
    private final Servo wrist;
    private final Servo hinge;

    public HorizontalArm(HardwareMap hardwareMap) {
        extension = hardwareMap.get(DcMotor.class, "horizontalExtension");
        claw = hardwareMap.get(Servo.class, "horizontalClaw");
        wrist = hardwareMap.get(Servo.class, "horizontalWrist");
        hinge = hardwareMap.get(Servo.class, "horizontalClawHinge");
        extensionBottomLimit = hardwareMap.get(TouchSensor.class, "bottomExtensionLimit");

        claw.setDirection(Servo.Direction.REVERSE);
    }

    public Action openClaw() {
        return telemetryPacket -> {
            claw.setPosition(Positions.HorizontalClawPosition.OPEN.pos);
            return false;
        };
    }

    public Action loosenClaw() {
        return telemetryPacket -> {
            claw.setPosition(Positions.HorizontalClawPosition.LOOSE.pos);
            return false;
        };
    }

    public Action closeClaw() {
        return telemetryPacket -> {
            claw.setPosition(Positions.HorizontalClawPosition.CLOSE.pos);
            return false;
        };
    }

    public Action wristTo(Positions.HorizontalWristPosition p) {
        return telemetryPacket -> {
            wrist.setPosition(p.pos);
            return false;
        };
    }

    public Action hingeTo(Positions.HorizontalHingePosition p) {
        return telemetryPacket -> {
            hinge.setPosition(p.pos);

            if (p == Positions.HorizontalHingePosition.UP) {
                Actions.runBlocking(
                        new SequentialAction(
                                loosenClaw(),
                                closeClaw()
                        )
                );
            }

            return false;
        };
    }

    public void moveExtensionBlocking(double power) {
        if ((!this.isExtensionBottomLimitPressed() || power > 0) && (extension.getCurrentPosition() < Positions.HorizontalExtPosition.TOP.ticks || power < 0)) {
            extension.setPower(power);
        } else {
            extension.setPower(0);
        }

        if (this.isExtensionBottomLimitPressed() && extension.getCurrentPosition() > Positions.HorizontalExtPosition.BOTTOM.ticks) {
            extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public boolean isExtensionBottomLimitPressed() {
        return extensionBottomLimit.isPressed();
    }
}
