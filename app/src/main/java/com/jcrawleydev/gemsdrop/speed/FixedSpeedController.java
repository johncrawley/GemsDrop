package com.jcrawleydev.gemsdrop.speed;

public class FixedSpeedController implements SpeedController{
    private final int fixedSpeed;

    public FixedSpeedController(int speed){
        fixedSpeed = speed;
    }


    @Override
    public void reset() {
        //do nothing
    }

    @Override
    public int getCurrentSpeed() {
        return fixedSpeed;
    }

    @Override
    public void update() {
        //do nothing
    }
}
