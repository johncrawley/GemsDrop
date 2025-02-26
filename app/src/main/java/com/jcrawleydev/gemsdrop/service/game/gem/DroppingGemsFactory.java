package com.jcrawleydev.gemsdrop.service.game.gem;


import com.jcrawleydev.gemsdrop.service.game.GridProps;

import java.util.Random;

public class DroppingGemsFactory {

    private final GridProps gridProps;
    private int numberOfNormalGemsDropped;
    private final Random random;

    public DroppingGemsFactory(GridProps gridProps){
        this.gridProps = gridProps;
        random = new Random(System.currentTimeMillis());
    }


    public DroppingGems createDroppingGems(){
        numberOfNormalGemsDropped++;
        int SPECIAL_GEM_THRESHOLD = 8;
        if(numberOfNormalGemsDropped < SPECIAL_GEM_THRESHOLD || isUnlucky()){
            return new DroppingGems(gridProps);
        }
        numberOfNormalGemsDropped = 0;
        return new WonderDroppingGem(gridProps);
    }


    private boolean isUnlucky(){
        int chanceOfAWonderGem = 10;
        return random.nextInt(chanceOfAWonderGem) != 1;
    }

}
