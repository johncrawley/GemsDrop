package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GEMS_DROP;

import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.grid.GridAdder;
import com.jcrawleydev.gemsdrop.game.level.LevelFactory;

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
    }


    @Override
    public void start() {
        game.resetDropCount();
        var level = levelFactory.getLevel(1);
        game.setCurrentGameLevel(level);
        game.getDroppingGemsFactory().setLevel(level);
        gridAdder.addTo(gemGrid, level.startingGrid());

        executorService.schedule(()-> loadState(GEMS_DROP),
                1000,
                TimeUnit.MILLISECONDS);
    }


}
