package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class VerticalClawSubsystem extends SubsystemBase {
    private final Servo claw;
    private final Servo hinge;

    public VerticalClawSubsystem(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "verticalClaw");
        hinge = hardwareMap.get(Servo.class, "verticalClawHinge");
    }

    public void open() {
        claw.setPosition(Positions.VerticalClawPosition.OPEN.pos);
    }

    public void close() {
        claw.setPosition(Positions.VerticalClawPosition.CLOSE.pos);
    }

    public void hingeTo(Positions.VerticalHingePosition p) throws InterruptedException {
        close();
        sleep(250);
        hinge.setPosition(p.pos);
    }
}