package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class VerticalExtensionSubsystem extends SubsystemBase {
    private final DcMotor extension;
    private final DigitalChannel bottomArmLimit;

    public VerticalExtensionSubsystem(HardwareMap hardwareMap) {
        extension = hardwareMap.get(DcMotor.class, "verticalArm");
        bottomArmLimit = hardwareMap.get(DigitalChannel.class, "bottomArmLimit");

        extension.setDirection(DcMotorSimple.Direction.REVERSE);
        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveTo(Positions.VerticalExtPosition pos) {
            boolean init = false;

            while (true) {
                if (!init) {
                    extension.setTargetPosition(pos.ticks);
                    extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    init = true;
                }

                if (pos == Positions.VerticalExtPosition.BOTTOM) {
                    if (bottomArmLimit.getState()) {
                        extension.setPower(1);
                    }

                    break;
                } else if (extension.isBusy()) {
                    extension.setPower(1);
                } else {
                    extension.setPower(0);
                    extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    break;
                }
            }
    }
}
