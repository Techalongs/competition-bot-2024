package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(16.5,15.75)
                .setStartPose(new Pose2d(11.25, 35.04, Math.toRadians(270)))
                .build();

        // OLD SPECIMEN
//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-16, 64, Math.toRadians(90)))
//                .strafeTo(new Vector2d(3, 32))
//                .strafeTo(new Vector2d(-30, 43))
//                .splineToConstantHeading(new Vector2d(-45, 10), Math.toRadians(180))
//                .strafeTo(new Vector2d(-45, 56.5))
//                .strafeTo(new Vector2d(2, 32))
//                .strafeTo(new Vector2d(-30, 43))
//                .splineToConstantHeading(new Vector2d(-55, 10), Math.toRadians(180))
//                .strafeTo(new Vector2d(-55, 56.5))
//                .strafeTo(new Vector2d(1, 32))
//                .strafeTo(new Vector2d(-30, 43))
//                .splineToConstantHeading(new Vector2d(-60, 10), Math.toRadians(180))
//                .strafeTo(new Vector2d(-61, 56.5))
//                .strafeTo(new Vector2d(1, 32))
//                .strafeTo(new Vector2d(-45, 56.5))
//                .strafeTo(new Vector2d(0, 32))
//                .strafeTo(new Vector2d(-45, 57))
//                .build());

        // 62.5 - hits wall - SPECIMEN
//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-16, 64, Math.toRadians(90)))
//                .strafeTo(new Vector2d(3, 32))
//                .strafeTo(new Vector2d(-30, 43))
//                .splineToConstantHeading(new Vector2d(-45, 10), Math.toRadians(180))
//                .strafeTo(new Vector2d(-45, 53))
//                .strafeTo(new Vector2d(-45, 10))
//                .strafeTo(new Vector2d(-55, 10))
//                .strafeTo(new Vector2d(-55, 53))
//                .strafeTo(new Vector2d(-55, 10))
//                .strafeTo(new Vector2d(-61, 10))
//                .strafeTo(new Vector2d(-61, 53))
//                .strafeTo(new Vector2d(-40, 62.5)) // Begin to hang specimens
//                .strafeTo(new Vector2d(2, 32))
//                .strafeTo(new Vector2d(-40, 62.5))
//                .strafeTo(new Vector2d(2, 32))
//                .strafeTo(new Vector2d(-40, 62.5))
//                .strafeTo(new Vector2d(2, 32))
//                .strafeTo(new Vector2d(-40, 62.5))
//                .strafeTo(new Vector2d(2, 32))
//                .build());

        // SAMPLE
//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(10, 64, Math.toRadians(90)))
//                .strafeTo(new Vector2d(3, 32))
//                .strafeTo(new Vector2d(58, 48))
//                .turn(Math.toRadians(135))
//                .turn(Math.toRadians(45))
//                .strafeTo(new Vector2d(45, 30))
//                .strafeTo(new Vector2d(58, 48))
//                .turn(Math.toRadians(-45))
//                .turn(Math.toRadians(45))
//                .strafeTo(new Vector2d(57, 30))
//                .strafeTo(new Vector2d(58, 48))
//                .turn(Math.toRadians(-45))
//                .turn(Math.toRadians(45))
//                .strafeTo(new Vector2d(61, 30))
//                .strafeTo(new Vector2d(58, 48))
//                .turn(Math.toRadians(-45))
//                .splineTo(new Vector2d(20, 10), Math.toRadians(180))
//                .build());


        // TESTING
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-16, 64, Math.toRadians(90)))
                .strafeTo(new Vector2d(1, 64))
                .strafeTo(new Vector2d(1, 10))
                .strafeTo(new Vector2d(1, 30))
                .strafeTo(new Vector2d(-40, 30))
                .strafeTo(new Vector2d(-40, 64))
                .strafeTo(new Vector2d(-40, 30))
                .strafeTo(new Vector2d(0, 30))
                .strafeTo(new Vector2d(0, 10))
                .build());

        // IDK
        /*
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(11.25, 35.04, Math.toRadians(266.77)))
                .lineToX(30)
                .turn(Math.toRadians(90))
                .lineToY(30)
                .turn(Math.toRadians(90))
                .lineToX(0)
                .turn(Math.toRadians(90))
                .lineToY(0)
                .turn(Math.toRadians(90))
                .build());
         */

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}