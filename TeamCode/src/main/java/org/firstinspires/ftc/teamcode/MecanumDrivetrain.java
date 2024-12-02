package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumDrivetrain {
    private final DcMotor frontLeft;
    private final DcMotor frontRight;
    private final DcMotor backLeft;
    private final DcMotor backRight;

    public MecanumDrivetrain(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotor.class, "CH2");
        frontRight = hardwareMap.get(DcMotor.class, "CH3");
        backLeft = hardwareMap.get(DcMotor.class, "CH0");
        backRight = hardwareMap.get(DcMotor.class, "CH1");

        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void drive(double limiter, Gamepad gamepad, double y) {
        float FLPower = (-gamepad.left_stick_y + gamepad.right_stick_x) + gamepad.left_stick_x;
        float FRPower = (-gamepad.left_stick_y - gamepad.right_stick_x) - gamepad.left_stick_x;
        float BLPower = (-gamepad.left_stick_y + gamepad.right_stick_x) - gamepad.left_stick_x;
        float BRPower = (-gamepad.left_stick_y - gamepad.right_stick_x) + gamepad.left_stick_x;

        frontLeft.setPower(FLPower * limiter);
        frontRight.setPower(FRPower * limiter);
        backLeft.setPower(BLPower * limiter);
        backRight.setPower(BRPower * limiter);
    }

    public double getFLMotorPower() {
        return frontLeft.getPower();
    }

    public double getFRMotorPower() {
        return frontRight.getPower();
    }

    public double getBLMotorPower() {
        return backLeft.getPower();
    }

    public double getBRMotorPower() {
        return backRight.getPower();
    }

    public int getFLMotorPosition() {
        return frontLeft.getCurrentPosition();
    }

    public int getFRMotorPosition() {
        return frontRight.getCurrentPosition();
    }

    public int getBLMotorPosition() {
        return backLeft.getCurrentPosition();
    }

    public int getBRMotorPosition() {
        return backRight.getCurrentPosition();
    }
}
