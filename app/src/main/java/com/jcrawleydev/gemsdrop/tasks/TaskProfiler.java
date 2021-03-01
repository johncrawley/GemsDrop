package com.jcrawleydev.gemsdrop.tasks;


public class TaskProfiler {

    private long currentStartTime, currentDuration;
    private int numberOfIterations;


    private long existingAverage;
    private long shortestDuration = Long.MAX_VALUE;
    private long longestDuration;


    public void start(){
        currentStartTime = System.nanoTime();
    }

    public void reset(){
        currentStartTime = 0;
        currentDuration = 0;
        existingAverage = 0;
        shortestDuration = Long.MAX_VALUE;
        longestDuration = 0;
    }

    public void end(){
        currentDuration = System.nanoTime()- currentStartTime;
        numberOfIterations++;
        existingAverage = (existingAverage + currentDuration)/2;
        shortestDuration = Math.min(shortestDuration, currentDuration);
        longestDuration = Math.max(longestDuration, currentDuration);
    }

    public Long getAverage(){
        return existingAverage;
    }
    public int getNumberOfIterations(){
        return this.numberOfIterations;
    }

    public void print(){
        System.out.println("number of iterations: " + numberOfIterations + " average runtime: " + existingAverage + " nanoseconds");
        System.out.println("Shortest duration: " + shortestDuration);
        System.out.println("Longest duration: " + longestDuration);

    }
}
