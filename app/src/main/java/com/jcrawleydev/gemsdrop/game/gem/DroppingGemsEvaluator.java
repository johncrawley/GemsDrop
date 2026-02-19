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


    public void evaluateTouchingGems(DroppingGems droppingGems, Runnable movementRunnable){
       if(droppingGems instanceof WonderDroppingGem){
           evaluateWonderGem(droppingGems, movementRunnable);
       }
       else{
           evaluateNormalGems(droppingGems, movementRunnable);
       }
    }


    private void log(String msg){
        System.out.println("^^^ DroppingGemsEvaluator: " + msg);
    }


    private void evaluateNormalGems(DroppingGems droppingGems, Runnable movementRunnable){
        droppingGems.addConnectingGemsTo(gemGrid);

        if(droppingGems.areAllAddedToGrid()){
            soundEffectManager.playSoundEffect(GEM_HITS_FLOOR);
            gemMover.disableControls();
            loadState(EVALUATE_GRID);
        }
        else if(droppingGems.areAnyAddedToGrid()){
            soundEffectManager.playSoundEffect(GEM_HITS_FLOOR);
            gemMover.disableControls();
            loadState(GEM_FREE_FALL);
        }
        else{
            runMovement(movementRunnable);
        }
    }


    private void evaluateWonderGem(DroppingGems droppingGems, Runnable movementRunnable){
        var markedGemIds = droppingGems.addConnectingGemsTo(gemGrid);
        var numberOfMarkedGems = markedGemIds.size();

        if(numberOfMarkedGems > 0){
            taskScheduler.cancelTask();
            long [] ids = getArrayFrom(markedGemIds);
            game.removeGemsFromView(ids);
            game.updateScore(numberOfMarkedGems);
            soundEffectManager.playWonderGemRemovedSound(numberOfMarkedGems);
        }
        else{
            runMovement(movementRunnable);
        }
    }


    private void loadState(GameStateName stateName){
        gemMover.cancelQueuedMovements();
        stateManager.load(stateName, this.getClass().getSimpleName());
    }


    private void runMovement(Runnable movementRunnable){
        movementRunnable.run();
        game.updateDroppingGemsOnView();
        gemMover.enableMovement();
        gemMover.attemptNextQueuedMovement();
    }


    private long[] getArrayFrom(Set<Long> set){
        return set.stream().mapToLong(Long::longValue).toArray();
    }

}
