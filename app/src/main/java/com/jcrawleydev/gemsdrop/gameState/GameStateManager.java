package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.speed.SpeedController;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

public interface  GameStateManager {

    void loadState(GameState.Type type);
    SpeedController getSpeedController();
    GemGroup getGemGroup();
    GemGridLayer getGemGridLayer();
    GemGroupLayer getGemGroupLayer();
    int getMaxColumnHeight();
    GemControls getControls();
}