package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.util.TeamUtils;

public class MecanumDrivetrain {

    private final DcMotor frontLeft;
    private final DcMotor frontRight;
    private final DcMotor backLeft;
    private final DcMotor backRight;

    public MecanumDrivetrain(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotor.class, "CH1");
        frontRight = hardwareMap.get(DcMotor.class, "CH0");
        backLeft = hardwareMap.get(DcMotor.class, "CH3");
        backRight = hardwareMap.get(DcMotor.class, "CH2");

        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Action drive(Gamepad gamepad, double limiter) {
        float FLPower = (-gamepad.left_stick_y + gamepad.right_stick_x) + gamepad.left_stick_x;
        float FRPower = (-gamepad.left_stick_y - gamepad.right_stick_x) - gamepad.left_stick_x;
        float BLPower = (-gamepad.left_stick_y + gamepad.right_stick_x) - gamepad.left_stick_x;
        float BRPower = (-gamepad.left_stick_y - gamepad.right_stick_x) + gamepad.left_stick_x;

        return telemetryPacket -> {
            frontLeft.setPower(FLPower * limiter);
            frontRight.setPower(FRPower * limiter);
            backLeft.setPower(BLPower * limiter);
            backRight.setPower(BRPower * limiter);

            return false;
        };
    }

    public void driveBlocking(Gamepad gamepad, double limiter) {
        TeamUtils.runOnce(drive(gamepad, limiter));
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
