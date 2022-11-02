package com.jcrawleydev.gemsdrop;

import com.jcrawleydev.gemsdrop.gameState.GameState;
import com.jcrawleydev.gemsdrop.score.Score;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public Score score;
    public boolean isSoundEnabled = false;
    public GameState.Type currentGameState = GameState.Type.BEGIN_NEW_GAME;
}
