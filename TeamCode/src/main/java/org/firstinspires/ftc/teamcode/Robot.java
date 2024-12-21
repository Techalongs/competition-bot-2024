package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
        extension.setPower(power);
    }

    public void openHorizontalClaw() {
        horizontalClaw.setPosition(1);
    }

    public void closeHorizontalClaw() {
        horizontalClaw.setPosition(0);
    }

    public void horizontalHingeUp() {
        horizontalClawHinge.setPosition(0);
    }

    public void horizontalHingeDown() {
        horizontalClawHinge.setPosition(1);
    }

    public void openVerticalClaw() {
        verticalClaw.setPosition(1);
    }

    public void closeVerticalClaw() {
        verticalClaw.setPosition(0);
    }

    public void verticalHingeUp() {
        verticalClawHinge.setPosition(0);
    }

    public void verticalHingeDown() {
        verticalClawHinge.setPosition(1);
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
        telemetry.addData("Horizontal Claw Position", getHorizontalClawPosition());
        telemetry.addData("Horizontal Claw Hinge Position", getHorizontalHingePosition());
        telemetry.addData("Vertical Claw Position", getVerticalClawPosition());
        telemetry.addData("Vertical Claw Hinge Position", getVerticalHingePosition());

        for (String caption : extraData.keySet()) {
            telemetry.addData(caption, extraData.get(caption));
        }

        telemetry.update();
    }
}
