package com.jcrawleydev.gemsdrop;


import androidx.lifecycle.ViewModel;

import com.jcrawleydev.gemsdrop.audio.MusicPlayer;
import com.jcrawleydev.gemsdrop.game.GameModel;


public class MainViewModel extends ViewModel {

   public GameModel gameModel = new GameModel();
   public MusicPlayer musicPlayer = new MusicPlayer();

}
