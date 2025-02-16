package com.jcrawleydev.gemsdrop.service.game.grid;

import com.jcrawleydev.gemsdrop.service.game.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.gem.NullGem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GridEvaluator {

    private final List<List<Gem>> gemColumns;
    private final int MINIMUM_MATCH_NUMBER;
    private final int NUMBER_OF_COLUMNS;
    private final int NUMBER_OF_ROWS;
    private final Set<Long> markedGemIds = new HashSet<>();
    private final SectionEvaluator sectionEvaluator;

        public GridEvaluator(List<List<Gem>> gemColumns, int numberOfRows){
        this.MINIMUM_MATCH_NUMBER = 3;
        this.NUMBER_OF_COLUMNS = gemColumns.size();
        this.NUMBER_OF_ROWS = numberOfRows;
        this.gemColumns = gemColumns;
        sectionEvaluator = new SectionEvaluator(MINIMUM_MATCH_NUMBER);
    }


    public long[] evaluateGemGrid(){
        log("Entered evalAndDelete()");
        markedGemIds.clear();
        evaluate();
        updateSetOfMarkedGemIds();
        return markedGemIds.stream().mapToLong(x -> x).toArray();
    }


    private void log(String msg){
        System.out.println("GridEvaluator: " + msg);
    }


    private void updateSetOfMarkedGemIds(){
       for(var column : gemColumns){
           for(var gem: column){
               if(gem.isMarkedForDeletion()){
                   markedGemIds.add(gem.getId());
               }
           }
       }
    }


    private void evaluate(){
        log("Entered evaluate");
        evaluateRows();
        evaluateColumns();
        evaluateDiagonals();
    }


    private void evaluateRows(){
        for(int i = 0; i < NUMBER_OF_ROWS; i++){
            evaluateRow(i);
        }
    }


    private void evaluateColumns(){
        for(List<Gem> column: gemColumns){
            sectionEvaluator.evaluateGemsIn(column);
        }
    }


    private void evaluateDiagonals(){
        List<List<Gem>> diagonals = new ArrayList<>();
        addLowerHalfDiagonalsTo(diagonals);
        addUpperHalfDiagonalsTo(diagonals);
        addLowerHalfReverseDiagonalsTo(diagonals);
        addUpperHalfReverseDiagonalsTo(diagonals);

        for(List<Gem> diagonal : diagonals){
            sectionEvaluator.evaluateGemsIn(diagonal);
        }
    }


    private void addLowerHalfDiagonalsTo(List<List<Gem>> diagonals){

        for(int i = 0; i< (NUMBER_OF_ROWS - MINIMUM_MATCH_NUMBER); i++) {
            List<Gem> diagonal = new ArrayList<>();
            for (int currentRowIndex = 0 ; currentRowIndex < (NUMBER_OF_COLUMNS - i); currentRowIndex++) {
                int colIndex = i + currentRowIndex;
                addGemToDiagonal(diagonal, colIndex, currentRowIndex);
            }
            diagonals.add(diagonal);
        }
    }


    private void addUpperHalfDiagonalsTo(List<List<Gem>> diagonals){
        for(int startingRow = 1; startingRow < NUMBER_OF_ROWS - MINIMUM_MATCH_NUMBER; startingRow++) {
            List<Gem> diagonal = new ArrayList<>();
            for (int columnIndex = 0; columnIndex < NUMBER_OF_COLUMNS; columnIndex++) {
                List<Gem> column = gemColumns.get(columnIndex);
                int rowToRetrieve = columnIndex + startingRow;
                if(rowToRetrieve < column.size()){
                    diagonal.add(column.get(rowToRetrieve));
                    continue;
                }
                diagonal.add(new NullGem());
            }
            diagonals.add(diagonal);
        }
    }


    private void addGemToDiagonal(List<Gem> diagonal, int colIndex, int currentRowIndex){
        List<Gem> column = gemColumns.get(colIndex);
        if(currentRowIndex >= column.size()){
            diagonal.add(new NullGem());
            return;
        }
        diagonal.add(column.get(currentRowIndex));
    }


    private List<Gem> constructRow(int i){
        List<Gem> filledOutRow = new ArrayList<>();
        for(List<Gem> column : gemColumns){
            if(column.size() > i){
                filledOutRow.add(column.get(i));
                continue;
            }
            filledOutRow.add(new NullGem());
        }
        return filledOutRow;
    }


    private void addUpperHalfReverseDiagonalsTo(List<List<Gem>> diagonals){

        for(int startingRow = 1; startingRow < NUMBER_OF_ROWS - MINIMUM_MATCH_NUMBER; startingRow++){
            List<Gem> diagonal = new ArrayList<>();
            for(int rowIndex = startingRow, columnIndex = NUMBER_OF_COLUMNS -1; rowIndex < NUMBER_OF_ROWS && columnIndex >= 0; rowIndex++, columnIndex--){
                List<Gem> column = gemColumns.get(columnIndex);
                diagonal.add( rowIndex < column.size() ? column.get(rowIndex) : new NullGem());
            }
            diagonals.add(diagonal);
        }
    }


    private void evaluateRow(int i){
        List<Gem> filledOutRow = constructRow(i);
        sectionEvaluator.evaluateGemsIn(filledOutRow);
    }


    private void addLowerHalfReverseDiagonalsTo(List<List<Gem>> diagonals){
        for(int i = NUMBER_OF_COLUMNS - 1; i >= MINIMUM_MATCH_NUMBER -1; i--){
            diagonals.add(getLowerHalfReverseDiagonalStartingFromColumn(i));
        }
    }


    private List<Gem> getLowerHalfReverseDiagonalStartingFromColumn(int columnIndex){
        List<Gem> diagonal = new ArrayList<>();
        for(int column = columnIndex, row = 0; column >= 0; column--, row++){
            diagonal.add(getLowerHalfReverseDiagonalGem(row, column));
        }
        return diagonal;
    }


    private Gem getLowerHalfReverseDiagonalGem(int rowIndex, int columnIndex){
        List<Gem> column = gemColumns.get(columnIndex);
        if(rowIndex >= column.size()){
            return new NullGem();
        }
        return column.get(rowIndex);
    }



}
