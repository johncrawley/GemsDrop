package com.jcrawleydev.gemsdrop.game.gem;


import com.jcrawleydev.gemsdrop.game.GridProps;
import com.jcrawleydev.gemsdrop.game.level.GameLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DroppingGemsFactory {

    private GridProps gridProps;
    private final Random random;
    private GameLevel gameLevel;
    private List<GemColor> gemColors;
    private int numberOfNormalGemsDropped;


    public DroppingGemsFactory(){
        random = new Random(System.currentTimeMillis());
    }


    public void setGridProps(GridProps gridProps){
        this.gridProps = gridProps;
    }


    public void setLevel(GameLevel level){
        this.gameLevel = level;
        this.gemColors = new ArrayList<>(level.gemColors());
    }


    public DroppingGems createDroppingGems(){
        if(shouldCreateWonderGem()){
            numberOfNormalGemsDropped = 0;
            return new WonderDroppingGem(gridProps);
        }
        return new DroppingGems(gridProps, getRandomGemColors());
    }


    private boolean shouldCreateWonderGem(){
        return (haveEnoughNormalGemsDropped() && isLucky())
                || haveTooManyNormalGemsDropped();
    }


    public boolean haveEnoughNormalGemsDropped(){
        numberOfNormalGemsDropped++;
        return numberOfNormalGemsDropped >= gameLevel.specialGemConditions().minNormalGemStreak();
    }


    public boolean haveTooManyNormalGemsDropped(){
       return numberOfNormalGemsDropped > gameLevel.specialGemConditions().maxNormalGemStreak();
    }


    private boolean isLucky(){
        int odds = gameLevel.specialGemConditions().specialGemOdds();
        return random.nextInt(odds) == 1;
    }


    public List<GemColor> getRandomGemColors(){
        return List.of(getRandomColor(), getRandomColor(), getRandomColor());
    }


    public GemColor getRandomColor(){
        int index = random.nextInt(gemColors.size());
        return gemColors.get(index);
    }

}
