package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.speed.SpeedController;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;
import com.jcrawleydev.gemsdrop.view.ScoreBoardLayer;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

public interface  GameStateManager {

    void loadState(GameState.Type type);
    SpeedController getSpeedController();
    GemGroupFactory getGemGroupFactory();
    GemGroup getGemGroup();
    void setGemGroup(GemGroup gemGroup);
    GemGridLayer getGemGridLayer();
    GemGroupLayer getGemGroupLayer();
    int getMaxColumnHeight();
    GemControls getControls();
    ScoreBoardLayer getScoreBoardLayer();

}
