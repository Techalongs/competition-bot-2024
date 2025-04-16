package org.firstinspires.ftc.teamcode;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HorizontalClawSubsystem extends SubsystemBase {
    private final Servo claw;
    private final Servo wrist;
    private final Servo hinge;

    public HorizontalClawSubsystem(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "horizontalClaw");
        wrist = hardwareMap.get(Servo.class, "horizontalWrist");
        hinge = hardwareMap.get(Servo.class, "horizontalClawHinge");

        claw.setDirection(Servo.Direction.REVERSE);
    }

    public void open() {
        claw.setPosition(Positions.HorizontalClawPosition.OPEN.pos);
    }

    public void loosen() {
        claw.setPosition(Positions.HorizontalClawPosition.LOOSE.pos);
    }

    public void close() {
        claw.setPosition(Positions.HorizontalClawPosition.CLOSE.pos);
    }

    public void wristTo(Positions.HorizontalWristPosition p) {
        wrist.setPosition(p.pos);
    }

    public void hingeTo(Positions.HorizontalHingePosition p) {
        hinge.setPosition(p.pos);

        if (p == Positions.HorizontalHingePosition.UP) {
            loosen();
            close();
        }
    }

    public class OpenHClaw extends CommandBase {
        private final HorizontalClawSubsystem horizontalClawSubsystem;

        public OpenHClaw(HorizontalClawSubsystem subsystem) {
            horizontalClawSubsystem = subsystem;
            addRequirements(horizontalClawSubsystem);
        }

        @Override
        public void initialize() {
            horizontalClawSubsystem.open();
        }

        @Override
        public boolean isFinished() {
            return true;
        }
    }
}
