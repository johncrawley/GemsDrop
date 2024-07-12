package com.jcrawleydev.gemsdrop.service;

public class GamePreferenceManager {

    private GameService gameService;

    public GamePreferenceManager(GameService gameService){
        this.gameService = gameService;
    }

    public int getLevel(){
        return 0;
    }


    public void saveLevel(int value){

    }
}
