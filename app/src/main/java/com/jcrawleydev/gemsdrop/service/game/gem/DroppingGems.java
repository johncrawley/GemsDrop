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

public class DroppingGems {

    private final GridProps gridProps;
    private final List<Gem> gems;
    private final int INITIAL_NUMBER_OF_GEMS = 3;
    private final Random random;
    private Gem gemC, gemB, gemA;
    enum Orientation { NORTH, EAST, SOUTH, WEST }
    private Orientation orientation = NORTH;
    private final List<GemColor> gemColors = List.of(GemColor.RED, GemColor.BLUE, GemColor.PURPLE, GemColor.GREEN, GemColor.YELLOW);
    //private Map<String, Gem> originalGems;
    private final int middleColumnIndex;
    private final int INITIAL_POSITION;



    public DroppingGems(GridProps gridProps){
        this.gridProps = gridProps;
        random = new Random(System.currentTimeMillis());
        gems = new ArrayList<>(INITIAL_NUMBER_OF_GEMS);
        middleColumnIndex = gridProps.numberOfColumns() / 2;
        INITIAL_POSITION = gridProps.numberOfPositions() - 1;
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


    public void addConnectingGemsTo(GemGrid gemGrid){
        /*
        if(isOrientationVertical()){
            gemGrid.addIfConnecting(bottomGem, centreGem, topGem);
            return;
        }

         */
        addIfConnecting(getBottomGem(), gemGrid);
        addIfConnecting(getCentreGem(), gemGrid);
        addIfConnecting(getTopGem(), gemGrid);
    }


    private void addIfConnecting(Gem gem, GemGrid gemGrid){
        if(!gem.isAlreadyAddedToTheGrid()){
            gemGrid.addIfConnecting(gem);
        }
    }


    public boolean areAnyAddedToGrid(){
        return gemA.isAlreadyAddedToTheGrid()
                || gemB.isAlreadyAddedToTheGrid()
                || gemC.isAlreadyAddedToTheGrid();
    }


    public boolean areAllAddedToGrid(){
        return gemA.isAlreadyAddedToTheGrid()
                && gemB.isAlreadyAddedToTheGrid()
                && gemC.isAlreadyAddedToTheGrid();
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
            getFreeGems().forEach(Gem::moveLeft);
        }
    }


    public void moveRight(){
        if(getRightmostColumn() < gridProps.numberOfColumns() -1){
            getFreeGems().forEach(Gem::moveRight);
        }
    }


    public List<Gem> getFreeGems(){
        return gems.stream().filter(gem -> !gem.isAlreadyAddedToTheGrid()).toList();
    }


    public long[] getFreeGemIds(){
        return gems.stream().filter(gem -> !gem.isAlreadyAddedToTheGrid()).mapToLong(Gem::getId).toArray();
    }


    public void moveUp(){
        getFreeGems().forEach(Gem::moveUp);
    }


    public void moveDown(){
        getFreeGems().forEach(Gem::moveDown);
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
        return orientation == NORTH ? gemA : gemC;
    }


    public Gem getBottomGem(){
        return orientation == NORTH ? gemC : gemA;
    }


    public void create(){
        gems.clear();
        orientation = NORTH;
        createGems();
    }


    private void createGems(){
        gemA = createGem(TOP, 3);
        gemB = createGem(CENTRE, 2);
        gemC = createGem(BOTTOM, 1);

        //originalGems = Map.of("originalTopGem", topGem, "originalCentreGem", centreGem, "originalBottomGem", bottomGem);

        gems.add(gemA);
        gems.add(gemB);
        gems.add(gemC);
        gems.forEach(g -> g.setColumn(middleColumnIndex));
    }


    private Gem createGem(GemGroupPosition gemGroupPosition, int offset){
        int initialContainerPosition = INITIAL_POSITION + (gridProps.depthPerDrop() * offset);
        return new Gem(getRandomColor(), gemGroupPosition, initialContainerPosition);
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
