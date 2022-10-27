package com.jcrawleydev.gemsdrop.gemgrid;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.item.DrawableItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;


public class GemGrid {

    private List<List<Gem>> gemColumns;
    private final int NUMBER_OF_ROWS;
    private final int NUMBER_OF_COLUMNS;
    private float gemSize;
    private int floorY;
    private int startingX;
    private int dropIncrement;
    private boolean haveAnyGemsMovedDuringLastDrop;
    private final int INITIAL_FLOOR_POSITION = 1;

    public GemGrid(int numberOfColumns, int numberOfRows){
        NUMBER_OF_ROWS = numberOfRows;
        NUMBER_OF_COLUMNS = numberOfColumns;
        initColumns();
    }

    public List<List<Gem>> getGemColumns(){
        return this.gemColumns;
    }


    public int getHighestColumnIndex(){
        return gemColumns.stream().map(List::size).max(Integer::compare).orElse(1) - 1;
    }


    public void setGemSize(float gemSize){
        this.gemSize = gemSize;
    }

    public void setFloorY(int y){
        this.floorY = y;
    }

    public void setStartingX(int x){
        this.startingX = x;
    }

    public void setDropIncrement(int dropIncrement){
        this.dropIncrement = dropIncrement;
    }


    public List<Integer> getColumnHeights(){
        List<Integer> heights = new ArrayList<>(NUMBER_OF_COLUMNS);
        for(List<?> column : gemColumns){
            heights.add(column.size());
        }
        return heights;
    }


    public void flickerGemsMarkedForDeletion(){
        for(List<Gem> column : gemColumns){
            for(Gem gem: column){
                if(!gem.isMarkedForDeletion()){
                    continue;
                }
                if(gem.isVisible()){
                    gem.setInvisible();
                    continue;
                }
                gem.setVisible();
            }
        }
    }


    public boolean shouldAddAll(GemGroup gemGroup) {
        if(gemGroup.getRealBottomPosition() <= INITIAL_FLOOR_POSITION){
            return true;
        }
        if(gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL){
            return areAllGemsConnectingToColumnsReal(gemGroup);
        }
        return gemGroup.getRealBottomPosition() <= getRealColumnHeight(gemGroup.getXPosition());
    }


    public float getTopYOfColumn(int position){
        int index = Math.max(0, position);
        index = Math.min(gemColumns.size()-1, index);
        return floorY - (gemColumns.get(index).size() * gemSize);
    }


    public boolean addAnyFrom(GemGroup gemGroup){
        if(isVertical(gemGroup)) {
            return false;
        }
        boolean hasGemBeenAdded = false;
        List<Gem> gems = gemGroup.getGridGems();

        for(int i = 0, position = gemGroup.getBaseXPosition(); i< gems.size(); i++, position++){
            Gem gem = gems.get(i);
            if(isColumnSizeGreaterThanGemGroupBottomPositionReal(gemGroup, position)){
                add(gem, position);
                hasGemBeenAdded = true;
            }
        }
        return hasGemBeenAdded;
    }


    private boolean isColumnSizeGreaterThanGemGroupBottomPositionReal(GemGroup gemGroup, int position){
        String msg = " colHeight(" + position + ") : "
                + getRealColumnHeight(position)
                + " gemGroupBottomPosition: "
                + gemGroup.getRealBottomPosition();
        log("Entered isColumnSizeGreaterThanGemGroupBottomPosition() " + msg);
        return getRealColumnHeight(position) >= gemGroup.getRealBottomPosition();
    }


    private void log(String msg){
        System.out.println("GemGrid: "+  msg);
    }


    boolean isVertical(GemGroup gemGroup){
        return gemGroup.getOrientation() == GemGroup.Orientation.VERTICAL;
    }


    private boolean areAllGemsConnectingToColumnsReal(GemGroup gemGroup){
        for(int position : gemGroup.getGemPositions()){
            if(gemGroup.getRealBottomPosition() > getRealColumnHeight(position)){
                return false;
            }
        }
        return true;
    }


    public int getColumnHeight(int position){
        return INITIAL_FLOOR_POSITION + gemColumns.get(position).size();
    }


    public boolean doesColumnHeightMeetLowestGem(int columnIndex, GemGroup gemGroup){
        return getRealColumnHeight(columnIndex) >= gemGroup.getRealBottomPosition();
    }


    public boolean doesColumnHeightMeetMiddleGem(int columnIndex, GemGroup gemGroup){
        return getRealColumnHeight(columnIndex) >= gemGroup.getRealMiddlePosition();
    }


    public int getRealColumnHeight(int position){
        return INITIAL_FLOOR_POSITION + (gemColumns.get(position).size() * 2);
    }


    public List<DrawableItem> getAllGems(){
        List<DrawableItem> allGems = new ArrayList<>(NUMBER_OF_COLUMNS * NUMBER_OF_ROWS);
        for(List<Gem> column : gemColumns){
            allGems.addAll(column);
        }
        return allGems;
    }


    public List<Gem> getAllGemsInGrid(){
        List<Gem> allGems = new ArrayList<>(NUMBER_OF_COLUMNS * NUMBER_OF_ROWS);
        for(List<Gem> column : gemColumns){
            allGems.addAll(column);
        }
        return allGems;
    }


    public int getNumberOfColumns(){
        return NUMBER_OF_COLUMNS;
    }


    public int getNumberOfRows(){
        return NUMBER_OF_ROWS;
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


    public void turnAllGemsGrey(){
        for(List<Gem> column : gemColumns){
            for(Gem gem : column){
                gem.setGrey();
            }
        }
    }


    public void add(GemGroup gemGroup) {
        List<Gem> gems = gemGroup.getCopyOfGemsToAddToGrid();
        int position = gemGroup.getXPosition();

        if (gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL) {
            addHorizontal(gems, position);
        } else {
            addVertical(gems, position);
        }
    }


    public void add(Gem gem, int position){
        addGemToColumn(gem.clone(), position);
        gem.setInvisible();
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

        for(int i = NUMBER_OF_ROWS-1; i>=0; i--) {
            str.append("[ ");
            appendRowTo(str, i);
            str.append("] ");
        }
        return str.toString();
    }


    private void addHorizontal(List<Gem> gems, int gemGroupPosition){
        int initialOffset = gemGroupPosition - gems.size()/ 2;
        for (int i = 0; i < gems.size(); i++) {
            addGemToColumn(gems.get(i), initialOffset + i);
        }
    }


    private void addGemToColumn(Gem gem, int columnIndex){
        if(!gem.isVisible()){
            return;
        }
        List<Gem> column = gemColumns.get(columnIndex);
        int rowIndex = column.size();
        setGemCoordinatesToGridPosition(gem, rowIndex, columnIndex);
        column.add(gem);
    }


    private void addVertical(List<Gem> gems, int columnIndex){
        List<Gem> column = gemColumns.get(columnIndex);

        for(Gem gem : gems){
            int rowIndex = column.size();
            setGemCoordinatesToGridPosition(gem, rowIndex, columnIndex);
            column.add(gem);
        }
    }


    private void setGemCoordinatesToGridPosition(Gem gem, int rowIndex, int columnIndex){
        float gemX = startingX + (gemSize * columnIndex);
        float gemY = floorY - ((rowIndex + 1) * gemSize);
        gem.setXY(gemX, gemY);
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


    public void dropGems(){
        for(List<Gem> column : gemColumns){
            dropGemsInColumn(column);
        }
    }


    private void dropGemsInColumn(List<Gem> column){
        for(int i =0; i< column.size(); i++){
            dropGemIfAboveGridPosition(column.get(i), i);
        }
    }


    private void dropGemIfAboveGridPosition(Gem gem, int actualRowIndex){
        float gridTopY = getYForRowTop(actualRowIndex);
        if(gem.getY() < gridTopY){
            float distance = Math.min(gridTopY - gem.getY(), dropIncrement);
            dropGem(gem, distance);
        }
    }


    private void dropGem(Gem gem, float distance){
        gem.incY(distance);
        haveAnyGemsMovedDuringLastDrop = true;
    }


    private float getYForRowTop(int rowIndex){
        return this.floorY - ((1 + rowIndex) * gemSize);
    }


    public boolean isStable(){
        boolean isStable = !haveAnyGemsMovedDuringLastDrop;
        haveAnyGemsMovedDuringLastDrop = false;
        return isStable;
    }

}
