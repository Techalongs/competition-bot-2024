package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HorizontalClaw {
    private final Servo claw;
    private final Servo wrist;
    private final Servo hinge;

    public HorizontalClaw(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "horizontalClaw");
        wrist = hardwareMap.get(Servo.class, "horizontalWrist");
        hinge = hardwareMap.get(Servo.class, "horizontalClawHinge");

        claw.setDirection(Servo.Direction.REVERSE);
    }

    public Action open() {
        return telemetryPacket -> {
            claw.setPosition(Positions.HorizontalClawPosition.OPEN.pos);
            return false;
        };
    }

    public Action loosen() {
        return telemetryPacket -> {
            claw.setPosition(Positions.HorizontalClawPosition.LOOSE.pos);
            return false;
        };
    }

    public Action close() {
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
                claw.setPosition(Positions.HorizontalClawPosition.LOOSE.pos);
                claw.setPosition(Positions.HorizontalClawPosition.CLOSE.pos);
            }

            return false;
        };
    }
}
