package com.jcrawleydev.gemsdrop.instructions;

import static com.jcrawleydev.gemsdrop.game.gem.GemColor.BLUE;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.GREEN;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.YELLOW;

import android.graphics.RectF;

import com.jcrawleydev.gemsdrop.game.gem.dropping.DroppingGems;
import com.jcrawleydev.gemsdrop.game.grid.GridProps;

import java.util.List;

public class GameInstructions {

    private DroppingGems droppingGems;
    private boolean areDroppingGemsVisible;
    private InstructionsView view;
    private int currentInstructionIndex;
    private List<Instruction> instructions;




    public GameInstructions(){
        initDroppingGems();
        setupMap();
    }


    public int getCurrentIndex(){
        return currentInstructionIndex;
    }


    public void setView(InstructionsView view){
        this.view = view;
    }


    private void setupMap(){
        float xStart = 0;
        float xRotateStart = 0.3f;
        float xRotateEnd = 0.6f;
        float xEnd = 1;

        float yStart = 0;
        float yDropStart = 0.8f;
        float yEnd = 1f;

        instructions.add(new Instruction(3,
                new RectF(xRotateEnd, yStart, xEnd, yDropStart) ,
                this::moveRight,
                this));
        instructions.add(new Instruction(3,
                new RectF(xStart, yStart, xRotateStart, yDropStart) ,
                this::moveLeft,
                this));
        instructions.add(new Instruction(3,
                new RectF(xRotateStart, yStart, xRotateEnd, yDropStart) ,
                this::rotate,
                this));
        instructions.add(new DropInstruction(
                new RectF(xStart, yDropStart, xEnd, yEnd),
                this));
    }


    public void onClick(float x, float y){
       instructions.get(currentInstructionIndex).onClick();
    }


    public void initCurrentInstruction(){
        instructions.get(currentInstructionIndex).init();
    }


    public void setClickBoundsOnView(RectF bounds){
        view.setClickBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }



    private void moveLeft(){
        droppingGems.moveLeft();
        view.updateGems();
    }



    private void moveRight(){
        droppingGems.moveRight();
        view.updateGems();
    }



    private void rotate(){
        droppingGems.rotate();
        view.updateGems();
    }


    public void drop(){
        droppingGems.moveDown();
        view.updateGems();
    }


    public void initDroppingGems(){
        var gridProps = new GridProps(18, 7, 2);
        droppingGems = new DroppingGems(gridProps, List.of(BLUE, GREEN, YELLOW));
    }


    public boolean areGemsVisible(){
        return areDroppingGemsVisible;
    }


    public void moveToNextInstruction(){
        if(currentInstructionIndex < instructions.size() -1){
            currentInstructionIndex++;
        }
    }


    /*
        - fragment loads for the first time
            - instruction index is 0, first instruction is loaded, bounds etc
            - show gems on view, show first instruction (no animation for now, just display)
            - click, click, click, gem moves, right, right, right
            - next instruction is queued (i.e. instruction index is incremented)
            - gems (layout) fades away, instruction1 fades away
            - on animation done, gems are reinitialized, updated on view, next instruction is loaded

     */


}
