package com.jcrawleydev.gemsdrop.service.game.grid;

import androidx.annotation.NonNull;


import com.jcrawleydev.gemsdrop.service.game.GridProps;
import com.jcrawleydev.gemsdrop.service.game.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.gem.GemColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GemGridImpl implements GemGrid {
    private List<List<Gem>> gemColumns;
    private final GridProps gridProps;
    private final Set<Gem> markedGems = new HashSet<>();

    public GemGridImpl(GridProps gridProps){
        this.gridProps = gridProps;
        initColumns();
    }

    @Override
    public List<List<Gem>> getGemColumns(){
        return this.gemColumns;
    }


    @Override
    public List<Gem> getGems(){
        return gemColumns.stream().flatMap(List::stream).toList();
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


    @Override
    public void addRow(List<Gem> gems){
        for(int i = 0; i < gemColumns.size(); i++){
            if(i < gems.size()){
                var gem = gems.get(i);
                var gemColumn = gemColumns.get(i);
                setPositionsFor(gem, i, gemColumn.size());
                gemColumn.add(gems.get(i));
            }
        }
    }


    private void setPositionsFor(Gem gem, int columnIndex, int gemColumnSize){
        gem.setColumn(columnIndex);
        int containerPosition = gemColumnSize * gridProps.depthPerDrop();
        gem.setContainerPosition(containerPosition);
    }


    public List<Gem> gravityDropGemsOnePosition(){
        List<Gem> freeFallGems= new ArrayList<>();
        for(var col : gemColumns){
            for(int i = 0; i < col.size(); i++){
                freeFallGem(col, i, freeFallGems);
            }
        }
        return freeFallGems;
    }


    private void freeFallGem(List<Gem> column, int index, Set<Long> freeFallIds){
        var gem = column.get(index);
        if(gem.getContainerPosition() > index * gridProps.depthPerDrop()){
            gem.decrementContainerPosition();
            freeFallIds.add(gem.getId());
        }
    }


    private void freeFallGem(List<Gem> column, int index, List<Gem> freeFallIds){
        var gem = column.get(index);
        if(gem.getContainerPosition() > index * gridProps.depthPerDrop()){
            gem.decrementContainerPosition();
            freeFallIds.add(gem);
        }
    }


    public void removeMarkedGems(){
        gemColumns.forEach(column -> column.removeIf(Gem::isMarkedForDeletion));
    }


    public void addIfConnecting(Gem gem){
        var column = getColumnBeneath(gem);
        if(isTouching(gem, column)){
            column.add(gem);
            gem.markAsAddedToGrid();
        }
    }


    public void addWonderGemIfConnecting(Gem wonderGem){
        var column = getColumnBeneath(wonderGem);
        log("entered addWonderGemIfConnecting() is connecting: " + isTouching(wonderGem, column));
        if(isTouching(wonderGem, column)){
            wonderGem.markAsAddedToGrid();
            var touchedGemColor = column.getLast().getColor();
            log("addWonderGemIfConnecting() touchedGemColor: " + touchedGemColor);
            markAllGemsOf(touchedGemColor);
        }
    }


    private void markAllGemsOf(GemColor gemColor){
        markedGems.clear();
        gemColumns.stream()
                .flatMap(List::stream)
                .filter(g -> g.getColor() == gemColor)
                .forEach(gem -> {
                    gem.markAsAddedToGrid();
                    markedGems.add(gem);
                });
        log("markAllGemsOf() number of marked gems: " + markedGems.size());
    }


    private boolean isTouching(Gem gem, List<Gem> column){
        return gem.getContainerPosition() <= getTopPositionOf(column);
    }


    private List<Gem> getColumnBeneath(Gem gem){
        var colIndex = gem.getColumn();
        var index = Math.min(gemColumns.size() -1, Math.max(0, colIndex));
        return gemColumns.get(index);
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
    public void printColumnHeights(){
        String colHeights = gemColumns.stream()
                .map(col -> String.valueOf(getTopPositionOf(col)))
                .reduce("", (total, colHeight) -> total + " " + colHeight);
        log("printGemGridColumnHeights() : " + colHeights);
    }


    @Override
    public boolean exceedsMaxHeight(){
        return gemColumns.stream().anyMatch(col -> col.size() > gridProps.numberOfRows());
    }


    private void log(String msg){
        System.out.println("^^^ GemGrid: "+  msg);
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
