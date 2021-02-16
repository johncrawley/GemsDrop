package com.jcrawleydev.gemsdrop.gemgrid;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.NullGem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Evaluator {

    private GemGrid gemGrid;
    private List<List<Gem>> gemColumns;
    private final int MATCH_NUMBER;
    private final int NUMBER_OF_COLUMNS;
    private final int NUMBER_OF_ROWS;

    public Evaluator(GemGrid gemGrid, int matchNumber){
        this.gemGrid = gemGrid;
        this.MATCH_NUMBER = matchNumber;
        this.NUMBER_OF_COLUMNS = gemGrid.getNumberOfColumns();
        this.NUMBER_OF_ROWS = gemGrid.getNumberOfRows();
        this.gemColumns = gemGrid.getGemColumns();

    }

    public void evaluate(){
        evaluateRows();
        evaluateColumns();
        evaluateDiagonal();
        //deleteMarkedGems();
    }

    private boolean hasMarkedGems = false;

    public boolean hasMarkedGems(){
        return hasMarkedGems;
    }

    public void resetMarkedStatus(){
        hasMarkedGems = false;
    }


    private void markGem(Gem gem){
        gem.setDeleteCandidateFlag();
        hasMarkedGems = true;
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


    private void evaluateDiagonal(){
        List<List<Gem>> diagonals = new ArrayList<>();
        addLowerHalfDiagonalsTo(diagonals);
        addUpperHalfDiagonalsTo(diagonals);
        addLowerHalfReverseDiagonalsTo(diagonals);
        addUpperHalfReverseDiagonalsTo(diagonals);

        for(List<Gem> diagonal : diagonals){
            evaluateGems(diagonal);
        }
    }

    public void addLowerHalfDiagonalsTo(List<List<Gem>> diagonals){

        for(int i=0; i< (NUMBER_OF_ROWS - MATCH_NUMBER); i++) {
            List<Gem> diagonal = new ArrayList<>();
            for (int j = 0 ; j < (NUMBER_OF_COLUMNS - i); j++) {
                int colIndex = j + i;
                List<Gem> column = gemColumns.get(colIndex);
                if(j >= column.size()){
                    diagonal.add(new NullGem());
                    continue;
                }
                diagonal.add(column.get(j));
            }
            diagonals.add(diagonal);
        }
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
        /*
            if(there are marked gems, display them for half a second or so
            then start process to drop all hanging gems
            gemGridDropProcess -> which will continue until no gem drops after an iteration
            then repeat evaluation
         */
    }



    public void addUpperHalfDiagonalsTo(List<List<Gem>> diagonals){

        for(int i=1; i< NUMBER_OF_ROWS - MATCH_NUMBER; i++) {
            List<Gem> diagonal = new ArrayList<>();
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                List<Gem> column = gemColumns.get(j);
                int rowToRetrieve = j+ 1;
                if(rowToRetrieve >= column.size()){
                    diagonal.add(new NullGem());
                    continue;
                }
                diagonal.add(column.get(rowToRetrieve));
            }
            diagonals.add(diagonal);
        }
    }


    private void addLowerHalfReverseDiagonalsTo(List<List<Gem>> diagonals){
        for(int i= NUMBER_OF_COLUMNS - 1; i>= MATCH_NUMBER; i--){
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
                    markAllCandidatesForDeletionInRange(currentIndex,comparisonIndex, gems);
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


    private void resetFlagForAllGemsInRange(int startIndex, int endIndex, List<Gem> gems){
        for(int i=startIndex; i<= endIndex; i++){
            gems.get(i).resetDeleteCandidateFlag();
        }
    }


    private void markAllCandidatesForDeletionInRange(int startIndex, int endIndex, List<Gem> gems){
        for(int i=startIndex; i<= endIndex; i++){
            gems.get(i).setMarkedForDeletion();
        }
    }



    public void deleteMarkedGems(){
        log("Entered deleteMarkedGems()");
        for(List<Gem> column : gemGrid.getGemColumns()){
            //column.removeIf( g -> g.isMarkedForDeletion());

            Iterator<Gem> iterator = column.iterator();
            while(iterator.hasNext()){
                Gem gem = iterator.next();
                if(gem.isMarkedForDeletion()){
                    log("Found gem to remove! " + gem.getColor().toString());
                    iterator.remove();
                }
            }
        }
    }

    void log(String msg){
        System.out.println("Evaluator: " + msg);
    }

}
