package com.jcrawleydev.gemsdrop.game;

import com.jcrawleydev.gemsdrop.audio.SoundEffect;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
    Turns all the Gems grey and signals for the game to end.
 */
public class GameOverAnimator {

    private Future<?> future;
    private final ScheduledExecutorService executorService;
    private final Game game;
    private final GameModel gameModel;
    private final GemGrid gemGrid;
    private int currentRowIndex;
    private List<List<Gem>> gemRows;
    private final GridProps gridProps;
    private final int numberOfRowsAlreadyGreyedOut;


    public GameOverAnimator(Game game, GemGrid gemGrid){
        this.game = game;
        this.gameModel = game.getGameModel();
        this.gemGrid = gemGrid;
        this.gridProps = game.getGameModel().getGridProps();
        executorService = Executors.newSingleThreadScheduledExecutor();
        numberOfRowsAlreadyGreyedOut = gameModel.getNumberOfRowsAlreadyGreyedOut();
    }


    public void startGameOverSequence(){
        populateGemRows();
        int delay = 100;
        turnDroppingGemsGrey();
        future = executorService.scheduleWithFixedDelay(this::turnNextRowGrey, delay, delay, TimeUnit.MILLISECONDS);
    }


    private void turnDroppingGemsGrey(){
        DroppingGems droppingGems = game.getDroppingGems();
        if(droppingGems != null){
            droppingGems.setGrey();
            updateColorsOnView(droppingGems.get());
        }
    }


    private void populateGemRows(){
        gemRows = getGemRows();
        currentRowIndex = gemRows.size() - (1 + numberOfRowsAlreadyGreyedOut);
    }


    private void turnNextRowGrey(){
        var row = gemRows.get(currentRowIndex);
        currentRowIndex--;
        row.forEach(Gem::setGrey);
        updateColorsOnView(row);
        if(currentRowIndex < 0 ){
            future.cancel(false);
            game.end();
        }
        game.getSoundEffectManager().play(SoundEffect.GEMS_GREYED_OUT);
        gameModel.incNumberOfGreyedOutRows();
    }


    private void updateColorsOnView(List<Gem> gems){
        game.updateGemsColorsOnView(gems);
    }


    public List<List<Gem>> getGemRows(){
        List<List<Gem>> gemRows = new ArrayList<>(gridProps.numberOfRows());
        for(int i = 0 ; i < gridProps.numberOfRows(); i++){
            gemRows.add(getGemRow(i));
        }
        return gemRows;
    }


    private List<Gem> getGemRow(int index){
        var row = new ArrayList<Gem>(gridProps.numberOfColumns());
        for(var column : gemGrid.getGemColumns()){
            if(index < column.size()){
                var gem = column.get(index);
                row.add(gem);
            }
        }
        return row;
    }


}
