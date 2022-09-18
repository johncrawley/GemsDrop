package com.jcrawleydev.gemsdrop.speed;

import android.content.Context;

import com.jcrawleydev.gemsdrop.R;

public class VariableSpeedController implements SpeedController {

        private int startingSpeed;
        private final int maxSpeed;
        private final int speedIncrease;
        private final int numberOfDropsToIncreaseSpeed;
        private int currentSpeed;
        private int currentNumberOfDropsToNextIncrease;


    private VariableSpeedController(Builder builder){
        this.startingSpeed = builder.startingSpeed;
        maxSpeed = builder.maxSpeed;
        speedIncrease = builder.speedIncrease;
        numberOfDropsToIncreaseSpeed =builder.numberOfDropsToIncreaseSpeed;
        reset();
    }


        @Override
        public void reset(){
            currentSpeed = startingSpeed;
            currentNumberOfDropsToNextIncrease = numberOfDropsToIncreaseSpeed;
        }


        @Override
        public int getCurrentSpeed(){
            return currentSpeed;
        }


        @Override
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


    public static class Builder {

        private int startingSpeed;
        private int maxSpeed;
        private int speedIncrease;
        private int numberOfDropsToIncreaseSpeed;

        private Builder(){}

        public static Builder newInstance(){
            return new Builder();
        }

        public Builder startingSpeed(int startingSpeed){
            this.startingSpeed = startingSpeed;
            return this;
        }


        public Builder speedIncrease(int speedIncrease){
            this.speedIncrease = speedIncrease;
            return this;
        }

        public Builder maxSpeed(int maxSpeed){
            this.maxSpeed = maxSpeed;
            return this;
        }

        public Builder numberOfDropsToIncreaseSpeed(int numberOfDropsToIncreaseSpeed){
            this.numberOfDropsToIncreaseSpeed = numberOfDropsToIncreaseSpeed;
            return this;
        }

        public SpeedController build(){
            return new VariableSpeedController(this);
        }

    }

}

