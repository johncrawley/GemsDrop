package com.jcrawleydev.gemsdrop.service.grid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.GemPosition;
import com.jcrawleydev.gemsdrop.service.GridProps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GemGridImpl implements GemGrid {
    private List<List<Gem>> gemColumns;
    private final GridProps gridProps;
    private boolean haveAnyGemsMovedDuringLastDrop;
    private final int MAX_POSITION;

    public GemGridImpl(GridProps gridProps){
        this.gridProps = gridProps;
        MAX_POSITION = gridProps.numberOfRows() * gridProps.depthPerDrop();
        initColumns();
    }

    public List<List<Gem>> getGemColumns(){
        return this.gemColumns;
    }


    private void initColumns(){
        gemColumns = new ArrayList<>(gridProps.numberOfColumns());
        for(int i=0; i< gridProps.numberOfColumns(); i++){
            gemColumns.add(new ArrayList<>(gridProps.numberOfRows()));
        }
    }


    @Override
    public int getHeightOfColumn(int columnIndex){
        if(columnIndex > gemColumns.size() -1 || columnIndex < 0){
            return 10_000;
        }
        return gemColumns.get(columnIndex).size() * gridProps.depthPerDrop();
    }


    @Override
    public int getNumberOfColumns(){
        return gridProps.numberOfColumns();
    }


    public int getHighestColumnIndex(){
        return gemColumns.stream().map(List::size).max(Integer::compare).orElse(1) - 1;
    }


    public List<Integer> getColumnHeights(){
        List<Integer> heights = new ArrayList<>(gridProps.numberOfColumns());
        for(List<?> column : gemColumns){
            heights.add(column.size());
        }
        return heights;
    }


    public List<Gem> getGemsMarkedForDeletion(){
        var markedGems = new ArrayList<Gem>();
        for(List<Gem> column : gemColumns){
            for(Gem gem: column) {
                if (gem.isMarkedForDeletion()) {
                    markedGems.add(gem);
                }
            }
        }
        return markedGems;
    }


    public List<Gem> addGems(List<Gem> gems, boolean isOrientationVertical) {
        log("Entered addGems() isOrientationVertical? : " + isOrientationVertical);
       return isOrientationVertical? addAllVerticalGems(gems) : addHorizontalGems(gems);
    }


    private List<Gem> addAllVerticalGems(List<Gem> gems){
        Gem topGem = null, centreGem = null, bottomGem = null;
        for(Gem gem: gems) {
            switch (gem.getPosition()) {
                case TOP -> topGem = gem;
                case BOTTOM -> bottomGem = gem;
                case CENTRE -> centreGem = gem;
            }
        }
        if(isTouchingAColumn(bottomGem, false)){
            addGem(bottomGem);
            addGem(centreGem);
            addGem(topGem);
            return Collections.emptyList();
        }
        return gems;
    }


    private List<Gem> addHorizontalGems(List<Gem> gems){
        log("entering addHorizontalGems, gems size: " + gems.size());
        var gemsCopy = new ArrayList<>(gems);
        for(Gem gem : gems){
            if(isTouchingAColumn(gem, true)){
                addGem(gem);
                gemsCopy.remove(gem);
            }
        }
        return gemsCopy;
    }


    private boolean isTouchingAColumn(@Nullable Gem gem, boolean isHorizontal){
        if(gem == null){
            return false;
        }
        int gemColumn = gem.getColumn();
        log("isTouchingAColumn() gemColumn " + gemColumn + " size: " + gemColumns.get(gemColumn).size() + " heightOfDroppingGem: " + getColumnHeightOfDropping(gem, isHorizontal) + " gem bottom depth: " + gem.getBottomDepth());
        return gemColumns.get(gemColumn).size() >= getColumnHeightOfDropping(gem, isHorizontal);
    }


    private int getColumnHeightOfDropping(Gem gem, boolean isHorizontal){
        int bottomDepth = gem.getBottomDepth() + (isHorizontal ? 0 : 2);
        int alignedPosition = bottomDepth + (bottomDepth % 2 == 1 ? 1 : 0);
        return (MAX_POSITION - alignedPosition) /2;
    }


    private void addGem(@Nullable Gem gem){
        gemColumns.get(gem.getColumn()).add(gem);
    }


    private Optional<Gem> getBottomGem(List<Gem> gems){
        return gems.stream().filter(g -> g.getPosition() == GemPosition.BOTTOM).findFirst();
    }



    private void log(String msg){
        System.out.println("^^^ GemGrid: "+  msg);
    }


    public void turnAllGemsGrey(){
        for(List<Gem> column : gemColumns){
            for(Gem gem : column){
                gem.setGrey();
            }
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


    @Override @NonNull
    public String toString(){
        StringBuilder str = new StringBuilder();

        for(int i = gridProps.numberOfRows(); i>=0; i--) {
            str.append("[ ");
            appendRowTo(str, i);
            str.append("] ");
        }
        return str.toString();
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


}