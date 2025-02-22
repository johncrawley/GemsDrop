package com.jcrawleydev.gemsdrop.service.game.state;

import static com.jcrawleydev.gemsdrop.service.game.state.GameEvent.*;

import com.jcrawleydev.gemsdrop.service.game.Game;

import java.util.Map;

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
        gameOverState = new GameOverState(game);
        gemFreeFallState = new GemFreeFallState(game);
        gemQuickDropState = new GemQuickDropState(game);
        gemsDropState = new GemsDropState(game);
        loadLevelState = new LoadLevelState(game);
    }


    public void sendEvent(GameEvent gameEvent){
       log("entered sendEvent() " + currentGameState + " -> " + gameEvent);
       currentGameState = switch(gameEvent){
           case START_GAME -> gameStartedState;
           case ALL_GEMS_CONNECTED -> evaluateGridState;
           case GEM_REMOVAL_ANIMATION_COMPLETE -> gemRemovalCompletedState;
           case GEMS_REMOVED_FROM_GRID -> gridGravityState;
           case GEM_COLUMN_EXCEEDS_MAXIMUM_HEIGHT -> gameOverState;
           case SOME_GEMS_ARE_CONNECTED -> gemFreeFallState;
           case QUICK_DROP_INITIATED -> gemQuickDropState;
           case DROP_GEMS -> gemsDropState;
           case LOAD_LEVEL -> loadLevelState;
       };
       currentGameState.onStart();
    }


    private void log(String msg){
        System.out.println("^^^ StateManager: " + msg);
    }

}
