package com.jcrawleydev.gemsdrop.service.game.state;

public enum GameEvent {
    START_GAME,
    LOAD_LEVEL,
    DROP_GEMS,
    GEM_REMOVAL_COMPLETE,
    ALL_GEMS_CONNECTED,
    SOME_GEMS_ARE_CONNECTED,
    QUICK_DROP_INITIATED,
    GEM_COLUMN_EXCEEDS_MAXIMUM_HEIGHT,
}
