package com.jcrawleydev.gemsdrop.game.state;

import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.GameOverAnimator;

public class GameEndingState extends AbstractGameState{

    public GameEndingState(Game game){
        super(game);
    }

    @Override
    public void start() {
        game.saveScore();
        game.getSoundEffectManager().playGameOverSound();
        new GameOverAnimator(game, gemGrid).startGameOverSequence();
    }

}
