package com.jcrawleydev.gemsdrop.service.game;

import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;
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
        int delay = 100;
        turnDroppingGemsGrey();
        future = executorService.scheduleWithFixedDelay(this::turnNextRowGrey, delay, delay, TimeUnit.MILLISECONDS);
    }


    private void turnDroppingGemsGrey(){
        DroppingGems droppingGems = game.getDroppingGems();
        if(droppingGems != null){
            var gems = droppingGems.get();
            gems.forEach(Gem::setGrey);
            game.updateGemsOnView(gems);
        }
    }


    private void populateGemRows(){
        gemRows = getGemRows();
        currentRowIndex = gemRows.size() - 1;
        log("populateGemRows() currentRowIndex: " + currentRowIndex + " number of rows: " + gridProps.numberOfRows());
    }


    private void log(String msg){
        System.out.println("^^^ GameOverAnimator: " + msg);
    }


    private void turnNextRowGrey(){
        var row = gemRows.get(currentRowIndex);
        currentRowIndex--;
        row.forEach(Gem::setGrey);
        game.updateGemsOnView(row);
        if(currentRowIndex < 0 ){
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
