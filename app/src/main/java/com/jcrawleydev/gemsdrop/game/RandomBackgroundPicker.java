package com.jcrawleydev.gemsdrop.game;

import java.util.Random;

public class RandomBackgroundPicker {

    private final Random random;
    private int currentBackgroundIndex = 0;


    public RandomBackgroundPicker(){
        random = new Random(System.currentTimeMillis());
    }


    public int pickRandomBackgroundIndex(){
        int numberOfBackgrounds = 2;
        currentBackgroundIndex = random.nextInt(numberOfBackgrounds);
        return currentBackgroundIndex;
    }


    public int getCurrentBackgroundIndex(){
        return currentBackgroundIndex;
    }

}
