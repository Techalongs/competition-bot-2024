package org.firstinspires.ftc.teamcode;

public class Positions {

    public enum HangExtPosition {
        TOP(24300);

        public final int ticks;

        HangExtPosition(int ticks) {
            this.ticks = ticks;
        }
    }

    public enum HorizontalExtPosition {
        BOTTOM(100),
        TOP(1500);

        public final int ticks;

        HorizontalExtPosition(int ticks) {
            this.ticks = ticks;
        }
    }

    public enum HorizontalClawPosition {
        OPEN(0.65),
        LOOSE(0.85),
        CLOSE(0.89);

        public final double pos;

        HorizontalClawPosition(double pos) {
            this.pos = pos;
        }
    }

    public enum HorizontalWristPosition {
        UP(0.87),
        MID(0.73),
        DOWN(0.47);

        public final double pos;

        HorizontalWristPosition(double pos) {
            this.pos = pos;
        }
    }

    public enum HorizontalHingePosition {
        UP(0),
        MID(0.39),
        DOWN(1);

        public final double pos;

        HorizontalHingePosition(double pos) {
            this.pos = pos;
        }
    }

    public enum VerticalExtPosition {
        BOTTOM(-10),
        PARK(-190),
        SPECIMEN_1(-150),
        SPECIMEN_2(-800),
        TOP(-2600);

        public final int ticks;

        VerticalExtPosition(int ticks) {
            this.ticks = ticks;
        }
    }

    public enum VerticalClawPosition {
        OPEN(0.9),
        CLOSE(0.43);

        public final double pos;

        VerticalClawPosition(double pos) {
            this.pos = pos;
        }
    }

    public enum VerticalHingePosition {
        UP(0.9),
        SPECIMEN(0.35),
        PARK(0.45),
        DOWN(0.03);

        public final double pos;

        VerticalHingePosition(double pos) {
            this.pos = pos;
        }
    }

}
