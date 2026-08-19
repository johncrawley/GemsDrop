package com.jcrawleydev.gemsdrop.instructions;

import static com.jcrawleydev.gemsdrop.game.gem.GemColor.BLUE;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.GREEN;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.YELLOW;

import com.jcrawleydev.gemsdrop.game.gem.dropping.DroppingGems;
import com.jcrawleydev.gemsdrop.game.grid.GridProps;

import java.util.List;

public class GameInstructions {

    private DroppingGems droppingGems;
    private boolean areDroppingGemsVisible;
    private InstructionStateName currentInstructionStateName, nextInstructionStateName;

    public GameInstructions(){
        initDroppingGems();
        currentInstructionStateName = InstructionStateName.MOVE_RIGHT;
    }


    public void initDroppingGems(){
        var gridProps = new GridProps(18, 7, 2);
        droppingGems = new DroppingGems(gridProps, List.of(BLUE, GREEN, YELLOW));
    }


    public boolean areGemsVisible(){
        return areDroppingGemsVisible;
    }


    public void moveRight(){
        droppingGems.moveRight();
    }


    public void moveToNextInstruction(){

    }


    /*

        - fragment loads
            - get GameInstructions instance from the viewModel
            - gameInstructions.getCurrentInstruction().start();

                [ current instruction starts, assigns next instruction, assigns screen bounds on view]

            - if user taps on the correct area of the screen
                - currentInstruction.executeMove()
                    - moveCounter goes up
                    - eachInstruction will have a move limit before calling GameInstructions to move to next instruction


     */


}
