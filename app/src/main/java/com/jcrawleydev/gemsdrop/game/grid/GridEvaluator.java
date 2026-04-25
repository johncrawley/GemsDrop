package com.jcrawleydev.gemsdrop.game.grid;

import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.gem.NullGem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GridEvaluator {

    private List<List<Gem>> gemColumns;
    private final int MINIMUM_MATCH_NUMBER;
    private int NUMBER_OF_COLUMNS;
    private int NUMBER_OF_ROWS;
    private final Set<Long> markedGemIds = new HashSet<>();
    private final SectionEvaluator sectionEvaluator;
    private int rowLimit;
    public record GemCoordinates (int column, int row){}
    private final List<List<GemCoordinates>> sections = new ArrayList<>();


    public GridEvaluator(){
        this.MINIMUM_MATCH_NUMBER = 3;
        sectionEvaluator = new SectionEvaluator(MINIMUM_MATCH_NUMBER);
    }


    public void init(List<List<Gem>> gemColumns, int numberOfRows){
        NUMBER_OF_COLUMNS = gemColumns.size();
        this.NUMBER_OF_ROWS = numberOfRows;
        rowLimit = NUMBER_OF_ROWS - MINIMUM_MATCH_NUMBER;
        this.gemColumns = gemColumns;
        buildSections();
    }


    public long[] evaluateGemGrid(){
        markedGemIds.clear();
        evaluate();
        updateSetOfMarkedGemIds();
        return markedGemIds.stream().
                mapToLong(x -> x)
                .toArray();
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
        evaluateRows();
        evaluateColumns();
        evaluateDiagonals();
    }

    private void evaluateOLD(){
        evaluateRows();
        evaluateColumns();
        //evaluateDiagonalSections();
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
        var diagonals = new ArrayList<List<Gem>>();
        addLowerHalfDiagonalsTo(diagonals);
        addUpperHalfDiagonalsTo(diagonals);
        addLowerHalfReverseDiagonalsTo(diagonals);
        addUpperHalfReverseDiagonalsTo(diagonals);

        for(List<Gem> diagonal : diagonals){
            sectionEvaluator.evaluateGemsIn(diagonal);
        }
    }


    private void evaluateDiagonalSections(){
        for(List<GemCoordinates> section : sections){
            sectionEvaluator.evaluate(section, gemColumns);
        }
    }


    private void buildSections(){
        if(!sections.isEmpty()){
            return;
        }
        addLowerHalfDiagonalSections();
        addUpperHalfDiagonalSections();

        addUpperHalfReverseSections();
        addLowerHalfReverseDiagonalSections();
        log("exiting buildSections() number of sections: " + sections.size());
    }


    private void log(String msg){
        System.out.println("^^^ GridEvaluator: " + msg);
    }


    private void addLowerHalfDiagonalsTo(List<List<Gem>> diagonals){
        for(int i = 0; i < rowLimit; i++) {
            var diagonal = new ArrayList<Gem>();
            for (int currentRowIndex = 0 ; currentRowIndex < (NUMBER_OF_COLUMNS - i); currentRowIndex++) {
                int colIndex = i + currentRowIndex;
                addGemToDiagonal(diagonal, colIndex, currentRowIndex);
            }
            diagonals.add(diagonal);
        }
    }

    private void addLowerHalfDiagonalSections(){
        for(int i = 0; i < rowLimit; i++) {
            var section = new ArrayList<GemCoordinates>();
            for (int currentRowIndex = 0 ; currentRowIndex < (NUMBER_OF_COLUMNS - i); currentRowIndex++) {
                int colIndex = i + currentRowIndex;
                section.add(new GemCoordinates(colIndex, currentRowIndex));
            }
            sections.add(section);
        }
    }


    private void addUpperHalfDiagonalSections(){
        for(int startingRow = 1; startingRow < rowLimit; startingRow++) {
            var section = new ArrayList<GemCoordinates>();
            for (int columnIndex = 0; columnIndex < NUMBER_OF_COLUMNS; columnIndex++) {
                var column = gemColumns.get(columnIndex);
                int rowIndex = columnIndex + startingRow;
                if(rowIndex < column.size()){
                    section.add(new GemCoordinates(columnIndex, rowIndex));
                    continue;
                }
                section.add(getNullGemCoordinates());
            }
            sections.add(section);
        }
    }


    private void addUpperHalfReverseSections(){
        for(int startingRow = 1; startingRow < rowLimit ; startingRow++){
            var section = new ArrayList<GemCoordinates>();
            for(int rowIndex = startingRow, columnIndex = NUMBER_OF_COLUMNS - 1; rowIndex < NUMBER_OF_ROWS && columnIndex >= 0; rowIndex++, columnIndex--){
                var column = gemColumns.get(columnIndex);
                if(rowIndex < column.size()){
                    section.add(new GemCoordinates(columnIndex, rowIndex));
                    continue;
                }
                section.add(getNullGemCoordinates());
            }
            sections.add(section);
        }
    }



    private GemCoordinates getNullGemCoordinates(){
        return new GemCoordinates(-1, -1);
    }


    private void addLowerHalfReverseDiagonalSections(){
        for(int i = NUMBER_OF_COLUMNS - 1; i >= MINIMUM_MATCH_NUMBER -1; i--){
            sections.add(getLowerHalfReverseDiagonalSection(i));
        }
    }


    private List<GemCoordinates> getLowerHalfReverseDiagonalSection(int columnIndex){
        var section = new ArrayList<GemCoordinates>();
        for(int column = columnIndex, row = 0; column >= 0; column--, row++){
            section.add(getLowerHalfReverseDiagonalCoordinates(row, column));
        }
        return section;
    }


    private GemCoordinates getLowerHalfReverseDiagonalCoordinates(int rowIndex, int columnIndex){
        var column = gemColumns.get(columnIndex);
        if(rowIndex >= column.size()){
            return getNullGemCoordinates();
        }
        return new GemCoordinates(columnIndex, rowIndex);
    }


    private void addUpperHalfDiagonalsTo(List<List<Gem>> diagonals){
        for(int startingRow = 1; startingRow < rowLimit; startingRow++) {
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
        int lastColumnIndex = NUMBER_OF_COLUMNS - 1;

        for(int startingRow = 1; startingRow < rowLimit ; startingRow++){
            var diagonal = new ArrayList<Gem>();
            for(int rowIndex = startingRow, columnIndex = lastColumnIndex; rowIndex < NUMBER_OF_ROWS && columnIndex >= 0; rowIndex++, columnIndex--){
                var column = gemColumns.get(columnIndex);
                diagonal.add( rowIndex < column.size() ? column.get(rowIndex) : new NullGem());
            }
            diagonals.add(diagonal);
        }
    }


    private void addLowerHalfReverseDiagonalsTo(List<List<Gem>> diagonals){
        for(int i = NUMBER_OF_COLUMNS - 1; i >= MINIMUM_MATCH_NUMBER -1; i--){
            diagonals.add(getLowerHalfReverseDiagonalStartingFromColumn(i));
        }
    }


    private void evaluateRow(int i){
        var filledOutRow = constructRow(i);
        sectionEvaluator.evaluateGemsIn(filledOutRow);
    }


    private List<Gem> getLowerHalfReverseDiagonalStartingFromColumn(int columnIndex){
        var diagonal = new ArrayList<Gem>();
        for(int column = columnIndex, row = 0; column >= 0; column--, row++){
            diagonal.add(getLowerHalfReverseDiagonalGem(row, column));
        }
        return diagonal;
    }


    private Gem getLowerHalfReverseDiagonalGem(int rowIndex, int columnIndex){
        var column = gemColumns.get(columnIndex);
        if(rowIndex >= column.size()){
            return new NullGem();
        }
        return column.get(rowIndex);
    }


}
