package com.jcrawleydev.gemsdrop.service.game.state;

import static com.jcrawleydev.gemsdrop.service.game.state.GameEvent.*;

import com.jcrawleydev.gemsdrop.service.game.Game;

import java.util.Map;

public class StateManager {

    private Map<GameEvent, AbstractGameState> stateMap;
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

        stateMap = Map.of(START_GAME, new GameStartedState(game),
                ALL_GEMS_CONNECTED, new EvaluateGridState(game),
                GEM_REMOVAL_ANIMATION_COMPLETE, new GemRemovalAnimationDoneState(game),
                GEMS_REMOVED_FROM_GRID, new GridGravityState(game),
                GEM_COLUMN_EXCEEDS_MAXIMUM_HEIGHT, new GameOverState(game),
                SOME_GEMS_ARE_CONNECTED, new GemFreeFallState(game),
                QUICK_DROP_INITIATED, new GemQuickDropState(game),
                DROP_GEMS, new GemsDropState(game));

        currentGameState = stateMap.get(START_GAME);
    }


    public void sendEvent(GameEvent gameEvent){
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


}
