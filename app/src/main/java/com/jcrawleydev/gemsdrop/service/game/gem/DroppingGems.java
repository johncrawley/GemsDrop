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
import java.util.Map;
import java.util.Random;

public class DroppingGems {

    private final GridProps gridProps;
    private final List<Gem> gems;
    private final int INITIAL_NUMBER_OF_GEMS = 3;
    private final Random random;
    private Gem bottomGem, centreGem, topGem;
    enum Orientation { NORTH, EAST, SOUTH, WEST }
    private Orientation orientation = NORTH;
    private final List<GemColor> gemColors = List.of(GemColor.RED, GemColor.BLUE, GemColor.PURPLE, GemColor.GREEN, GemColor.YELLOW);
    private Map<String, Gem> originalGems;



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
        printGemDetails();
    }


    private void rotateGem(Gem gem){
        gem.rotateClockwise();
    }


    public void addConnectingGemsTo(GemGrid gemGrid){
        addIfConnecting(bottomGem, gemGrid);
        addIfConnecting(centreGem, gemGrid);
        addIfConnecting(topGem, gemGrid);
    }


    private void addIfConnecting(Gem gem, GemGrid gemGrid){
        if(!gem.isAddedToGrid()){
            gemGrid.addIfConnecting(gem);
        }
    }


    public boolean areAnyAddedToGrid(){
        return bottomGem.isAddedToGrid() || centreGem.isAddedToGrid() || topGem.isAddedToGrid();
    }


    public boolean areAllAddedToGrid(){
        return bottomGem.isAddedToGrid() && centreGem.isAddedToGrid() && topGem.isAddedToGrid();
    }


    public int getLowestGemPosition(){
        return bottomGem.getContainerPosition();
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
            printGemDetails();
        }
    }


    public void moveRight(){
        if(getRightmostColumn() < gridProps.numberOfColumns() -1){
            getFreeGems().forEach(Gem::moveRight);
            printGemDetails();
        }
    }


    public List<Gem> getFreeGems(){
        return gems.stream().filter(gem -> !gem.isAddedToGrid()).toList();
    }


    public void moveUp(){
        getFreeGems().forEach(Gem::moveUp);
        printGemDetails();
    }


    public void moveDown(){
        getFreeGems().forEach(Gem::moveDown);
        printGemDetails();
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

        originalGems = Map.of("originalTopGem", topGem, "originalCentreGem", centreGem, "originalBottomGem", bottomGem);

        gems.add(topGem);
        gems.add(centreGem);
        gems.add(bottomGem);
        gems.forEach(g -> g.setColumn(gridProps.numberOfColumns()/2));
        log("createGems() gems created about to log initial gem positions: ");
        log("exiting createGems()");
    }


    private void printGemDetails(){
        log("************** gem details ****************");
        for(String gemName: originalGems.keySet()){
            Gem gem = originalGems.get(gemName);
            if(gem == null) continue;
            log("-->: " + gemName + " color: " + gem.getColor().toString() + " column: " + gem.getColumn() + " containerPosition: " + gem.getContainerPosition());
        }
        log("*******************************************");
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
