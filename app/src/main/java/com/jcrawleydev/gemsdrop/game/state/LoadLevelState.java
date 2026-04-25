package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.CREATE_GEMS;

import com.jcrawleydev.gemsdrop.audio.SoundEffect;
import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.grid.GridAdder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoadLevelState extends AbstractGameState {

    private final ScheduledExecutorService executorService;
    private final GridAdder gridAdder = new GridAdder();

    public LoadLevelState(Game game){
        super(game);
        executorService = Executors.newSingleThreadScheduledExecutor();
    }


    @Override
    public void start() {
        game.resetDropCount();
        var level = game.getLevel();
        game.setCurrentGameLevel(level);
        game.getDroppingGemsFactory().setLevel(level);
        gridAdder.addTo(gemGrid, level.startingGrid());
        game.createGridGemsOnView();

        soundEffectManager.play(SoundEffect.SILENCE); //workaround for delay in first sound effect played
        executorService.schedule(()-> loadState(CREATE_GEMS),
                1000,
                TimeUnit.MILLISECONDS);
    }


}
