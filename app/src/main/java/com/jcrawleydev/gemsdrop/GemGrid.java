package com.jcrawleydev.gemsdrop;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.NullGem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GemGrid {

    private int gemsCount;
    private List<List<Gem>> gemColumns;
    private int gemsPerGroup;
    private int gemAddOffset;
    private final int NUMBER_OF_ROWS;
    private int numberOfColumns;
    private final int MATCH_NUMBER = 3; // i.e. how many gems of same colour are required to make the sequence disappear

    public GemGrid(int numberOfColumns, int numberOfRows, int gemsPerGroup){
        NUMBER_OF_ROWS = numberOfRows;
        initColumns(numberOfColumns);
        this.gemsPerGroup = gemsPerGroup;
        this.gemAddOffset = gemsPerGroup / 2;
    }

    private void initColumns(int numberOfColumns){
        this.numberOfColumns = numberOfColumns;
        gemColumns = new ArrayList<>(numberOfColumns);
        for(int i=0; i< numberOfColumns; i++){
            gemColumns.add(new ArrayList<>(NUMBER_OF_ROWS));
        }
    }

    public void clear(){
        for(List<Gem> column : gemColumns){
            column.clear();
        }
        this.gemsCount = 0;
    }

    public void add(GemGroup gemGroup) {
        gemsCount += gemGroup.getGems().size();
        List<Gem> gems = gemGroup.getGems();
        int position = gemGroup.getPosition();

        if (gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL) {
            addHorizontal(gems, position);
        } else {
            addVertical(gems, position);
        }
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
        return gemsCount == 0;
    }

    public int gemCount(){
        return gemsCount;
    }

    public void evaluate(){
        evaluateRows();
        evaluateColumns();
        evaluateDiagonal();
        deleteMarkedGems();
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();

        for(int i = NUMBER_OF_ROWS-1; i>=0; i--) {
            str.append("[ ");
            str = appendRowTo(str, i);
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


    private StringBuilder appendRowTo(StringBuilder stringBuilder, int rowIndex){
        for (List<Gem> column : gemColumns) {
            if(column.size() > rowIndex){
                stringBuilder.append(column.get(rowIndex).getColor());
                stringBuilder.append(" ");
                continue;
            }
            stringBuilder.append("_ ");
        }
        return stringBuilder;
    }


    private void deleteMarkedGems(){
        for(List<Gem> column : gemColumns){
            //column.removeIf( g -> g.isMarkedForDeletion());

            Iterator<Gem> iterator = column.iterator();
            while(iterator.hasNext()){
                Gem gem = iterator.next();
                if(gem.isMarkedForDeletion()){
                    iterator.remove();
                    gemsCount--;
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

    /*
        // say match target is 3
        case 1: B B B G B B B
         - i = 0, j = 1 to end
         - deleteCandidateFlag should be set on i first, gems[i] flagged, totalSameColor = 1
         - j=1, same colour, reset candidate flag should be set on gem at index 1, totalSame=2
         - j=2, same colour, same deal, totalSame=3.
         - j=3, different colour, the totalSame is returned
         - i should now be 3, gem[3] flagged, totalSame = 1
         - j=4 not same color, gem[3] reset
         - i should now be 4, gem[4] is flagged, totalSame = 1
         - j=5, same color, flag set at gems[5], totalSame++
         - j=6  same color, flag set at gem([6], totalSame++
         - end of list, totalSame = 3, gems at 4,5,6 marked for deletion


     */
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
