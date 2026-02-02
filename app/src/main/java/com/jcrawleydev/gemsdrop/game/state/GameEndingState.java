package com.jcrawleydev.gemsdrop.game.state;

import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.GameOverAnimator;

public class GameEndingState extends AbstractGameState{

    private final GameOverAnimator gameOverAnimator;

    public GameEndingState(Game game){
        super(game);
        gameOverAnimator = new GameOverAnimator(game, gemGrid);
    }

    @Override
    public void start() {
        gameComponents.saveScore();
        gameOverAnimator.startGameOverSequence();
    }



}
