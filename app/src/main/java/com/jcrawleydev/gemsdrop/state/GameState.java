package com.jcrawleydev.gemsdrop.state;

public interface GameState {
    void start();
    void stop();
    void click(int x, int y);
}
