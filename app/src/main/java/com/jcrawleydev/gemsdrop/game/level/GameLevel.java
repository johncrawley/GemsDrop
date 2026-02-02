package com.jcrawleydev.gemsdrop.game.level;

import com.jcrawleydev.gemsdrop.game.gem.GemColor;

import java.util.List;
import java.util.Set;

public record GameLevel(int number,
                        int backgroundResourceId,
                        int startingDropDuration,
                        int endingDropDuration,
                        Set<GemColor> gemColors,
                        SpecialGemConditions specialGemConditions,
                        List<String> startingGrid){

}
