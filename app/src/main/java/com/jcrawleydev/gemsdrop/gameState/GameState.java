package com.jcrawleydev.gemsdrop.gameState;

public interface GameState {
    enum Type{CREATE_NEW_GEMS, DROP, EVALUATE_GRID, GRID_GRAVITY, FREE_FALL, FLICKER, HEIGHT_EXCEEDED, QUICK_DROP, BEGIN_NEW_GAME, GAME_OVER}
    void start();
    void stop();
    GameState.Type getStateType();
}
