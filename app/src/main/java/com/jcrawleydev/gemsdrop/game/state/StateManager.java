package com.jcrawleydev.gemsdrop.game.state;

import com.jcrawleydev.gemsdrop.game.Game;

import java.util.function.Consumer;

public class StateManager {

    private AbstractGameState currentGameState;
    AbstractGameState gameStartedState,
            evaluateGridState,
            gridGravityState,
            gemRemovalCompletedState,
            gameOverState,
            gemFreeFallState,
            gemQuickDropState,
            gemsDropState,
            loadLevelState;


    public void init(Game game){
        gameStartedState = new GameStartedState(game);
        evaluateGridState = new EvaluateGridState(game);
        gemRemovalCompletedState = new GemRemovalAnimationDoneState(game);
        gridGravityState = new GridGravityState(game);
        gameOverState = new GameEndingState(game);
        gemFreeFallState = new GemFreeFallState(game);
        gemQuickDropState = new GemQuickDropState(game);
        gemsDropState = new GemsDropState(game);
        loadLevelState = new LoadLevelState(game);
    }


    public void load(GameStateName gameStateName, String caller){
        log("Entered load: state name: " + gameStateName);
       currentGameState = switch(gameStateName){
           case GAME_STARTED -> gameStartedState;
           case EVALUATE_GRID -> evaluateGridState;
           case GEM_REMOVAL_ANIMATION_COMPLETE -> gemRemovalCompletedState;
           case GRID_GRAVITY -> gridGravityState;
           case GAME_OVER -> gameOverState;
           case GEM_FREE_FALL -> gemFreeFallState;
           case GEM_QUICK_DROP -> gemQuickDropState;
           case GEMS_DROP -> gemsDropState;
           case LOAD_LEVEL -> loadLevelState;
       };
       currentGameState.start();
    }

    public void performMovement(Consumer<AbstractGameState> consumer){
        if(currentGameState != null){
            consumer.accept(currentGameState);
        }
    }


    private void log(String msg){
        System.out.println("^^^ StateManager: " + msg);
    }

}
