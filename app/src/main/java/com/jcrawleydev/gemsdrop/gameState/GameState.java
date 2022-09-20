package com.jcrawleydev.gemsdrop.gameState;

public interface GameState {
    enum Type{DROP, EVAL, GRID_GRAVITY, FREE_FALL, FLICKER, HEIGHT_EXCEEDED, QUICK_DROP, GAME_OVER}
    void start();
    void stop();
}
