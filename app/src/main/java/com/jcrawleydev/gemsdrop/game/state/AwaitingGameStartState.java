package com.jcrawleydev.gemsdrop.game.state;


import android.os.Handler;
import android.os.Looper;

import com.jcrawleydev.gemsdrop.game.Game;

public class AwaitingGameStartState extends AbstractGameState{


    public AwaitingGameStartState(Game game){
        super(game);
        game.startGame();
    }


    @Override
    public void start() {
        loadState(GameStateName.LOAD_LEVEL);
        /*
      new Handler(Looper.getMainLooper())
              .postDelayed(()->loadState(GameStateName.GAME_STARTED), 800);

         */
    }

}
