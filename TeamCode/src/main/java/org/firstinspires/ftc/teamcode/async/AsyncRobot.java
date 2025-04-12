package org.firstinspires.ftc.teamcode.async;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Positions;

import java.util.HashMap;

public class AsyncRobot {

    private final MecanumDrivetrain drivetrain;
    private final HangArm hangArm;

    private final DcMotor verticalExtension;
    private final DcMotor horizontalExtension;
    private final Servo horizontalClaw;
    private final Servo horizontalWrist;
    private final Servo horizontalHinge;
    private final Servo verticalClaw;
    private final Servo verticalHinge;
    private final DigitalChannel verticalExtensionBottomLimit;
    private final TouchSensor horizontalExtensionBottomLimit;

    private final Telemetry telemetry;
    private final HashMap<String, String> extraData = new HashMap<>();

    public AsyncRobot(HardwareMap hardwareMap, Telemetry telemetry) {
        drivetrain = new MecanumDrivetrain(hardwareMap);

        hangArm = new HangArm(hardwareMap);

        horizontalExtension = hardwareMap.get(DcMotor.class, "horizontalExtension");
        horizontalExtensionBottomLimit = hardwareMap.get(TouchSensor.class, "bottomExtensionLimit");
        horizontalClaw = hardwareMap.get(Servo.class, "horizontalClaw");
        horizontalHinge = hardwareMap.get(Servo.class, "horizontalClawHinge");
        horizontalWrist = hardwareMap.get(Servo.class, "horizontalWrist");

        verticalExtension = hardwareMap.get(DcMotor.class, "verticalArm");
        verticalExtensionBottomLimit = hardwareMap.get(DigitalChannel.class, "bottomArmLimit");
        verticalClaw = hardwareMap.get(Servo.class, "verticalClaw");
        verticalHinge = hardwareMap.get(Servo.class, "verticalClawHinge");

        this.telemetry = telemetry;
    }

    public void init() {
        horizontalExtension.setDirection(DcMotorSimple.Direction.REVERSE);

        verticalExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        horizontalExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        verticalExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        horizontalExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        verticalExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        horizontalExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        horizontalClaw.setDirection(Servo.Direction.REVERSE);
    }

    public void moveArm(double power) {
        if ((!isVerticalExtensionBottomLimitPressed() || power > 0) && (verticalExtension.getCurrentPosition() < 2400 || power < 0))
            verticalExtension.setPower(power);
        else verticalExtension.setPower(0);

        if (isVerticalExtensionBottomLimitPressed() && verticalExtension.getCurrentPosition() > 100) {
            verticalExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            verticalExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    // horizontal arm move extension
//    public void moveExtension(double power) {
//        if ((!isHorizontalExtensionBottomLimitPressed() || power > 0) && (horizontalExtension.getCurrentPosition() < 1500 || power < 0))
//            horizontalExtension.setPower(power);
//        else horizontalExtension.setPower(0);
//
//        if (isHorizontalExtensionBottomLimitPressed() && horizontalExtension.getCurrentPosition() > 100) {
//            horizontalExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            horizontalExtension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
//    }

    public void openHorizontalClaw() {
        horizontalClaw.setPosition(0.65); // Full open - 0, 0.35
    }

    public void loosenHorizontalClaw() {
        horizontalClaw.setPosition(0.85); // 0.82
    }

    public void closeHorizontalClaw() {
        horizontalClaw.setPosition(0.89); // 0.85
    }

    public void horizontalWristUp() {
        horizontalWrist.setPosition(Positions.HorizontalWristPosition.UP.pos);
    }

    public void horizontalWristMid() {
        horizontalWrist.setPosition(Positions.HorizontalWristPosition.MID.pos);
    }

    public void horizontalWristDown() {
        horizontalWrist.setPosition(Positions.HorizontalWristPosition.DOWN.pos);
    }

    public void horizontalHingeUp() {
        horizontalHinge.setPosition(0.5);
    }

    public void horizontalHingeMid() {
        horizontalHinge.setPosition(0.39);
    }

    public void horizontalHingeDown() {
        horizontalHinge.setPosition(1);
    }

    public void openVerticalClaw() {
        verticalClaw.setPosition(0.9);
    }

    public void closeVerticalClaw() {
        verticalClaw.setPosition(0.43);
    }

    public void verticalHingeUp() {
        verticalHinge.setPosition(0.9);
    }

    public void verticalHingeMid() {
        verticalHinge.setPosition(0.35);
    }

    public void verticalHingeDown() {
        verticalHinge.setPosition(0.03);
    }

    public double getVerticalExtensionPower() {
        return verticalExtension.getPower();
    }

    public double getHorizontalExtensionPower() {
        return horizontalExtension.getPower();
    }

    public int getVerticalExtensionPosition() {
        return verticalExtension.getCurrentPosition();
    }

    public int getHorizontalExtensionPosition() {
        return horizontalExtension.getCurrentPosition();
    }

    public double getHorizontalClawPosition() {
        return horizontalClaw.getPosition();
    }

    public double getHorizontalWristPosition() {
        return horizontalWrist.getPosition();
    }

    public double getHorizontalHingePosition() {
        return horizontalHinge.getPosition();
    }

    public double getVerticalClawPosition() {
        return verticalClaw.getPosition();
    }

    public double getVerticalHingePosition() {
        return verticalHinge.getPosition();
    }

    public boolean isVerticalExtensionBottomLimitPressed() {
        return !verticalExtensionBottomLimit.getState();
    }

    public boolean isHorizontalExtensionBottomLimitPressed() {
        return horizontalExtensionBottomLimit.isPressed();
    }

    public void addData(String caption, double value) {
        addData(caption, String.valueOf(value));
    }

    public void addData(String caption, String value) {
        extraData.put(caption, value);
    }

    public void displayData() {
        telemetry.addData("Status", "Running");
        telemetry.addData("Arm Power", getVerticalExtensionPower());
        telemetry.addData("Extension Power", getHorizontalExtensionPower());
        telemetry.addData("Arm Hang Power", hangArm.getExtensionPower());
        telemetry.addData("Arm Hang Position", hangArm.getExtensionCurrentPosition());
        telemetry.addData("Extension Position", getHorizontalExtensionPosition());
        telemetry.addData("Horizontal Position", getHorizontalClawPosition());
        telemetry.addData("Horizontal Wrist Position", getHorizontalWristPosition());
        telemetry.addData("Horizontal Hinge Position", getHorizontalHingePosition());
        telemetry.addData("Vertical Position", getVerticalClawPosition());
        telemetry.addData("Vertical Hinge Position", getVerticalHingePosition());
        telemetry.addData("Vertical Extension Position", getVerticalExtensionPosition());
        telemetry.addData("Bottom Arm Limit", isVerticalExtensionBottomLimitPressed());
        telemetry.addData("Bottom Extension Limit", isHorizontalExtensionBottomLimitPressed());

        telemetry.addLine("DRIVETRAIN |");
        telemetry.addData("  FL Power", drivetrain.getFLMotorPower());
        telemetry.addData("  FR Power", drivetrain.getFRMotorPower());
        telemetry.addData("  BL Power", drivetrain.getBLMotorPower());
        telemetry.addData("  BR Power", drivetrain.getBRMotorPower());
        telemetry.addData("  FL Position", drivetrain.getFLMotorPosition());
        telemetry.addData("  FR Position", drivetrain.getFRMotorPosition());
        telemetry.addData("  BL Position", drivetrain.getBLMotorPosition());
        telemetry.addData("  BR Position", drivetrain.getBRMotorPosition());

        for (String caption : extraData.keySet()) {
            telemetry.addData(caption, extraData.get(caption));
        }

        telemetry.update();
    }

}
