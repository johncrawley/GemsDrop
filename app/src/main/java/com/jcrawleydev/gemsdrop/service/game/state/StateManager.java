package com.jcrawleydev.gemsdrop.service.game.state;

import static com.jcrawleydev.gemsdrop.service.game.state.GameEvent.*;

import java.util.Map;

public class StateManager {

    private final Map<GameEvent, GameState> stateMap;


    public StateManager(){

        stateMap = Map.of(ALL_GEMS_CONNECTED, new EvaluateGridState(),
                GEM_REMOVAL_COMPLETE, new GridGravityState(),
                GEM_COLUMN_EXCEEDS_MAXIMUM_HEIGHT, new GameOverState(),
                SOME_GEMS_ARE_CONNECTED, new GemFreeFallState(),
                QUICK_DROP_INITIATED, new GemQuickDropState(),
                DROP_GEMS, new GemsDropState());

    }



}
