package com.jcrawleydev.gemsdrop;

import android.os.Build;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.NullGem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.RequiresApi;

public class GemGrid {

    private int gemsCount;
    private List<List<Gem>> gemColumns;
    private int gemsPerGroup;
    private int gemAddOffset;
    private final int MAX_ROWS = 12;
    private int numberOfColumns;
    private final int MATCH_NUMBER = 3; // i.e. how many gems of same colour are required to make the sequence disappear

    public GemGrid(int numberOfColumns, int gemsPerGroup){
        initColumns(numberOfColumns);
        this.gemsPerGroup = gemsPerGroup;
        this.gemAddOffset = gemsPerGroup / 2;
    }

    private void initColumns(int numberOfColumns){
        this.numberOfColumns = numberOfColumns;
        gemColumns = new ArrayList<>(numberOfColumns);
        for(int i=0; i<numberOfColumns; i++){
            gemColumns.add(new ArrayList<Gem>(MAX_ROWS));
        }
    }

    public void add(GemGroup gemGroup) {
        gemsCount += gemGroup.getGems().size();
        List<Gem> latestGems = gemGroup.getGems();
        int position = gemGroup.getPosition();

        if (gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL) {
            int initialOffset = position - gemAddOffset;
            for (int i = 0; i < gemsPerGroup; i++) {
                this.gemColumns.get(initialOffset + i).add(latestGems.get(i));
            }
        } else {
            for (int i = 0; i < latestGems.size(); i++) {
                this.gemColumns.get(position).addAll(latestGems);
            }
        }
    }

    private void addVerticalGems(List<Gem> gems, int position){
        for (int i = 0; i < gems.size(); i++) {
            this.gemColumns.get(position).addAll(gems);
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
        deleteMarkedGems();
    }


    private void deleteMarkedGems(){
        for(List<Gem> column : gemColumns){
            //column.removeIf( g -> g.isMarkedForDeletion());
            log("deleteMarkedGems() : number of gems in column : " + column.size());
            Iterator<Gem> iterator = column.iterator();
            while(iterator.hasNext()){
                Gem gem = iterator.next();
                if(gem.isMarkedForDeletion()){
                    log("deleteMarkedGems() -> deleting!");
                    iterator.remove();
                    gemsCount--;
                }
            }

            log("deleteMarkedGems() : number of gems in column after deletion : " + column.size());
            log("deleteMarkedGems() : ***********************");
        }
    }


    private void evaluateRows(){
        for(int i=0; i < MAX_ROWS; i++){
            evaluateRow(i);
        }
    }

    private void log(String msg){
        System.out.println("GemGrid --> "  + msg);
    }

    private void evaluateRow(int i){
        log("Entered evaluateRow");
        List<Gem> convertedColumns = new ArrayList<>();
        for(List<Gem> column : gemColumns){
            if(column.size() > i){
                convertedColumns.add(column.get(i));
                continue;
            }
            convertedColumns.add(new NullGem());
        }
        evaluateGems(convertedColumns);
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
