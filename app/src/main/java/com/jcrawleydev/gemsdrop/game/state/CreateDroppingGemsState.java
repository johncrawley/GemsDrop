package com.jcrawleydev.gemsdrop.game.state;

import com.jcrawleydev.gemsdrop.game.Game;

public class CreateDroppingGemsState  extends AbstractGameState {


    public CreateDroppingGemsState(Game game) {
        super(game);
    }


    @Override
    public void start() {
        game.createDroppingGems();
        loadState(GameStateName.GEMS_DROP);
    }

}