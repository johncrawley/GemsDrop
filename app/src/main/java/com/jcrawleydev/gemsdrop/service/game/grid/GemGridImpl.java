package com.jcrawleydev.gemsdrop.service.game.grid;

import androidx.annotation.NonNull;


import com.jcrawleydev.gemsdrop.service.game.GridProps;
import com.jcrawleydev.gemsdrop.service.game.gem.Gem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GemGridImpl implements GemGrid {
    private List<List<Gem>> gemColumns;
    private final GridProps gridProps;

    public GemGridImpl(GridProps gridProps){
        this.gridProps = gridProps;
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


    public long[] gravityDropOnePosition(){
        Set<Long> freeFallIds = new HashSet<>();
        for(var col : gemColumns){
            for(int i = 0; i < col.size(); i++){
                freeFallGem(col, i, freeFallIds);
            }
        }
        return freeFallIds.stream().mapToLong(l -> l).toArray();
    }


    private void freeFallGem(List<Gem> column, int index, Set<Long> freeFallIds){
        var gem = column.get(index);
        if(gem.getContainerPosition() > index * gridProps.depthPerDrop()){
            gem.decrementContainerPosition();
            freeFallIds.add(gem.getId());
        }
    }


    public void removeMarkedGems(){
        gemColumns.forEach(column -> column.removeIf(Gem::isMarkedForDeletion));
    }


    public void addIfConnecting(Gem gem){
        var column = gemColumns.get(gem.getColumn());
        log("addIfConnecting() gem container position: " + gem.getContainerPosition() + " gem column index: " + gem.getColumn() + " top position of column: " + getTopPositionOf(column) + " gemId: " + gem.getId());
        if(gem.getContainerPosition() <= getTopPositionOf(column)){
            column.add(gem);
            gem.markAsAddedToGrid();
        }
    }


    public void addIfConnecting(Gem bottomGem, Gem middleGem, Gem topGem ){
        var column = gemColumns.get(bottomGem.getColumn());
        if(bottomGem.getContainerPosition() <= getTopPositionOf(column)){
            add(bottomGem, column);
            add(middleGem, column);
            add(topGem, column);
        }
    }


    private void add(Gem gem, List<Gem> column){
        column.add(gem);
        gem.markAsAddedToGrid();
    }


    private int getTopPositionOf(List<Gem> column){
        return column.size() * gridProps.depthPerDrop();
    }


    @Override
    public int getColumnHeightAt(int columnIndex){
        if(columnIndex > gemColumns.size() -1 || columnIndex < 0){
            return 10_000;
        }
        var column = gemColumns.get(columnIndex);
        return getTopPositionOf(column);
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


    public int gemCount(){
        return gemColumns.stream().map(List::size).reduce(0, Integer::sum);
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
