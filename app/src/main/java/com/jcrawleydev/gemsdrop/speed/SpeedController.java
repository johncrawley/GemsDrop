package com.jcrawleydev.gemsdrop.speed;

public interface SpeedController {
    void reset();
    int getCurrentSpeed();
    void update();
    int getInterval();
}

