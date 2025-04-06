package org.firstinspires.ftc.teamcode.util;

/**
 * Shared team utilities.
 */
public class TeamUtils {

    /**
     * Clamp a value inclusively within min and max. For example, to
     * keep power values between -1 and 1.
     *
     * @param value Value to be clamped
     * @param min Minimum value (inclusive)
     * @param max Maximum vale (inclusive)
     * @return Clamped value
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Square power input to allow for more gentle movements.
     *
     * @param input Power level
     * @return Squred input
     */
    public static double squareInput(double input) {
        return Math.signum(input) * input * input;
    }

}
