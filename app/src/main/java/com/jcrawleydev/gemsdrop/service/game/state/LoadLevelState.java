package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.grid.GridAdder;
import com.jcrawleydev.gemsdrop.service.game.level.LevelFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoadLevelState extends AbstractGameState {

    private final ScheduledExecutorService executorService;
    private final GridAdder gridAdder = new GridAdder();
    private final LevelFactory levelFactory = new LevelFactory();

    public LoadLevelState(Game game){
        super(game);
        executorService = Executors.newSingleThreadScheduledExecutor();
        initLevel();
    }


    private void initLevel(){
        var level = levelFactory.getLevel(1);
        game.setCurrentGameLevel(level);
        game.resetDropCount();
        game.setCurrentDropRate(level.startingDropDuration());
        gridAdder.addTo(gemGrid, level.startingGrid());

        executorService.schedule(()-> stateManager.sendEvent(GameEvent.DROP_GEMS),
                1000,
                TimeUnit.MILLISECONDS);
    }



}
