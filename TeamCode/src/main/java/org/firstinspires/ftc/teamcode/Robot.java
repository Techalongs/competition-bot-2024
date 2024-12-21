package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;

public class Robot extends MecanumDrivetrain {
    private final DcMotor extensionHinge;
    private final DcMotor extension;
    private final DcMotorEx leftHookHinge;
    private final DcMotorEx rightHookHinge;
    private final Servo clawServo;
    private final Servo clawHinge;
    private final TouchSensor hingeTopLimit;
    private final Telemetry telemetry;
    private final LinearOpMode opMode;
    private final HashMap<String, String> extraData = new HashMap<>();
    private double speed;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode opMode) {
        super(hardwareMap);

        extensionHinge = hardwareMap.get(DcMotor.class, "armHinge");
        extension = hardwareMap.get(DcMotor.class, "extension");
        leftHookHinge = hardwareMap.get(DcMotorEx.class, "leftHookHinge");
        rightHookHinge = hardwareMap.get(DcMotorEx.class, "rightHookHinge");
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        clawHinge = hardwareMap.get(Servo.class, "clawHinge");
        hingeTopLimit = hardwareMap.get(TouchSensor.class, "hingeTopLimit");
        this.telemetry = telemetry;
        this.opMode = opMode;
        this.speed = 0.5;
    }

    public Robot(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode opMode, double speed) {
        this(hardwareMap, telemetry, opMode);
        this.speed = speed;
    }

    public void init() {
        clawServo.setDirection(Servo.Direction.REVERSE);

        extensionHinge.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftHookHinge.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightHookHinge.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        extensionHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftHookHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightHookHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    public void hingeArm(double power) {
        extensionHinge.setPower(power);
    }

//    public void hingeArm(double power) {
//        if (power < 0 && !hingeBottomLimit.isPressed()) {
//            extensionHinge.setPower(power);
//        } else if (power > 0 && !hingeTopLimit.isPressed()) {
//            extensionHinge.setPower(power);
//        } else {
//            extensionHinge.setPower(0);
//        }
//    }
//
//    public void hingeArm(int ticks) {
//        extensionHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        extensionHinge.setTargetPosition(ticks);
//
//        extensionHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        extensionHinge.setPower(speed);
//
//        while (extensionHinge.isBusy() && opMode.opModeIsActive()) {
//            displayData();
//        }
//
//        stopHinge();
//    }
//
//    public void stayHinge(int stayPosition) {
//        if (getExtensionHingePosition() < stayPosition && !extensionBottomLimit.isPressed()) extensionHinge.setPower(-0.01);
//        else if (!extensionTopLimit.isPressed()) extensionHinge.setPower(0.01);
//        else extensionHinge.setPower(0);
//    }
//
//    public void stopHinge() {
//        extensionHinge.setPower(0);
//    }

    public void moveExtension(double power) {
        extension.setPower(power);
    }
//
//    public void moveExtension(double power) {
//        if (power < 0 && !extensionBottomLimit.isPressed()) {
//            extension.setPower(power);
//        } else if (power > 0 && !extensionTopLimit.isPressed()) {
//            extension.setPower(power);
//        } else {
//            extension.setPower(0);
//        }
//    }
//
//    public void extendExtension(int ticks) {
//        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        extension.setTargetPosition(ticks);
//
//        extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        extension.setPower(speed);
//
//        while (extension.isBusy() && opMode.opModeIsActive()) {
//            displayData();
//        }
//
//        extension.setPower(0);
//    }

//    public void retractExtension(int ticks) {
//        extendExtension(-ticks);
//    }

    public void hingeHooksReady() {
        leftHookHinge.setTargetPosition(-400);
        rightHookHinge.setTargetPosition(130);

        leftHookHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightHookHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftHookHinge.setPower(0.1);
        rightHookHinge.setPower(0.1);

        while (leftHookHinge.isBusy() && rightHookHinge.isBusy() && opMode.opModeIsActive()) {
            this.displayData();
        }

        leftHookHinge.setPower(0);
        rightHookHinge.setPower(0);
    }

    public void hingeHooks() {
        leftHookHinge.setTargetPosition(-165);
        rightHookHinge.setTargetPosition(300);

        leftHookHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightHookHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftHookHinge.setPower(0.1);
        rightHookHinge.setPower(0.1);

        while (leftHookHinge.isBusy() && rightHookHinge.isBusy() && opMode.opModeIsActive()) {
            this.displayData();
        }

        leftHookHinge.setPower(0);
        rightHookHinge.setPower(0);
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
//
//    public double getArmHingePower() {
//        return extensionHinge.getPower();
//    }
//
//    public double getExtensionPower() {
//        return extension.getPower();
//    }

    public double getLeftHookHingePower() {
        return leftHookHinge.getPower();
    }

    public double getRightHookHingePower() {
        return rightHookHinge.getPower();
    }

    public int getExtensionHingePosition() {
        return extensionHinge.getCurrentPosition();
    }

    public int getExtensionPosition() {
        return extension.getCurrentPosition();
    }

    public int getLeftHookHingePosition() {
        return leftHookHinge.getCurrentPosition();
    }

    public int getRightHookHingePosition() {
        return rightHookHinge.getCurrentPosition();
    }

    public double getClawPosition() {
        return clawServo.getPosition();
    }

    public double getClawHingePosition() {
        return clawHinge.getPosition();
    }

    public boolean isHingeTopLimitPressed() {
        return hingeTopLimit.isPressed();
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
        telemetry.addData("Left Hook Hinge Power", getLeftHookHingePower());
        telemetry.addData("Right Hook Hinge Power", getRightHookHingePower());
        telemetry.addData("Extension Hinge Position", getExtensionHingePosition());
        telemetry.addData("Extension Position", getExtensionPosition());
        telemetry.addData("Left Hook Hinge Position", getLeftHookHingePosition());
        telemetry.addData("Right Hook Hinge Position", getRightHookHingePosition());
        telemetry.addData("Claw Position", getClawPosition());
        telemetry.addData("Claw Hinge Position", getClawHingePosition());
        telemetry.addData("Top Hinge Limit", isHingeTopLimitPressed());

        for (String caption : extraData.keySet()) {
            telemetry.addData(caption, extraData.get(caption));
        }

        telemetry.update();
    }
}
