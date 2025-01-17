package com.jcrawleydev.gemsdrop.service.game;

import com.jcrawleydev.gemsdrop.service.game.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;

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
    private final GemGrid gemGrid;
    private int currentRowIndex;
    private List<List<Gem>> gemRows;
    private final GridProps gridProps;


    public GameOverAnimator(Game game, GemGrid gemGrid, GridProps gridProps){
        this.game = game;
        this.gemGrid = gemGrid;
        this.gridProps = gridProps;
        executorService = Executors.newSingleThreadScheduledExecutor();

    }


    public void startGameOverSequence(){
        populateGemRows();
        future = executorService.scheduleWithFixedDelay(this::turnNextRowGrey, 0, 120, TimeUnit.MILLISECONDS);
    }


    private void populateGemRows(){
        gemRows = getGemRows();
        currentRowIndex = 0;
    }


    private void turnNextRowGrey(){
        var row = gemRows.get(currentRowIndex);
        row.forEach(Gem::setGrey);
        game.updateGemsOnView(row);
        currentRowIndex++;
        if(currentRowIndex >= gemRows.size()){
            future.cancel(false);
            game.end();
        }
    }


    public List<List<Gem>> getGemRows(){
        List<List<Gem>> gemRows = new ArrayList<>(gridProps.numberOfRows());
        for(int i = 0 ; i < gridProps.numberOfRows(); i++){
            gemRows.add(getGemRow(i));
        }
        return gemRows;
    }


    private List<Gem> getGemRow(int index){
        List<Gem> row = new ArrayList<>(gridProps.numberOfColumns());
        for(var column : gemGrid.getGemColumns()){
            if(index < column.size()){
                row.add(column.get(index));
            }
        }
        return row;
    }



}
