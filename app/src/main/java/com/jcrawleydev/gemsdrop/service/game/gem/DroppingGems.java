package com.jcrawleydev.gemsdrop.service.game.gem;

import static com.jcrawleydev.gemsdrop.service.game.gem.GemGroupPosition.BOTTOM;
import static com.jcrawleydev.gemsdrop.service.game.gem.GemGroupPosition.CENTRE;
import static com.jcrawleydev.gemsdrop.service.game.gem.GemGroupPosition.TOP;
import static com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems.Orientation.EAST;
import static com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems.Orientation.NORTH;
import static com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems.Orientation.SOUTH;
import static com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems.Orientation.WEST;

import com.jcrawleydev.gemsdrop.service.game.GridProps;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class DroppingGems {

    private final GridProps gridProps;
    private List<Gem> gems;
    private final Random random;
    private Gem gemC, gemB, gemA;
    enum Orientation { NORTH, EAST, SOUTH, WEST }
    private Orientation orientation = NORTH;
    private final List<GemColor> gemColors = List.of(GemColor.RED, GemColor.BLUE, GemColor.PURPLE, GemColor.GREEN, GemColor.YELLOW);
    private final int middleColumnIndex;
    private final int INITIAL_POSITION;
    private boolean isWonderGem = false;


    public DroppingGems(GridProps gridProps){
        this.gridProps = gridProps;
        random = new Random(System.currentTimeMillis());
        int initialNumberOfGems = 3;
        gems = new ArrayList<>(initialNumberOfGems);
        middleColumnIndex = gridProps.numberOfColumns() / 2;
        INITIAL_POSITION = gridProps.numberOfPositions() - 1;
    }


    public void rotate(){
        if(isWonderGem){
            return;
        }
        updateOrientation();
        for(Gem gem: gems){
            rotateGem(gem);
        }
    }


    private void rotateGem(Gem gem){
        gem.rotateClockwise();
    }


    public void addConnectingGemsTo(GemGrid gemGrid){
        if(isWonderGem){
            addWonderGemIfConnecting(getCentreGem(), gemGrid );
            return;
        }
        addIfConnecting(getBottomGem(), gemGrid);
        addIfConnecting(getCentreGem(), gemGrid);
        addIfConnecting(getTopGem(), gemGrid);
    }


    private void addWonderGemIfConnecting(Gem wonderGem, GemGrid gemGrid){
        if(!wonderGem.isAlreadyAddedToTheGrid()){
            gemGrid.addWonderGemIfConnecting(wonderGem);
        }
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


    public boolean areInInitialPosition(){
        return getCentreGem().getContainerPosition() > INITIAL_POSITION;
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
        return gems.stream().filter(gem -> !gem.isAlreadyAddedToTheGrid()).toList();
    }


    private void forEachFreeGem(Consumer<Gem> consumer){
        gems.stream().filter(gem -> !gem.isAlreadyAddedToTheGrid()).forEach(consumer);
    }


    public void moveUp(){
        forEachFreeGem(Gem::moveUp);
    }


    public void moveDown(){
        forEachFreeGem(Gem::moveDown);
    }


    public int getLeftmostColumn(){
        if(isWonderGem){
            return getCentreGem().getColumn();
        }
        return orientation == EAST ? gemC.getColumn() : gemA.getColumn();
    }


    public int getRightmostColumn(){
        if(isWonderGem){
            return getCentreGem().getColumn();
        }
        return orientation == WEST ? gemC.getColumn() : gemA.getColumn();
    }


    public Gem getRightmostGem(){
        if(isWonderGem){
            return getCentreGem();
        }
        return orientation == EAST ? gemA : gemC;
    }


    public Gem getCentreGem(){
        return gemB;
    }


    public Gem getTopGem(){
        if(isWonderGem){
            return getCentreGem();
        }
        return  orientation == NORTH ? gemA : gemC;
    }


    public Gem getBottomGem(){
        if(isWonderGem){
            return getCentreGem();
        }
        return orientation == NORTH ? gemC : gemA;
    }


    public void create(int numberOfGemGroupsDropped){
        orientation = NORTH;
        if(shouldCreateWonderGem(numberOfGemGroupsDropped)){
            createWonderGem();
            return;
        }
        createNormalGems();
    }


    private void createNormalGems(){
        gemA = createGem(TOP, 3);
        gemB = createGem(CENTRE, 2);
        gemC = createGem(BOTTOM, 1);

        gems = List.of(gemA, gemB, gemC);
        isWonderGem = false;
    }


    private void createWonderGem(){
        gemB = createGem(CENTRE, 1, GemColor.BLUE);
        isWonderGem = true;
    }


    private Gem createGem(GemGroupPosition gemGroupPosition, int offset){
        return createGem(gemGroupPosition, offset, getRandomColor());
    }


    private Gem createGem(GemGroupPosition gemGroupPosition, int offset, GemColor gemColor){
        int initialContainerPosition = INITIAL_POSITION + (gridProps.depthPerDrop() * offset);
        var gem = new Gem(getRandomColor(), gemGroupPosition, initialContainerPosition);
        gem.setColumn(middleColumnIndex);
        return gem;
    }


    private boolean shouldCreateWonderGem(int numberOfGemGroupsDropped){
        if(numberOfGemGroupsDropped < 10){
            return false;
        }
        return random.nextInt(25) == 1;
    }


    private void log(String msg){
        System.out.println("^^^ DroppingGems: " + msg);
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


    public GemColor getRandomColor(){
        int index = random.nextInt(gemColors.size());
        return gemColors.get(index);
    }

}
