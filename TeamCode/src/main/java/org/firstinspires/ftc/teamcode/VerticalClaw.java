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

    public VerticalClaw(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "verticalClaw");
        hinge = hardwareMap.get(Servo.class, "verticalClawHinge");
    }

    public Action open() {
        return telemetryPacket -> {
            claw.setPosition(Positions.VerticalClawPosition.OPEN.pos);
            return false;
        };
    }

    public Action close() {
        return telemetryPacket -> {
            claw.setPosition(Positions.VerticalClawPosition.CLOSE.pos);
            return false;
        };
    }

    public Action hingeTo(Positions.VerticalHingePosition p) {
        return new Action() {
            int time = 0;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                Actions.runBlocking(close());
                if (time++ < 10000) return true;
                hinge.setPosition(p.pos);
                return false;
            }
        };
    }
}
