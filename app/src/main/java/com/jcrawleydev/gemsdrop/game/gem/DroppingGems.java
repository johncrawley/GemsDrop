package com.jcrawleydev.gemsdrop.game.gem;

import static com.jcrawleydev.gemsdrop.game.gem.GemGroupPosition.*;
import static com.jcrawleydev.gemsdrop.game.gem.DroppingGems.Orientation.*;

import com.jcrawleydev.gemsdrop.game.GridProps;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DroppingGems {

    private final GridProps gridProps;
    List<Gem> gems;
    Gem gemC, gemB, gemA;
    enum Orientation { NORTH, EAST, SOUTH, WEST }
    private Orientation orientation = NORTH;
    private final int middleColumnIndex;
    private final int INITIAL_POSITION;


    public DroppingGems(GridProps gridProps, List<GemColor> gemColors){
        this.gridProps = gridProps;
        middleColumnIndex = gridProps.numberOfColumns() / 2;
        INITIAL_POSITION = gridProps.numberOfPositions() - 1;
        create(gemColors);
    }


    public void rotate(){
        updateOrientation();
        for(Gem gem: gems){
            rotateGem(gem);
        }
    }


    private void rotateGem(Gem gem){
        gem.rotateClockwise();
    }


    public Set<Long> addConnectingGemsTo(GemGrid gemGrid){
        addIfConnecting(getBottomGem(), gemGrid);
        addIfConnecting(getCentreGem(), gemGrid);
        addIfConnecting(getTopGem(), gemGrid);
        return Collections.emptySet();
    }


    private void addIfConnecting(Gem gem, GemGrid gemGrid){
        if(!gem.isAlreadyAddedToTheGrid()){
            gemGrid.addIfConnecting(gem);
        }
    }


    public boolean areAnyAddedToGrid(){
        return gems.stream().anyMatch(Gem::isAlreadyAddedToTheGrid);
    }


    public boolean areAllAddedToGrid(){
        return gems.stream().allMatch(Gem::isAlreadyAddedToTheGrid);
    }


    public int getLowestGemPosition(){
        return getBottomGem().getContainerPosition();
    }


    public void setColumn(int columnIndex){
        for(Gem gem: gems){
            gem.setColumn(columnIndex);
        }
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
            forEachFreeGem(Gem::moveLeft);
        }
    }


    public void moveRight(){
        if(getRightmostColumn() < gridProps.numberOfColumns() -1){
            forEachFreeGem(Gem::moveRight);
        }
    }


    public List<Gem> getFreeGems(){
        return gems
                .stream()
                .filter(gem -> !gem.isAlreadyAddedToTheGrid())
                .collect(Collectors.toList());
    }


    private void forEachFreeGem(Consumer<Gem> consumer){
        gems.stream().filter(gem -> !gem.isAlreadyAddedToTheGrid()).forEach(consumer);
    }


    public void moveDown(){
        forEachFreeGem(Gem::moveDown);
    }


    public int getLeftmostColumn(){
        return orientation == EAST ? gemC.getColumn() : gemA.getColumn();
    }


    public int getRightmostColumn(){
        return orientation == WEST ? gemC.getColumn() : gemA.getColumn();
    }


    public Gem getRightmostGem(){
        return orientation == EAST ? gemA : gemC;
    }


    public Gem getCentreGem(){
        return gemB;
    }


    public Gem getTopGem(){
        return  orientation == NORTH ? gemA : gemC;
    }


    public Gem getBottomGem(){
        return orientation == NORTH ? gemC : gemA;
    }


    protected void create(List<GemColor> gemColors){
        gemA = createGem(TOP, 3, gemColors.get(0));
        gemB = createGem(CENTRE, 2, gemColors.get(1));
        gemC = createGem(BOTTOM, 1, gemColors.get(2));
        gems = List.of(gemA, gemB, gemC);
    }


    Gem createGem(GemGroupPosition gemGroupPosition, int offset, GemColor gemColor){
        int initialContainerPosition = INITIAL_POSITION + (gridProps.depthPerDrop() * offset);
        var gem = new Gem(gemColor, gemGroupPosition, initialContainerPosition);
        gem.setColumn(middleColumnIndex);
        return gem;
    }


    public void setGems(List<Gem> remainingGems){
        gems.clear();
        gems.addAll(remainingGems);
    }


    public List<Gem> get(){
        return new ArrayList<>(gems);
    }


    public void setGrey(){
        gems.forEach(Gem::setGrey);
    }

}
