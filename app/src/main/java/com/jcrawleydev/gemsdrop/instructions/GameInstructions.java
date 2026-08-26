package com.jcrawleydev.gemsdrop.instructions;

import static com.jcrawleydev.gemsdrop.game.gem.GemColor.BLUE;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.GREEN;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.YELLOW;

import com.jcrawleydev.gemsdrop.game.gem.dropping.DroppingGems;
import com.jcrawleydev.gemsdrop.game.grid.GridProps;

import java.util.ArrayList;
import java.util.List;

public class GameInstructions {

    private DroppingGems droppingGems;
    private boolean areDroppingGemsVisible;
    private InstructionsView view;
    private int currentInstructionIndex;
    private List<Instruction> instructions;


    public GameInstructions(){
        initDroppingGems();
        initInstructions();
    }


    public int getCurrentIndex(){
        return currentInstructionIndex;
    }


    public void setView(InstructionsView view){
        this.view = view;
    }


    private void initInstructions(){
        instructions = new ArrayList<>();
        instructions.add(new Instruction(3,
                this::moveRight,
                this));
        instructions.add(new Instruction(3,
                this::moveLeft,
                this));
        instructions.add(new Instruction(3,
                this::rotate,
                this));
        instructions.add(new DropInstruction(this));
    }


    public void onClick(float x, float y){
       instructions.get(currentInstructionIndex).onClick();
    }


    public void initCurrentInstruction(){
        instructions.get(currentInstructionIndex).init();
    }


    private void moveLeft(){
        droppingGems.moveLeft();
        updateGems();
    }


    private void moveRight(){
        droppingGems.moveRight();
        updateGems();
    }



    private void rotate(){
        droppingGems.rotate();
        updateGems();
    }


    public void drop(){
        droppingGems.moveDown();
        updateGems();
    }

    private void updateGems(){
        if(view != null){
            view.updateGems();
        }
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
            if(view != null){
                view.start(currentInstructionIndex);
            }
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
