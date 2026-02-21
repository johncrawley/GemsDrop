package com.jcrawleydev.gemsdrop.game.state;

import com.jcrawleydev.gemsdrop.game.Game;

public class StateManager {

    private AbstractGameState currentGameState;

    private AbstractGameState awaitingGameStartedState,
            gameStartedState,
            evaluateGridState,
            gridGravityState,
            gemRemovalCompletedState,
            gameOverState,
            gemFreeFallState,
            gemQuickDropState,
            gemsDropState,
            loadLevelState;


    public void init(Game game, GameStateName existingStateName){
        awaitingGameStartedState = new AwaitingGameStartState(game);
        gameStartedState = new GameStartedState(game);
        evaluateGridState = new EvaluateGridState(game);
        gemRemovalCompletedState = new GemRemovalAnimationDoneState(game);
        gridGravityState = new GridGravityState(game);
        gameOverState = new GameEndingState(game);
        gemFreeFallState = new GemFreeFallState(game);
        gemQuickDropState = new GemQuickDropState(game);
        gemsDropState = new GemsDropState(game);
        loadLevelState = new LoadLevelState(game);
        load(existingStateName, "StateManager.init()");
    }


    public void load(GameStateName gameStateName, String caller){
        log("Entered load: state name: " + gameStateName);
       currentGameState = switch(gameStateName){
           case AWAITING_GAME_START -> awaitingGameStartedState;
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


    public AbstractGameState getCurrentGameState(){
        return currentGameState;
    }


    private void log(String msg){
        System.out.println("^^^ StateManager: " + msg);
    }

}
