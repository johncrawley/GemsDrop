package com.jcrawleydev.gemsdrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GemGroupView {

    private TransparentView transparentView;
    private int GEM_WIDTH, HALF_WIDTH;
    private Map<Gem.Color, Bitmap> gemColorMap;
    private BitmapLoader bitmapLoader;
    private GemGroup gemGroup;
    private int numberOfGems;

    public GemGroupView(View view, Context context, GemGroup gemGroup){
        transparentView = (TransparentView)view;
        bitmapLoader = new BitmapLoader(context);
        linkBitmapsToColorsAndAssignWidths();
        setGemGroup(gemGroup);
    }


    public void setGemGroup(GemGroup gemGroup){
        this.gemGroup = gemGroup;
        numberOfGems = gemGroup.getGems().size();
        assignXCoordinatesToGems();
        setBitmapReferences();
        setDrawItems();
        assignYCoordinateToGems();
        gemGroup.setDropIncrement(HALF_WIDTH);
    }

    private void assignYCoordinateToGems(){
        int yOffset = - HALF_WIDTH;
        for(Gem gem: gemGroup.getGems()){
            gem.setY(yOffset);
        }
    }

    private void assignXCoordinatesToGems(){
        int initialX = - HALF_WIDTH - (numberOfGems / 2 * GEM_WIDTH);
        for(int i = 0; i < numberOfGems; i++){
            int x = initialX + (i * GEM_WIDTH);
            gemGroup.getGems().get(i).setX(x);
        }
    }


    private void setBitmapReferences(){
        for(Gem gem: gemGroup.getGems()){
            gem.setBitmap(gemColorMap.get(gem.getColor()));
        }
    }

    private void linkBitmapsToColorsAndAssignWidths(){
        gemColorMap = new HashMap<>();
        link(Gem.Color.BLUE, R.drawable.jewel_blue);
        link(Gem.Color.YELLOW, R.drawable.jewel_yellow);
        link(Gem.Color.GREEN, R.drawable.jewel_green);
        link(Gem.Color.RED, R.drawable.jewel_red);
    }


    private void link(Gem.Color color, int drawableId){
        gemColorMap.put(color, bitmapLoader.get(drawableId));
        Bitmap bm = bitmapLoader.get(drawableId);
        assignWidths(bm);
    }

    private void assignWidths(Bitmap bm){
        GEM_WIDTH = bm.getWidth();
        HALF_WIDTH = GEM_WIDTH /2;
    }


    public void updateAndDraw(){
        transparentView.setTranslateY(gemGroup.getY());
        transparentView.updateAndDraw();
        transparentView.invalidate();

    }

    public void setDrawItems(){
        transparentView.setDrawItems(new ArrayList<>(gemGroup.getGems()));
    }


}
