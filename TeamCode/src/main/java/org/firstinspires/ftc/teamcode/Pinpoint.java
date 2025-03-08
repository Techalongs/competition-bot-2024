package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxI2cDeviceSynch;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.util.TypeConversion;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.nio.ByteOrder;

@I2cDeviceType
@DeviceProperties(
        name = "goBILDA® Pinpoint Odometry Computer",
        xmlTag = "goBILDAPinpoint",
        description ="goBILDA® Pinpoint Odometry Computer (IMU Sensor Fusion for 2 Wheel Odometry)"
)
public class Pinpoint extends I2cDeviceSynchDevice<I2cDeviceSynchSimple> {
    public Pinpoint(I2cDeviceSynchSimple i2cDeviceSynchSimple, boolean deviceClientIsOwned) {
        super(i2cDeviceSynchSimple, deviceClientIsOwned);

        this.deviceClient.setI2cAddress(I2cAddr.create7bit(0x31));
        super.registerArmingStateCallback(false);
    }

    @Override
    protected synchronized boolean doInitialize() {
        ((LynxI2cDeviceSynch)(deviceClient)).setBusSpeed(LynxI2cDeviceSynch.BusSpeed.FAST_400K);
        return true;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return "";
    }

    /**
     * This calibrates the Pinpoint by resetting the Pose and recalibrating the IMU.
     * There will be a 300 millisecond delay when calling this method.
     *
     * @throws InterruptedException Takes 300 milliseconds to complete.
     */
    void calibrate() throws InterruptedException {
        deviceClient.write(4, TypeConversion.intToByteArray(2, ByteOrder.LITTLE_ENDIAN));
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        }
    }

    /**
     * Re-initializes the pinpoint and recalibrates it. This should be run in the end of the init
     * phase of an OpMode, as this usually takes a couple seconds to complete.
     * Wait for telemetry to display "Ready to start." before starting the OpMode.
     *
     * @param hardwareMap The HardwareMap containing the pinpoint
     * @param telemetry The telemetry of the OpMode
     * @param name the name of the pinpoint as specified in the hardware map. Typically "pinpoint"
     * @throws InterruptedException Takes a few seconds to complete.
     */
    public static void calibratePinpoint(HardwareMap hardwareMap, Telemetry telemetry, String name) throws InterruptedException {
        telemetry.addLine("DO NOT START");
        telemetry.update();
        hardwareMap.get(Pinpoint.class, name).calibrate();
        telemetry.addLine("Ready to start.");
        telemetry.update();
    }
}
