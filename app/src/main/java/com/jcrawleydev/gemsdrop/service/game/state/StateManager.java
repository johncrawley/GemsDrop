package com.jcrawleydev.gemsdrop.service.game.state;

import static com.jcrawleydev.gemsdrop.service.game.state.GameEvent.*;

import com.jcrawleydev.gemsdrop.service.game.Game;

import java.util.Map;

public class StateManager {

    private Map<GameEvent, AbstractGameState> stateMap;
    private AbstractGameState currentGameState;



    public void init(Game game){
        stateMap = Map.of(START_GAME, new GameStartedState(game),
                ALL_GEMS_CONNECTED, new EvaluateGridState(game),
                GEM_REMOVAL_COMPLETE, new GridGravityState(game),
                GEM_COLUMN_EXCEEDS_MAXIMUM_HEIGHT, new GameOverState(game),
                SOME_GEMS_ARE_CONNECTED, new GemFreeFallState(game),
                QUICK_DROP_INITIATED, new GemQuickDropState(game),
                DROP_GEMS, new GemsDropState(game));

        currentGameState = stateMap.get(START_GAME);
    }


    public void sendEvent(GameEvent gameEvent){
        if(!stateMap.containsKey(gameEvent)){
            return;
        }
        currentGameState = stateMap.get(gameEvent);
    }


}
