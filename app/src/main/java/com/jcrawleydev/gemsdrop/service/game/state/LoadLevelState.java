package com.jcrawleydev.gemsdrop.service.game.state;

import static com.jcrawleydev.gemsdrop.service.game.state.GameStateName.GEMS_DROP;

import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGemsFactory;
import com.jcrawleydev.gemsdrop.service.game.grid.GridAdder;
import com.jcrawleydev.gemsdrop.service.game.level.LevelFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoadLevelState extends AbstractGameState {

    private final ScheduledExecutorService executorService;
    private final GridAdder gridAdder = new GridAdder();
    private final LevelFactory levelFactory = new LevelFactory();
    private final DroppingGemsFactory droppingGemsFactory;

    public LoadLevelState(Game game){
        super(game);
        executorService = Executors.newSingleThreadScheduledExecutor();
        droppingGemsFactory = game.getDroppingGemsFactory();
    }

    @Override
    public void start() {
        initLevel();
    }

    private void initLevel(){
        var level = levelFactory.getLevel(1);
        game.setCurrentGameLevel(level);
        game.resetDropCount();
        game.setCurrentDropRate(level.startingDropDuration());
        droppingGemsFactory.setSpecialGemConditions(level.specialGemConditions());
        droppingGemsFactory.setGemColors(level.gemColors());
        gridAdder.addTo(gemGrid, level.startingGrid());
        log("initLevel() about to load gems drop state");
        executorService.schedule(()-> loadState(GEMS_DROP),
                1000,
                TimeUnit.MILLISECONDS);
    }



}
