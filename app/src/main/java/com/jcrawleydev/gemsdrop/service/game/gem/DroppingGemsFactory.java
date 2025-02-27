package com.jcrawleydev.gemsdrop.service.game.gem;


import com.jcrawleydev.gemsdrop.service.game.GridProps;
import com.jcrawleydev.gemsdrop.service.game.level.SpecialGemConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DroppingGemsFactory {

    private final GridProps gridProps;
    private int numberOfNormalGemsDropped;
    private final Random random;
    private int minNormalGemStreak = 8;
    private int maxNormalGemStreak = 25;
    private int specialGemOdds = 10;
    private List<GemColor> gemColors;

    public DroppingGemsFactory(GridProps gridProps){
        this.gridProps = gridProps;
        random = new Random(System.currentTimeMillis());
    }

    public void setSpecialGemConditions(SpecialGemConditions specialGemConditions){
        minNormalGemStreak = specialGemConditions.minNormalGemStreak();
        maxNormalGemStreak = specialGemConditions.maxNormalGemStreak();
        specialGemOdds = specialGemConditions.specialGemOdds();
    }


    public DroppingGems createDroppingGems(){
        if(++numberOfNormalGemsDropped < minNormalGemStreak
                || isUnlucky()
                || numberOfNormalGemsDropped < maxNormalGemStreak){
            return new DroppingGems(gridProps, getRandomGemColors());
        }
        numberOfNormalGemsDropped = 0;
        return new WonderDroppingGem(gridProps);
    }


    public void setGemColors(Set<GemColor> gemColors){
        this.gemColors = new ArrayList<>(gemColors);
    }


    private boolean isUnlucky(){
        return random.nextInt(specialGemOdds) != 1;
    }

    public List<GemColor> getRandomGemColors(){
        return List.of(getRandomColor(), getRandomColor(), getRandomColor());
    }

    public GemColor getRandomColor(){
        int index = random.nextInt(gemColors.size());
        return gemColors.get(index);
    }

}
