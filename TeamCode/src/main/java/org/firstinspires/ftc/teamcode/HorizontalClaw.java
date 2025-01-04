package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HorizontalClaw {
    private final Servo claw;
    private final Servo wrist;
    private final Servo hinge;

    public enum Position {
        OPEN(1),
        LOOSE(0.05),
        CLOSE(0);

        private final double pos;

        Position(double pos) {
            this.pos = pos;
        }
    }

    public enum WristPosition {
        UP(1),
        DOWN(0.85);

        private final double pos;

        WristPosition(double pos) {
            this.pos = pos;
        }
    }

    public enum HingePosition {
        UP(0),
        PICKUP(0.75),
        DOWN(1);

        private final double pos;

        HingePosition(double pos) {
            this.pos = pos;
        }
    }

    public HorizontalClaw(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "horizontalClaw");
        wrist = hardwareMap.get(Servo.class, "horizontalWrist");
        hinge = hardwareMap.get(Servo.class, "horizontalClawHinge");

        claw.setDirection(Servo.Direction.REVERSE);
    }

    public Action open() {
        return telemetryPacket -> {
            claw.setPosition(Position.OPEN.pos);
            return false;
        };
    }

    public Action loosen() {
        return telemetryPacket -> {
            claw.setPosition(Position.LOOSE.pos);
            return false;
        };
    }

    public Action close() {
        return telemetryPacket -> {
            claw.setPosition(Position.CLOSE.pos);
            return false;
        };
    }

    public Action wristUp() {
        return telemetryPacket -> {
            wrist.setPosition(WristPosition.UP.pos);
            return false;
        };
    }

    public Action wristDown() {
        return telemetryPacket -> {
            wrist.setPosition(WristPosition.DOWN.pos);
            return false;
        };
    }

    public Action hingeTo(HingePosition pos) {
        return telemetryPacket -> {
            hinge.setPosition(pos.pos);

            if (pos == HingePosition.UP) {
                Actions.runBlocking(
                        new SequentialAction(
                                loosen(),
                                close()
                        )
                );
            }

            return false;
        };
    }
}
