package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Optional;

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
    //     */
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

    /**
     * Runs an action only once even if the action returns true to continue.
     * Copied from Roadrunner's Actions#runBlocking() source and modified to run once.
     *
     * @param a Action to run once
     */
    public static void runOnce(Action a) {
        final FtcDashboard dash = FtcDashboard.getInstance();
        final Canvas c = new Canvas();
        a.preview(c);
        final TelemetryPacket p = new TelemetryPacket();
        Optional.ofNullable(p.fieldOverlay()).ifPresent(canvas -> canvas.getOperations().addAll(c.getOperations()));
        a.run(p);
        dash.sendTelemetryPacket(p);
    }

    /**
     * Run an Action blocking and then sleep for a number of seconds.
     * Note: The sleep is a proper SleepAction.
     *
     * @param a Action
     * @param seconds Seconds to sleep (can be fractional)
     */
    public static void runAndSleep(Action a, double seconds) {
        Actions.runBlocking(
                new SequentialAction(
                        a,
                        new SleepAction(seconds)
                )
        );
    }

    /**
     * Run the action blocking and return how many milliseconds it took.
     *
     * @param action Action to time
     * @return Milliseconds elapsed during run
     */
    public static double timeAction(Action action) {
        return timeAction(action, new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS));
    }

    /**
     * Run the action blocking and return how many milliseconds it took.
     *
     * @param action Action to time
     * @param timer Supplied timer
     * @return Milliseconds elapsed during run
     */
    public static double timeAction(Action action, ElapsedTime timer) {
        timer.reset();
        Actions.runBlocking(action);
        return timer.milliseconds();
    }

}
