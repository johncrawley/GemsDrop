package com.jcrawleydev.gemsdrop.speed;

public class FixedSpeedController implements SpeedController{
    private final int interval;

    public FixedSpeedController(int interval){
        this.interval = interval;
    }


    @Override
    public void reset() {
        //do nothing
    }

    @Override
    public int getCurrentSpeed() {
        throw new RuntimeException("Use Interval, not speed!");
    }

    @Override
    public void update() {
        //do nothing
    }

    @Override
    public int getInterval() {
        return interval;
    }
}
