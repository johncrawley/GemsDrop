package com.jcrawleydev.gemsdrop.service.game.gem;

import static com.jcrawleydev.gemsdrop.service.game.gem.GemGroupPosition.BOTTOM;
import static com.jcrawleydev.gemsdrop.service.game.gem.GemGroupPosition.CENTRE;
import static com.jcrawleydev.gemsdrop.service.game.gem.GemGroupPosition.TOP;
import static com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems.Orientation.EAST;
import static com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems.Orientation.NORTH;
import static com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems.Orientation.SOUTH;
import static com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems.Orientation.WEST;
import static com.jcrawleydev.gemsdrop.service.game.gem.GemUtils.convertContainerPositionToGridHeight;

import com.jcrawleydev.gemsdrop.service.game.GridProps;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;

public class DroppingGems {

    private final GridProps gridProps;
    private final List<Gem> gems;
    private final int INITIAL_NUMBER_OF_GEMS = 3;
    private final Random random;
    private Gem bottomGem, centreGem, topGem;
    enum Orientation { NORTH, EAST, SOUTH, WEST }
    private Orientation orientation = Orientation.NORTH;
    private final List<GemColor> gemColors = List.of(GemColor.RED, GemColor.BLUE, GemColor.PURPLE, GemColor.GREEN, GemColor.YELLOW);


    public DroppingGems(GridProps gridProps){
        this.gridProps = gridProps;
        random = new Random(System.currentTimeMillis());
        gems = new ArrayList<>(INITIAL_NUMBER_OF_GEMS);
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


    public int getLowestHeight(){
        OptionalInt lowestPoint = gems.stream().mapToInt(Gem::getContainerPosition).max();
        if(lowestPoint.isPresent()){
            return convertContainerPositionToGridHeight(lowestPoint.getAsInt(), gridProps.numberOfRows());
        }
        return 0;
    }


    public int getBottomDepth(){
       return getBottomGem().getContainerPosition() + gridProps.depthPerDrop();
    }


    public int getBottomHeight(){
      //  return getBottomHeightOf(getBottomGem(), gridProps);
        return 0;
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
            gems.forEach(Gem::moveLeft);
        }
    }


    public void moveRight(){
        if(getRightmostColumn() < gridProps.numberOfColumns() -1){
            gems.forEach(Gem::moveRight);
        }
    }


    public void moveUp(){
        gems.forEach(Gem::moveUp);
    }


    public void moveDown(){
        gems.forEach(Gem::moveDown);
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


    public Gem getRightmostGem(){
        return switch (orientation){
            case NORTH, SOUTH -> centreGem;
            case EAST -> topGem;
            case WEST -> bottomGem;
        };
    }


    public Gem getCentreGem(){
        return centreGem;
    }


    public Gem getTopGem(){
        return centreGem;
    }


    public Gem getBottomGem(){
        return switch (orientation){
            case NORTH -> bottomGem;
            case SOUTH -> topGem;
            case EAST, WEST -> centreGem;
        };
    }


    public void create(){
        gems.clear();
        orientation = NORTH;
        createGems();
    }


    private void createGems(){
        topGem = createGem(TOP, 3);
        centreGem = createGem(CENTRE, 2);
        bottomGem = createGem(BOTTOM, 1);

        gems.add(topGem);
        gems.add(centreGem);
        gems.add(bottomGem);
        gems.forEach(g -> g.setColumn(gridProps.numberOfColumns()/2));
        log("createGems() gems created about to log initial gem positions: ");
        log("exiting createGems()");
    }


    private Gem createGem(GemGroupPosition gemGroupPosition, int offset){
        int initialPosition = gridProps.numberOfPositions() - 1;
        log("createGem() gridProps.depthsPerDrop: " + gridProps.depthPerDrop());
        int initialContainerPosition = initialPosition + (gridProps.depthPerDrop() * offset);
        log("createGem() offsetPosition: " + initialContainerPosition);
        return new Gem(getRandomColor(), gemGroupPosition, initialContainerPosition);
    }


    private void log(String msg){
        System.out.println("^^^ DroppingGems: " + msg);
    }

    public void drop(){
        gems.forEach(Gem::moveDown);
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
