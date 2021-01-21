package com.jcrawleydev.gemsdrop;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.NullGem;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.DrawItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;


public class GemGrid {

    private List<List<Gem>> gemColumns;
    private int gemsPerGroup;
    private int gemAddOffset;
    private final int NUMBER_OF_ROWS;
    private final int NUMBER_OF_COLUMNS;
    private final int MATCH_NUMBER = 3; // i.e. how many gems of same colour are required to make the sequence disappear

    public GemGrid(int numberOfColumns, int numberOfRows, int gemsPerGroup){
        NUMBER_OF_ROWS = numberOfRows;
        NUMBER_OF_COLUMNS = numberOfColumns;
        initColumns();
        this.gemsPerGroup = gemsPerGroup;
        this.gemAddOffset = gemsPerGroup / 2;
    }


    public List<Integer> getColumnHeights(){
        List<Integer> heights = new ArrayList<>(NUMBER_OF_COLUMNS);
        for(List<?> column : gemColumns){
            heights.add(column.size());
        }
        return heights;
    }

    public List<List<Gem>> getGemColumns(){
        return this.gemColumns;
    }

    public List<DrawItem> getAllGems(){
        List<DrawItem> allGems = new ArrayList<>(NUMBER_OF_COLUMNS * NUMBER_OF_ROWS);
        for(List<Gem> column : gemColumns){
            allGems.addAll(column);
        }
        return allGems;
    }


    public int getNumberOfColumns(){
        return NUMBER_OF_COLUMNS;
    }

    private void initColumns(){
        gemColumns = new ArrayList<>(NUMBER_OF_COLUMNS);
        for(int i=0; i< NUMBER_OF_COLUMNS; i++){
            gemColumns.add(new ArrayList<>(NUMBER_OF_ROWS));
        }
    }

    public void clear(){
        for(List<Gem> column : gemColumns) {
            column.clear();
        }
    }

    public void add(GemGroup gemGroup) {
        log("Entered add()");

        List<Gem> gems = gemGroup.getGems();
        int position = gemGroup.getPosition();

        if (gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL) {
            log("about to invoke addHorizontal()");
            addHorizontal(gems, position);
        } else {
            log("About to invoke addVertical");
            addVertical(gems, position);
        }
        log("Exiting add()");
    }


    public void printColumns(){
        for(List<Gem> column : gemColumns){
            StringBuilder str = new StringBuilder("col: ");
            for( Gem gem: column){
                str.append(" ");
                str.append(gem.getColor().toString());
            }
            log(str.toString());
        }
    }

    public boolean isEmpty(){
        for(List<Gem> column : gemColumns){
            if(!column.isEmpty()){
                return false;
            }
        }
        return true;
    }


    public int gemCount(){
        int amount = 0;
        for(List<Gem> column : gemColumns){
            amount += column.size();
        }
        return amount;
    }

    public void evaluate(){
        evaluateRows();
        evaluateColumns();
        evaluateDiagonal();
        deleteMarkedGems();
    }

    @Override @NonNull
    public String toString(){
        StringBuilder str = new StringBuilder();

        for(int i = NUMBER_OF_ROWS-1; i>=0; i--) {
            str.append("[ ");
            appendRowTo(str, i);
            str.append("] ");
        }
        return str.toString();
    }


    private void addHorizontal(List<Gem> gems, int position){
        int initialOffset = position - gemAddOffset;
        for (int i = 0; i < gemsPerGroup; i++) {
            int column = initialOffset + i;
            this.gemColumns.get(column).add(gems.get(i));

        }
    }


    private void addVertical(List<Gem> gems, int position){
        this.gemColumns.get(position).addAll(gems);
    }


    private void appendRowTo(StringBuilder stringBuilder, int rowIndex){
        for (List<Gem> column : gemColumns) {
            if(column.size() > rowIndex){
                stringBuilder.append(column.get(rowIndex).getColor());
                stringBuilder.append(" ");
                continue;
            }
            stringBuilder.append("_ ");
        }
    }


    private void deleteMarkedGems(){
        for(List<Gem> column : gemColumns){
            //column.removeIf( g -> g.isMarkedForDeletion());

            Iterator<Gem> iterator = column.iterator();
            while(iterator.hasNext()){
                Gem gem = iterator.next();
                if(gem.isMarkedForDeletion()){
                    iterator.remove();
                }
            }

        }
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
            for (int j = 0 ; j < (gemColumns.size()- i); j++) {
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


    public void addUpperHalfDiagonalsTo(List<List<Gem>> diagonals){

        for(int i=1; i< NUMBER_OF_ROWS - MATCH_NUMBER; i++) {
            List<Gem> diagonal = new ArrayList<>();
            for (int j = 0; j < gemColumns.size(); j++) {
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

            List<Gem> diagonal = new ArrayList<>();
            for(int j = i, row = 0; j >= 0; j--, row++){
                List<Gem> column = gemColumns.get(j);
                if(row >= column.size()){
                    diagonal.add(new NullGem());
                    continue;
                }
                diagonal.add(column.get(row));
            }
            diagonals.add(diagonal);
        }
    }


    private void addUpperHalfReverseDiagonalsTo(List<List<Gem>> diagonals){

        for(int startingRow= 1; startingRow < NUMBER_OF_ROWS - MATCH_NUMBER; startingRow++){

            List<Gem> diagonal = new ArrayList<>();
            for(int rowIndex = startingRow, columnIndex = NUMBER_OF_COLUMNS -1; rowIndex < NUMBER_OF_ROWS; rowIndex++, columnIndex--){

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




    private void log(String msg){
        System.out.println("GemGrid --> "  + msg);
    }


    private void evaluateRow(int i){
        List<Gem> filledOutRow = constructRow(i);
        // printRow(filledOutRow);
        evaluateGems(filledOutRow);
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


    private void printRow(List<Gem> gems){
        StringBuilder str =new StringBuilder();
        for(Gem gem : gems){
            str.append(" ");
            String color = "_";
            if(gem.getColor() != null){
                color = gem.getColor().toString();
            }
            str.append(color);
        }
        log(str.toString());
    }


    private void evaluateGems(List<Gem> gems){
        int i = 0;
        while( i < gems.size() - (MATCH_NUMBER - 1)){
            i = evaluateSection(gems, i);
        }
    }


    private int evaluateSection(List<Gem> gems, int i){
        Gem currentGem = gems.get(i);
        if(currentGem instanceof NullGem){
            return i+1;
        }

        currentGem.setDeleteCandidateFlag();
        int sameColorCount = 1;

        for(int j = i+1; j < gems.size(); j++){
            Gem gem = gems.get(j);
            if(gem.isNotSameColorAs(currentGem)){
                if(sameColorCount >= MATCH_NUMBER){
                    markAllCandidatesForDeletionInRange(i,j, gems);
                    return j;
                }
                resetFlagForAllGemsInRange(i,j,gems);
                return i+1;
            }
            sameColorCount++;
            gem.setDeleteCandidateFlag();
        }

        markAllCandidatesForDeletionInRange(i,gems.size()-1, gems);
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


}
