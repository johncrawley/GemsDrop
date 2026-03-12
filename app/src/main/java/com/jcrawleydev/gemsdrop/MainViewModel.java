package com.jcrawleydev.gemsdrop;


import androidx.lifecycle.ViewModel;

import com.jcrawleydev.gemsdrop.audio.MusicPlayer;
import com.jcrawleydev.gemsdrop.game.GameModel;
import com.jcrawleydev.gemsdrop.game.score.HighScores;


public class MainViewModel extends ViewModel {
    public HighScores highScores = new HighScores();
    public GameModel gameModel = new GameModel(highScores);
    public MusicPlayer musicPlayer = new MusicPlayer();
}
