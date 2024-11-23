package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;

public class Robot implements MecanumDrivetrain {
    private final DcMotor frontLeft;
    private final DcMotor frontRight;
    private final DcMotor backLeft;
    private final DcMotor backRight;
    private final DcMotor extensionHinge;
    private final DcMotor extension;
    private final Servo clawServo;
    private final Servo clawHinge;
    private final Telemetry telemetry;
    private final LinearOpMode opMode;
    private final HashMap<String, String> extraData = new HashMap<>();
    private double speed;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode opMode) {
        frontLeft = hardwareMap.get(DcMotor.class, "CH1");
        frontRight = hardwareMap.get(DcMotor.class, "CH0");
        backLeft = hardwareMap.get(DcMotor.class, "CH3");
        backRight = hardwareMap.get(DcMotor.class, "CH2");
        extensionHinge = hardwareMap.get(DcMotor.class, "armHinge");
        extension = hardwareMap.get(DcMotor.class, "extension");
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        clawHinge = hardwareMap.get(Servo.class, "clawHinge");

        this.telemetry = telemetry;
        this.opMode = opMode;
        this.speed = 0.5;
    }

    public Robot(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode opMode, double speed) {
        this(hardwareMap, telemetry, opMode);
        this.speed = speed;
    }

    public void init() {
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        clawServo.setDirection(Servo.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extensionHinge.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extensionHinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
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

    private void move(int tick1, int tick2, int tick3, int tick4) {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setTargetPosition(tick1);
        frontRight.setTargetPosition(tick2);
        backLeft.setTargetPosition(tick3);
        backRight.setTargetPosition(tick4);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
        backRight.setPower(speed);

        while (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy() && opMode.opModeIsActive()) {
            displayData();
        }

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    @Override
    public void moveForward(int ticks) {
        move(ticks, ticks, ticks, ticks);
    }

    @Override
    public void moveBackward(int ticks) {
        moveForward(-ticks);
    }

    @Override
    public void turnLeft(int ticks) {
        move(-ticks, ticks, -ticks, ticks);
    }

    @Override
    public void turnRight(int ticks) {
        turnLeft(-ticks);
    }

    @Override
    public void strafeLeft(int ticks) {
        move(-ticks, ticks, ticks, -ticks);
    }

    @Override
    public void strafeRight(int ticks) {
        strafeLeft(-ticks);
    }

    public void hingeArm(double power) {
        extensionHinge.setPower(power);
    }

    public void hingeArm(int ticks) {
        extensionHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionHinge.setTargetPosition(ticks);

        extensionHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extensionHinge.setPower(speed);

        while (extensionHinge.isBusy() && opMode.opModeIsActive()) {
            displayData();
        }

        stopHinge();
    }

    public void stayHinge(int stayPosition) {
        if (getExtensionHingePosition() < stayPosition) extensionHinge.setPower(0.2);
        else extensionHinge.setPower(0.01);
    }

    public void stopHinge() {
        extensionHinge.setPower(0);
    }

    public void moveExtension(double power) {
        extension.setPower(power);
    }

    public void extendExtension(int ticks) {
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setTargetPosition(ticks);

        extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extension.setPower(speed);

        while (extension.isBusy() && opMode.opModeIsActive()) {
            displayData();
        }

        extension.setPower(0);
    }

    public void retractExtension(int ticks) {
        extendExtension(-ticks);
    }

    public void openClaws() {
        clawServo.setPosition(1);
    }

    public void closeClaws() {
        clawServo.setPosition(0);
    }

    public void clawHingeUp() {
        clawHinge.setPosition(0);
    }

    public void clawHingeDown() {
        clawHinge.setPosition(1);
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

    public double getArmHingePower() {
        return extensionHinge.getPower();
    }

    public double getExtensionPower() {
        return extension.getPower();
    }

    public int getExtensionHingePosition() {
        return extensionHinge.getCurrentPosition();
    }

    public int getExtensionPosition() {
        return extension.getCurrentPosition();
    }

    public double getClawPosition() {
        return clawServo.getPosition();
    }

    public double getClawHingePosition() {
        return clawHinge.getPosition();
    }

    public void addData(String caption, double value) {
        addData(caption, String.valueOf(value));
    }

    public void changeSpeed(double speed) {
        this.speed = speed;
    }

    public void addData(String caption, String value) {
        extraData.put(caption, value);
    }

    public void displayData() {
        telemetry.addData("Status", "Running");
        telemetry.addData("Front Left Power", getFLMotorPower());
        telemetry.addData("Front Right Power", getFRMotorPower());
        telemetry.addData("Back Left Power", getBLMotorPower());
        telemetry.addData("Back Right Power", getBRMotorPower());
        telemetry.addData("Front Left Position", getFLMotorPosition());
        telemetry.addData("Front Right Position", getFRMotorPosition());
        telemetry.addData("Back Left Position", getBLMotorPosition());
        telemetry.addData("Back Right Position", getBRMotorPosition());
        telemetry.addData("Arm Hinge Power", getArmHingePower());
        telemetry.addData("Extension Power", getExtensionPower());
        telemetry.addData("Extension Hinge Position", getExtensionHingePosition());
        telemetry.addData("Extension Position", getExtensionPosition());
        telemetry.addData("Left Claw Position", getClawPosition());
        telemetry.addData("Claw Hinge Position", getClawHingePosition());

        for (String caption : extraData.keySet()) {
            telemetry.addData(caption, extraData.get(caption));
        }

        telemetry.update();
    }
}
