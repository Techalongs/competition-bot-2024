package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class VerticalClaw {
    private final Servo claw;
    private final Servo hinge;

    public enum Position {
        OPEN(1),
        CLOSE(0.43);

        private final double pos;

        Position(double pos) {
            this.pos = pos;
        }
    }

    public enum HingePosition {
        UP(0.9),
        SPECIMEN(0.35),
        DOWN(0.03);

        private final double pos;

        HingePosition(double pos) {
            this.pos = pos;
        }
    }

    public VerticalClaw(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "verticalClaw");
        hinge = hardwareMap.get(Servo.class, "verticalClawHinge");
    }

    public Action open() {
        return telemetryPacket -> {
            claw.setPosition(Position.OPEN.pos);
            return false;
        };
    }

    public Action close() {
        return telemetryPacket -> {
            claw.setPosition(Position.CLOSE.pos);
            return false;
        };
    }

    public Action hingeTo(HingePosition pos) {
        return new Action() {
            int time = 0;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                Actions.runBlocking(close());
                if (time++ < 10000) return true;
                hinge.setPosition(pos.pos);
                return false;
            }
        };
    }
}
