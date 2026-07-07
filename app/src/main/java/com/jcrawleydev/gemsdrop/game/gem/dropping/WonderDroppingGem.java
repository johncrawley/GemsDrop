package com.jcrawleydev.gemsdrop.game.gem.dropping;

import static com.jcrawleydev.gemsdrop.game.gem.GemGroupPosition.CENTRE;

import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.gem.GemColor;
import com.jcrawleydev.gemsdrop.game.grid.GridProps;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class WonderDroppingGem extends DroppingGems {

    public WonderDroppingGem(GridProps gridProps){
        super(gridProps, Collections.emptyList());
    }

    @Override
    public void create(List<GemColor> gemColors){
        gemB = createGem(CENTRE, 1, GemColor.WONDER);
        gems = List.of(gemB);
    }


    @Override
    public void rotate(){
        //do nothing
    }


    public Set<Long> addTo(GemGrid gemGrid){
        if(!gemB.isAlreadyAddedToTheGrid()){
            return gemGrid.getMarkedGemIdsFromTouching(gemB);
        }
        return Collections.emptySet();
    }


    public int getLeftmostColumn(){
        return gemB.getColumn();
    }


    @Override
    public int getRightmostColumn(){
        return gemB.getColumn();
    }


    @Override
    public Gem getRightmostGem(){
        return gemB;
    }


    @Override
    public Gem getTopGem(){
        return gemB;
    }


    @Override
    public Gem getBottomGem(){
        return gemB;
    }
}
