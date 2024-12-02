package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private final Servo clawServo;
    private final Servo clawHinge;

    public enum ClawPosition {
        OPEN,
        CLOSE
    }

    public enum ClawHingePosition {
        UP,
        DOWN
    }

    public Claw(HardwareMap hardwareMap) {
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        clawHinge = hardwareMap.get(Servo.class, "clawHinge");

        clawServo.setDirection(Servo.Direction.REVERSE);
    }

    public Action openClaw() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                clawServo.setPosition(1);
                return false;
            }
        };
    }

    public Action closeClaw() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                clawServo.setPosition(0);
                return false;
            }
        };
    }

    public ClawPosition getPosition() {
        if (clawServo.getPosition() == 1) return ClawPosition.OPEN;
        else return ClawPosition.CLOSE;
    }

    public Action hingeUp() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                clawHinge.setPosition(0);
                return false;
            }
        };
    }

    public Action hingeDown() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                clawHinge.setPosition(1);
                return false;
            }
        };
    }

    public ClawHingePosition getHingePosition() {
        if (clawHinge.getPosition() == 0) return ClawHingePosition.UP;
        else return ClawHingePosition.DOWN;
    }
}
