package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public interface MecanumDrivetrain {

    void drive(double limiter, Gamepad gamepad, double y);
    void moveForward(int ticks);
    void moveBackward(int ticks);
    void turnLeft(int ticks);
    void turnRight(int ticks);
    void strafeLeft(int ticks);
    void strafeRight(int ticks);
}
