package com.jcrawleydev.gemsdrop.service;

import static com.jcrawleydev.gemsdrop.service.DroppingGems.Orientation.EAST;
import static com.jcrawleydev.gemsdrop.service.DroppingGems.Orientation.NORTH;
import static com.jcrawleydev.gemsdrop.service.DroppingGems.Orientation.SOUTH;
import static com.jcrawleydev.gemsdrop.service.DroppingGems.Orientation.WEST;
import static com.jcrawleydev.gemsdrop.service.GemUtils.convertDepthToHeight;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.GemColor;
import com.jcrawleydev.gemsdrop.gem.GemPosition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;

public class DroppingGems {

    private final List<Gem> gems;
    private final int INITIAL_NUMBER_OF_GEMS = 3;
    private final Random random;
    private final int numberOfRows, numberOfColumns;
    private Gem bottomGem, centreGem, topGem;
    enum Orientation { NORTH, EAST, SOUTH, WEST }
    private Orientation orientation = Orientation.NORTH;
    private final List<GemColor> gemColors = List.of(GemColor.RED, GemColor.BLUE, GemColor.PURPLE, GemColor.GREEN, GemColor.YELLOW);


    public DroppingGems(int numberOfRows, int numberOfColumns){
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        random = new Random(System.currentTimeMillis());
        gems = new ArrayList<>(INITIAL_NUMBER_OF_GEMS);
    }


    public void rotate(){
        updateOrientation();
        for(Gem gem: gems){
            gem.rotate();
        }
    }


    public int getLowestHeight(){
        OptionalInt lowestPoint = gems.stream().mapToInt(Gem::getDepth).max();
        if(lowestPoint.isPresent()){
            return convertDepthToHeight(lowestPoint.getAsInt(), numberOfRows);
        }
        return 0;
    }


    public int getBottomDepth(){
       return getBottomGem().getBottomDepth();
    }


    public void setColumn(int columnIndex){
        for(Gem gem: gems){
            gem.setColumn(columnIndex);
        }
    }


    private boolean canRotate(){
        return false;
    }


    public boolean isOrientationVertical(){
        return orientation == NORTH || orientation == SOUTH;
    }


    private void updateOrientation(){
        orientation = switch (orientation){
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }


    public void moveLeft(){
        if(getLeftmostColumn() > 0){
            gems.forEach(Gem::moveLeft);
        }
    }


    public void moveRight(){
        if(getRightmostColumn() < numberOfColumns -1){
            gems.forEach(Gem::moveRight);
        }
    }


    public int getLeftmostColumn(){
        return switch (orientation){
            case NORTH, SOUTH -> centreGem.getColumn();
            case EAST -> bottomGem.getColumn();
            case WEST -> topGem.getColumn();
        };
    }


    public int getRightmostColumn(){
        return switch (orientation){
            case NORTH, SOUTH -> centreGem.getColumn();
            case EAST -> topGem.getColumn();
            case WEST -> bottomGem.getColumn();
        };
    }


    public Gem getBottomGem(){
        return switch (orientation){
            case NORTH -> topGem;
            case SOUTH -> bottomGem;
            case EAST, WEST -> centreGem;
        };
    }


    public Gem getCentreGem(){
        return centreGem;
    }


    public void create(){
        gems.clear();
        orientation = NORTH;
        createGems();
    }


    private void createGems(){
        topGem = new Gem(getRandomColor(), GemPosition.TOP, -6);
        centreGem = new Gem(getRandomColor(), GemPosition.CENTRE, -4);
        bottomGem = new Gem(getRandomColor(), GemPosition.BOTTOM, -2);
        gems.add(topGem);
        gems.add(centreGem);
        gems.add(bottomGem);
        gems.forEach(g -> g.setColumn(numberOfColumns/2));
    }


    public void drop(){
        gems.forEach(Gem::incDepth);
    }


    public void setGems(List<Gem> remainingGems){
        gems.clear();
        gems.addAll(remainingGems);
    }


    public boolean isEmpty(){
        return gems.isEmpty();
    }


    public boolean haveReducedInNumber(){
        return gems.size() < INITIAL_NUMBER_OF_GEMS;
    }


    public List<Gem> get(){
        return new ArrayList<>(gems);
    }


    public GemColor getRandomColor(){
        int index = random.nextInt(gemColors.size());
        return gemColors.get(index);
    }

}
