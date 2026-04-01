package com.jcrawleydev.gemsdrop.game;

import java.util.Random;

public class RandomBackgroundPicker {

    private final Random random;
    private int currentBackgroundIndex = 0;


    public RandomBackgroundPicker(){
        random = new Random(System.currentTimeMillis());
    }


    public void pickRandomBackgroundIndex(){
        int numberOfBackgrounds = 6;
        currentBackgroundIndex = random.nextInt(numberOfBackgrounds);
    }


    public int getCurrentBackgroundIndex(){
        return currentBackgroundIndex;
    }

}
