package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;

public class Robot extends MecanumDrivetrain {
    private final DcMotor arm;
    private final DcMotor hangArm;
    private final DcMotor extension;
    private final Servo horizontalClaw;
    private final Servo horizontalWrist;
    private final Servo horizontalClawHinge;
    private final Servo verticalClaw;
    private final Servo verticalClawHinge;
    private final DigitalChannel bottomArmLimit;
    private final TouchSensor bottomExtensionLimit;
    private final Telemetry telemetry;
    private final HashMap<String, String> extraData = new HashMap<>();

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);

        arm = hardwareMap.get(DcMotor.class, "verticalArm");
        hangArm = hardwareMap.get(DcMotor.class, "hangArm");
        extension = hardwareMap.get(DcMotor.class, "horizontalExtension");
        horizontalClaw = hardwareMap.get(Servo.class, "horizontalClaw");
        horizontalWrist = hardwareMap.get(Servo.class, "horizontalWrist");
        horizontalClawHinge = hardwareMap.get(Servo.class, "horizontalClawHinge");
        verticalClaw = hardwareMap.get(Servo.class, "verticalClaw");
        verticalClawHinge = hardwareMap.get(Servo.class, "verticalClawHinge");
        bottomArmLimit = hardwareMap.get(DigitalChannel.class, "bottomArmLimit");
        bottomExtensionLimit = hardwareMap.get(TouchSensor.class, "bottomExtensionLimit");
        this.telemetry = telemetry;
    }

    public void init() {
        extension.setDirection(DcMotorSimple.Direction.REVERSE);
        // hangArm.setDirection(DcMotorSimple.Direction.REVERSE);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hangArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hangArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hangArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        horizontalClaw.setDirection(Servo.Direction.REVERSE);
    }

    public void moveArm(double power) {
        if ((!isBottomArmLimitPressed() || power > 0) && (arm.getCurrentPosition() < 2400 || power < 0))
            arm.setPower(power);
        else arm.setPower(0);

        if (isBottomArmLimitPressed() && arm.getCurrentPosition() > 100) {
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void moveExtension(double power) {
        if ((!isBottomExtensionLimitPressed() || power > 0) && (extension.getCurrentPosition() < 1500 || power < 0))
            extension.setPower(power);
        else extension.setPower(0);

        if (isBottomExtensionLimitPressed() && extension.getCurrentPosition() > 100) {
            extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void moveHangArm(double power) {
        hangArm.setPower(power);
    }

    public boolean moveHangArmTo(int pos) {
        hangArm.setTargetPosition(pos / 2);
        hangArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (hangArm.getCurrentPosition() > hangArm.getTargetPosition()) {
            hangArm.setPower(1);
            return true;
        } else {
            hangArm.setPower(0);
            return false;
        }
    }

    public void openHorizontalClaw() {
        horizontalClaw.setPosition(0.65); // Full open - 0, 0.35
    }

    public void loosenHorizontalClaw() {
        horizontalClaw.setPosition(0.82); // 0.82
    }

    public void closeHorizontalClaw() {
        horizontalClaw.setPosition(0.89); // 0.85
    }

    public void horizontalWristUp() {
        horizontalWrist.setPosition(0.87);
    }

    public void horizontalWristMid() {
        horizontalWrist.setPosition(0.73); // Previous Down
    }

    public void horizontalWristDown() {
        horizontalWrist.setPosition(0.47);
    }

    public void horizontalHingeUp() {
        horizontalClawHinge.setPosition(0.5);
    }

    public void horizontalHingeMid() {
        horizontalClawHinge.setPosition(0.39);
    }

    public void horizontalHingeDown() {
        horizontalClawHinge.setPosition(1);
    }

    public void openVerticalClaw() {
        verticalClaw.setPosition(0.9);
    }

    public void closeVerticalClaw() {
        verticalClaw.setPosition(0.43);
    }

    public void verticalHingeUp() {
        verticalClawHinge.setPosition(0.9);
    }

    public void verticalHingeMid() {
        verticalClawHinge.setPosition(0.35);
    }

    public void verticalHingeDown() {
        verticalClawHinge.setPosition(0.03);
    }

    public double getArmPower() {
        return arm.getPower();
    }

    public double getHangArmPower() {
        return hangArm.getPower();
    }

    public double getExtensionPower() {
        return extension.getPower();
    }

    public int getArmPosition() {
        return arm.getCurrentPosition();
    }

    public int getHangArmPosition() {
        return hangArm.getCurrentPosition();
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

    public boolean isBottomArmLimitPressed() {
        return !bottomArmLimit.getState();
    }

    public boolean isBottomExtensionLimitPressed() {
        return bottomExtensionLimit.isPressed();
    }

    public void addData(String caption, double value) {
        addData(caption, String.valueOf(value));
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
        telemetry.addData("Arm Hang Power", getHangArmPower());
        telemetry.addData("Arm Position", getArmPosition());
        telemetry.addData("Arm Hang Position", getHangArmPosition());
        telemetry.addData("Extension Position", getExtensionPosition());
        telemetry.addData("Horizontal Position", getHorizontalClawPosition());
        telemetry.addData("Horizontal Wrist Position", getHorizontalWristPosition());
        telemetry.addData("Horizontal Hinge Position", getHorizontalHingePosition());
        telemetry.addData("Vertical Position", getVerticalClawPosition());
        telemetry.addData("Vertical Hinge Position", getVerticalHingePosition());
        telemetry.addData("Bottom Arm Limit", isBottomArmLimitPressed());
        telemetry.addData("Bottom Extension Limit", isBottomExtensionLimitPressed());

        for (String caption : extraData.keySet()) {
            telemetry.addData(caption, extraData.get(caption));
        }

        telemetry.update();
    }
}
