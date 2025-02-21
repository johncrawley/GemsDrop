package com.jcrawleydev.gemsdrop.service.game.gem;

import static com.jcrawleydev.gemsdrop.service.audio.SoundEffect.GEM_HITS_FLOOR;

import com.jcrawleydev.gemsdrop.service.audio.SoundEffectManager;
import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.GemMover;
import com.jcrawleydev.gemsdrop.service.game.TaskScheduler;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.service.game.state.GameEvent;
import com.jcrawleydev.gemsdrop.service.game.state.StateManager;

import java.util.Set;

public class DroppingGemsEvaluator {

    private final Game game;
    private final GemMover gemMover;
    private final GemGrid gemGrid;
    private final StateManager stateManager;
    private final SoundEffectManager soundEffectManager;
    private final TaskScheduler taskScheduler;

    public DroppingGemsEvaluator(Game game){
        this.game = game;
        gemMover = game.getGemMover();
        gemGrid = game.getGemGrid();
        stateManager = game.getStateManager();
        soundEffectManager = game.getSoundEffectManager();
        taskScheduler = game.getTaskScheduler();
    }

    public boolean evaluateTouchingGems(DroppingGems droppingGems){
        return droppingGems.containsWonderGem()
                ? evaluateWonderGem(droppingGems)
                : evaluateNormalGems(droppingGems);
    }


    private boolean evaluateNormalGems(DroppingGems droppingGems){
        boolean haveAnyGemsBeenAdded = false;
        droppingGems.addConnectingGemsTo(gemGrid);

        if(droppingGems.areAllAddedToGrid()){
            gemMover.disableControls();
            stateManager.sendEvent(GameEvent.ALL_GEMS_CONNECTED);
            haveAnyGemsBeenAdded = true;
        }
        else if(droppingGems.areAnyAddedToGrid()){
            gemMover.disableControls();
            stateManager.sendEvent(GameEvent.SOME_GEMS_ARE_CONNECTED);
            haveAnyGemsBeenAdded = true;
        }
        if(haveAnyGemsBeenAdded){
            soundEffectManager.playSoundEffect(GEM_HITS_FLOOR);
        }
        return haveAnyGemsBeenAdded;
    }


    private boolean evaluateWonderGem(DroppingGems droppingGems){
        var haveAnyGemsBeenAdded = false;
        var markedGemIds = droppingGems.addWonderGemTo(gemGrid);
        var numberOfMarkedGems = markedGemIds.size();

        if(numberOfMarkedGems > 0){
            taskScheduler.cancelTask();
            long [] ids = getArrayFrom(markedGemIds);
            game.removeGemsFromView(ids);
            game.updateScore(numberOfMarkedGems);
            soundEffectManager.playWonderGemRemovedSound(numberOfMarkedGems);
            haveAnyGemsBeenAdded = true;
        }
        return haveAnyGemsBeenAdded;
    }


    private long[] getArrayFrom(Set<Long> set){
        return set.stream().mapToLong(Long::longValue).toArray();
    }

}
