package com.jcrawleydev.gemsdrop.gemgrid;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.NullGem;

import java.util.ArrayList;
import java.util.List;

public class Evaluator {

    private GemGrid gemGrid;
    private final List<List<Gem>> gemColumns;
    private final int MATCH_NUMBER;
    private final int NUMBER_OF_COLUMNS;
    private final int NUMBER_OF_ROWS;
    private boolean hasMarkedGems = false;
    private final List<Gem> markedGems = new ArrayList<>();
    private List<Long> markedGemIds = new ArrayList<>();


    public Evaluator(GemGrid gemGrid, int matchNumber){
        this.gemGrid = gemGrid;
        this.MATCH_NUMBER = matchNumber;
        this.NUMBER_OF_COLUMNS = gemGrid.getNumberOfColumns();
        this.NUMBER_OF_ROWS = gemGrid.getNumberOfRows();
        this.gemColumns = gemGrid.getGemColumns();
    }


    public Evaluator(List<List<Gem>> gemColumns, int numberOfRows){
        this.MATCH_NUMBER = 3;
        this.NUMBER_OF_COLUMNS = gemColumns.size();
        this.NUMBER_OF_ROWS = numberOfRows;
        this.gemColumns = gemColumns;
    }


    public void deleteMarkedGems(List<List<Gem>> gemColumns){
        for(List<Gem> column : gemGrid.getGemColumns()){
            column.removeIf(Gem::isMarkedForDeletion);
        }
        this.hasMarkedGems = false;
    }


    public long[] evalAndDelete(){
        markedGems.clear();
        markedGemIds.clear();
        evaluate();
        deleteMarkedGems();
        return markedGemIds.stream().mapToLong(x -> x).toArray();
    }


    public void evaluate(){
        evaluateRows();
        evaluateColumns();
        evaluateDiagonals();
    }


    public boolean hasMarkedGems(){
        return hasMarkedGems;
    }


    public void deleteMarkedGems(){
        for(List<Gem> column : gemGrid.getGemColumns()){
            column.removeIf(Gem::isMarkedForDeletion);
        }
        this.hasMarkedGems = false;
    }


    private void evaluateRows(){
        for(int i = 0; i < NUMBER_OF_ROWS; i++){
            evaluateRow(i);
        }
    }


    private void evaluateColumns(){
        for(List<Gem> column: gemColumns){
            evaluateGems(column);
        }
    }


    private void evaluateDiagonals(){
        List<List<Gem>> diagonals = new ArrayList<>();
        addLowerHalfDiagonalsTo(diagonals);
        addUpperHalfDiagonalsTo(diagonals);
        addLowerHalfReverseDiagonalsTo(diagonals);
        addUpperHalfReverseDiagonalsTo(diagonals);

        for(List<Gem> diagonal : diagonals){
            evaluateGems(diagonal);
        }
    }


    private void addLowerHalfDiagonalsTo(List<List<Gem>> diagonals){

        for(int i=0; i< (NUMBER_OF_ROWS - MATCH_NUMBER); i++) {
            List<Gem> diagonal = new ArrayList<>();
            for (int currentRowIndex = 0 ; currentRowIndex < (NUMBER_OF_COLUMNS - i); currentRowIndex++) {
                int colIndex = i + currentRowIndex;
               addGemToDiagonal(diagonal, colIndex, currentRowIndex);
            }
            diagonals.add(diagonal);
        }
    }


    private void addUpperHalfDiagonalsTo(List<List<Gem>> diagonals){

        for(int startingRow = 1; startingRow < NUMBER_OF_ROWS - MATCH_NUMBER; startingRow++) {
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

        for(int startingRow= 1; startingRow < NUMBER_OF_ROWS - MATCH_NUMBER; startingRow++){

            List<Gem> diagonal = new ArrayList<>();
            for(int rowIndex = startingRow, columnIndex = NUMBER_OF_COLUMNS -1; rowIndex < NUMBER_OF_ROWS; rowIndex++, columnIndex--){
                if(columnIndex < 0){
                    break;
                }
                List<Gem> column = gemColumns.get(columnIndex);
                if(rowIndex >= column.size()){
                    diagonal.add(new NullGem());
                    continue;
                }
                diagonal.add(column.get(rowIndex));
            }
            diagonals.add(diagonal);
        }
    }


    private void evaluateRow(int i){
        List<Gem> filledOutRow = constructRow(i);
        evaluateGems(filledOutRow);
    }


    private void addLowerHalfReverseDiagonalsTo(List<List<Gem>> diagonals){
        for(int i= NUMBER_OF_COLUMNS - 1; i >= MATCH_NUMBER -1; i--){
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


    private void evaluateGems(List<Gem> gems){
        int i = 0;
        while( i < gems.size() - (MATCH_NUMBER - 1)){
            i = evaluateSection(gems, i);
        }
    }


    private int evaluateSection(List<Gem> gems, int currentIndex){
        Gem currentGem = gems.get(currentIndex);
        int nextIndex = currentIndex + 1;
        if(currentGem instanceof NullGem){
            return nextIndex;
        }

        markGem(currentGem);
        int sameColorCount = 1;

        for(int comparisonIndex = nextIndex; comparisonIndex < gems.size(); comparisonIndex++){
            Gem comparisonGem = gems.get(comparisonIndex);
            if(comparisonGem.isNotSameColorAs(currentGem)){
                if(sameColorCount >= MATCH_NUMBER){
                    markAllCandidatesForDeletionInRange(currentIndex, comparisonIndex, gems);
                    return comparisonIndex;
                }
                resetFlagForAllGemsInRange(currentIndex,comparisonIndex,gems);
                return nextIndex;
            }
            sameColorCount++;
            markGem(comparisonGem);
        }

        markAllCandidatesForDeletionInRange(currentIndex,gems.size()-1, gems);
        return gems.size();
    }


    private void markGem(Gem gem){
        gem.setDeleteCandidateFlag();
        markedGemIds.add(gem.getId());
    }


    private void resetFlagForAllGemsInRange(int startIndex, int endIndex, List<Gem> gems){
        for(int i=startIndex; i<= endIndex; i++){
            gems.get(i).resetDeleteCandidateFlag();
        }
    }


    private void markAllCandidatesForDeletionInRange(int startIndex, int endIndex, List<Gem> gems){
        for(int i=startIndex; i<= endIndex; i++){
            gems.get(i).setMarkedForDeletion();
        }
        hasMarkedGems = true;
    }



}
