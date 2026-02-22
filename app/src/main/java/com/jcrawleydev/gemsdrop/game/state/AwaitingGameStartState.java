package com.jcrawleydev.gemsdrop.game.state;


import android.os.Handler;
import android.os.Looper;

import com.jcrawleydev.gemsdrop.game.Game;

public class AwaitingGameStartState extends AbstractGameState{


    public AwaitingGameStartState(Game game){
        super(game);
    }


    @Override
    public void start() {
      // var handler = new Handler(Looper.getMainLooper());
      // handler.postDelayed(()->loadState(GameStateName.GAME_STARTED), 800);
    }

}
