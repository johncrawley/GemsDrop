package com.jcrawleydev.gemsdrop.gemgroup;

import android.content.Context;

import com.jcrawleydev.gemsdrop.R;

public class SpeedController {

    private final Context context;
    private final int startingSpeed;
    private final int maxSpeed;
    private final int speedIncrease;
    private final int numberOfDropsToIncreaseSpeed;
    private int currentSpeed;
    private int currentNumberOfDropsToNextIncrease;

    public SpeedController(Context context){
        this.context = context;
        startingSpeed = getInt(R.integer.starting_speed);
        maxSpeed = getInt(R.integer.max_speed);
        speedIncrease = getInt(R.integer.speed_increase);
        numberOfDropsToIncreaseSpeed = getInt(R.integer.number_of_drops_to_increase_speed);
        reset();
    }


    public void reset(){
        currentSpeed = startingSpeed;
        currentNumberOfDropsToNextIncrease = numberOfDropsToIncreaseSpeed;
    }


    public int getCurrentSpeed(){
        return currentSpeed;
    }


    public void update(){
        if(currentSpeed >= maxSpeed){
            currentSpeed = maxSpeed;
        }
        currentNumberOfDropsToNextIncrease --;
        if(currentNumberOfDropsToNextIncrease < 1){
            currentNumberOfDropsToNextIncrease = numberOfDropsToIncreaseSpeed;
            currentSpeed += speedIncrease;
        }
    }


    private int getInt(int resId){
        return context.getResources().getInteger(resId);
    }
}
