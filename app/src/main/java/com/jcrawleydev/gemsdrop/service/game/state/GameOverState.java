package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.GameOverAnimator;

public class GameOverState extends AbstractGameState implements GameState{

    private final GameOverAnimator gameOverAnimator;

    public GameOverState(Game game){
        super(game);
        gameOverAnimator = new GameOverAnimator(game, gemGrid);
    }

    @Override
    public void onStart() {
        gameOverAnimator.startGameOverSequence();
    }


}
