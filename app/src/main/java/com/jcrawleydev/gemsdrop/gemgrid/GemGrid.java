package com.jcrawleydev.gemsdrop.gemgrid;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.DrawItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;


public class GemGrid {

    private List<List<Gem>> gemColumns;
    private final int NUMBER_OF_ROWS;
    private final int NUMBER_OF_COLUMNS;
    private int gemSize;
    private int floorY;
    private int startingX;
    private int dropIncrement;
    private boolean haveAnyGemsMovedDuringLastDrop;

    public GemGrid(int numberOfColumns, int numberOfRows){
        NUMBER_OF_ROWS = numberOfRows;
        NUMBER_OF_COLUMNS = numberOfColumns;
        initColumns();
    }

    public List<List<Gem>> getGemColumns(){
        return this.gemColumns;
    }

    public void setGemSize(int gemSize){
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


    public boolean shouldAdd(GemGroup gemGroup) {
        final int FLOOR_POSITON = 1;
        if(gemGroup.getBottomPosition() <= FLOOR_POSITON){
            return true;
        }
        if(gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL){
            return areAllGemsConnectingToColumns(gemGroup);
        }
        return gemGroup.getBottomPosition() <= getColumnHeight(gemGroup.getPosition());
    }


    public boolean addAnyFrom(GemGroup gemGroup){
        if(isVertical(gemGroup)) {
            return false;
        }
        boolean hasGemBeenAdded = false;
        List<Gem> gems = gemGroup.getGridGems();

        for(int i=0, position = gemGroup.getBasePosition(); i< gems.size();i++, position ++){
            Gem gem = gems.get(i);
            if(gemColumns.get(position).size() >= gemGroup.getBottomPosition()){
                add(gem, position);
                hasGemBeenAdded = true;
            }
        }
        return hasGemBeenAdded;
    }

    public List<Gem.Color> getRemainingColors(){
        Set<Gem.Color> colors = new HashSet<>();

        for(List<Gem> column : gemColumns){
            for(Gem gem: column){
                colors.add(gem.getColor());
            }
        }
        System.out.println("Number of colors: " + colors.size());
        return new ArrayList<>(colors);
    }


    boolean isVertical(GemGroup gemGroup){
        return gemGroup.getOrientation() == GemGroup.Orientation.VERTICAL;
    }


    private boolean areAllGemsConnectingToColumns(GemGroup gemGroup){
        for(int position : gemGroup.getGemPositions()){
            if(gemGroup.getBottomPosition() > getColumnHeight(position)){
                return false;
            }
        }
        return true;
    }


    public int getColumnHeight(int position){
        return gemColumns.get(position).size();
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


    public void add(GemGroup gemGroup) {
        List<Gem> gems = gemGroup.getCopyOfGemsToAddToGrid();
        int position = gemGroup.getPosition();

        if (gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL) {
            addHorizontal(gems, position);
        } else {
            addVertical(gems, position);
        }
    }


    public void add(Gem gem, int position){
        addHorizontal(Collections.singletonList(gem.clone()), position);
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
        int gemX = startingX + (gemSize * columnIndex);
        int gemY = floorY - ((rowIndex + 1) * gemSize);
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
        if(gem.getY() < getYForRowTop(actualRowIndex)){
            dropGem(gem);
        }
    }


    private void dropGem(Gem gem){
        gem.incY(dropIncrement);
        haveAnyGemsMovedDuringLastDrop = true;
    }


    private int getYForRowTop(int rowIndex){
        return this.floorY - ((1 + rowIndex) * gemSize);
    }



    public boolean isStable(){
        boolean isStable = !haveAnyGemsMovedDuringLastDrop;
        haveAnyGemsMovedDuringLastDrop = false;
        return isStable;
    }

}
