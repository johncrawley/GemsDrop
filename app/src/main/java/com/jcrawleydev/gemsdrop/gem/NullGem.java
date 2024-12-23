package com.jcrawleydev.gemsdrop.gem;

import com.jcrawleydev.gemsdrop.service.game.gem.GemColor;

public class NullGem extends Gem{

    public NullGem(){
        super(GemColor.BLUE);
        this.color = null;
    }

}
