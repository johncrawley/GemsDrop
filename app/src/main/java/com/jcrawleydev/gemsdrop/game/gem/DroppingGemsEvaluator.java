package com.jcrawleydev.gemsdrop.game.gem;

import static com.jcrawleydev.gemsdrop.audio.SoundEffect.GEM_HITS_FLOOR;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.EVALUATE_GRID;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GEM_FREE_FALL;

import com.jcrawleydev.gemsdrop.audio.SoundEffectManager;
import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.GemMover;
import com.jcrawleydev.gemsdrop.game.TaskScheduler;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.game.state.GameStateName;
import com.jcrawleydev.gemsdrop.game.state.StateManager;

import java.util.Set;

public class DroppingGemsEvaluator {

    private final Game game;
    private GemMover gemMover;
    private final GemGrid gemGrid;
    private final StateManager stateManager;
    private final SoundEffectManager soundEffectManager;
    private final TaskScheduler taskScheduler;

    public DroppingGemsEvaluator(Game game){
        this.game = game;
        gemGrid = game.getGemGrid();
        stateManager = game.getStateManager();
        soundEffectManager = game.getSoundEffectManager();
        taskScheduler = game.getTaskScheduler();
    }


    public void setGemMover(GemMover gemMover){
        this.gemMover = gemMover;
    }



    public boolean evaluate(DroppingGems droppingGems){
        if(droppingGems instanceof WonderDroppingGem){
            return evaluateWonderGem(droppingGems);
        }
        else{
            return evaluateNormalGems(droppingGems);
        }
    }


    private boolean evaluateNormalGems(DroppingGems droppingGems){
        droppingGems.addConnectingGemsTo(gemGrid);

        if(droppingGems.areAllAddedToGrid()){
            soundEffectManager.playSoundEffect(GEM_HITS_FLOOR);
            gemMover.disableControls();
            loadState(EVALUATE_GRID);
            return true;
        }
        else if(droppingGems.areAnyAddedToGrid()){
            soundEffectManager.playSoundEffect(GEM_HITS_FLOOR);
            gemMover.disableControls();
            loadState(GEM_FREE_FALL);
            return true;
        }
        return false;
    }


    private boolean evaluateWonderGem(DroppingGems droppingGems){
        var markedGemIds = droppingGems.addConnectingGemsTo(gemGrid);
        var numberOfMarkedGems = markedGemIds.size();

        if(numberOfMarkedGems > 0){
            taskScheduler.cancelTask();
            long [] ids = getArrayFrom(markedGemIds);
            game.removeGemsFromView(ids);
            soundEffectManager.playWonderGemRemovedSound(numberOfMarkedGems);
            return true;
        }
        return false;
    }


    private void loadState(GameStateName stateName){
        gemMover.cancelQueuedMovements();
        stateManager.load(stateName, this.getClass().getSimpleName());
    }


    private long[] getArrayFrom(Set<Long> set){
        return set.stream().mapToLong(Long::longValue).toArray();
    }

}
