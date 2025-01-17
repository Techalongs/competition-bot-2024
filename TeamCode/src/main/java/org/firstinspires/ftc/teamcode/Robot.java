package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;

public class Robot extends MecanumDrivetrain {
    private final DcMotor arm;
    private final DcMotor extension;
    private final Servo horizontalClaw;
    private final Servo horizontalWrist;
    private final Servo horizontalClawHinge;
    private final Servo verticalClaw;
    private final Servo verticalClawHinge;
    private final Telemetry telemetry;
    private final HashMap<String, String> extraData = new HashMap<>();
    private double speed;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);

        arm = hardwareMap.get(DcMotor.class, "verticalArm");
        extension = hardwareMap.get(DcMotor.class, "horizontalExtension");
        horizontalClaw = hardwareMap.get(Servo.class, "horizontalClaw");
        horizontalWrist = hardwareMap.get(Servo.class, "horizontalWrist");
        horizontalClawHinge = hardwareMap.get(Servo.class, "horizontalClawHinge");
        verticalClaw = hardwareMap.get(Servo.class, "verticalClaw");
        verticalClawHinge = hardwareMap.get(Servo.class, "verticalClawHinge");
        this.telemetry = telemetry;
        this.speed = 0.5;
    }

    public Robot(HardwareMap hardwareMap, Telemetry telemetry, double speed) {
        this(hardwareMap, telemetry);
        this.speed = speed;
    }

    public void init() {
        arm.setDirection(DcMotorSimple.Direction.REVERSE);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        horizontalClaw.setDirection(Servo.Direction.REVERSE);
    }

    public void moveArm(double power) {
        arm.setPower(power);
    }

    public void moveExtension(double power) {
        if (extension.getCurrentPosition() > -1520 || power > 0) extension.setPower(power);
        else extension.setPower(0);
    }

    public void openHorizontalClaw() {
        horizontalClaw.setPosition(0.65); // Full open - 0, 0.35
    }

    public void loosenHorizontalClaw() {
        horizontalClaw.setPosition(0.82); // 0.5
    }

    public void closeHorizontalClaw() {
        horizontalClaw.setPosition(0.85); // 0.55
    }

    public void horizontalWristUp() {
        horizontalWrist.setPosition(1);
    }

    public void horizontalWristMid() {
        horizontalWrist.setPosition(0.8); // Previous Down
    }

    public void horizontalWristDown() {
        horizontalWrist.setPosition(0.55);
    }

    public void horizontalHingeUp() {
        horizontalClawHinge.setPosition(0);
    }

    public void horizontalHingeMid() {
        horizontalClawHinge.setPosition(0.39);
    }

    public void horizontalHingeDown() {
        horizontalClawHinge.setPosition(0.5);
    }

    public void openVerticalClaw() {
        verticalClaw.setPosition(0.57);
    }

    public void closeVerticalClaw() {
        verticalClaw.setPosition(0);
    }

    public void verticalHingeUp() {
        verticalClawHinge.setPosition(0.5);
    }

    public void verticalHingeMid() {
        verticalClawHinge.setPosition(0.35);
    }

    public void verticalHingeDown() {
        verticalClawHinge.setPosition(0.07);
    }

    public double getArmPower() {
        return arm.getPower();
    }

    public double getExtensionPower() {
        return extension.getPower();
    }

    public int getArmPosition() {
        return arm.getCurrentPosition();
    }

    public int getExtensionPosition() {
        return extension.getCurrentPosition();
    }

    public double getHorizontalClawPosition() {
        return horizontalClaw.getPosition();
    }

    public double getHorizontalWristPosition() {
        return horizontalWrist.getPosition();
    }

    public double getHorizontalHingePosition() {
        return horizontalClawHinge.getPosition();
    }

    public double getVerticalClawPosition() {
        return verticalClaw.getPosition();
    }

    public double getVerticalHingePosition() {
        return verticalClawHinge.getPosition();
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
        telemetry.addData("Arm Power", getArmPower());
        telemetry.addData("Extension Power", getExtensionPower());
        telemetry.addData("Arm Position", getArmPosition());
        telemetry.addData("Extension Position", getExtensionPosition());
        telemetry.addData("Horizontal Position", getHorizontalClawPosition());
        telemetry.addData("Horizontal Wrist Position", getHorizontalWristPosition());
        telemetry.addData("Horizontal Hinge Position", getHorizontalHingePosition());
        telemetry.addData("Vertical Position", getVerticalClawPosition());
        telemetry.addData("Vertical Hinge Position", getVerticalHingePosition());

        for (String caption : extraData.keySet()) {
            telemetry.addData(caption, extraData.get(caption));
        }

        telemetry.update();
    }
}
