package com.jcrawleydev.gemsdrop.service;

public class Game {

    private GameService gameService;
    private int difficulty;

    public void init(GameService gameService){
        this.gameService = gameService;
    }


    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }


    public void startGame(){

    }

    public void quit(){

    }
}
