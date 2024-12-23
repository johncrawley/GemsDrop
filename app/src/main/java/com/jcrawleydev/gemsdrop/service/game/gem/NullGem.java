package com.jcrawleydev.gemsdrop.service.game.gem;

public class NullGem extends Gem {

    public NullGem(){
        super(GemColor.BLUE, GemGroupPosition.CENTRE, -1);
        this.color = null;
    }

}
