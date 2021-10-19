package com.jcrawleydev.gemsdrop.view;

import android.graphics.Bitmap;
import android.view.View;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.item.DrawableItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GemGroupLayer implements UpdatableView{

    private final TransparentView transparentView;
    private Map<Gem.Color, Bitmap> gemColorMap;
    private final BitmapLoader bitmapLoader;
    private GemGroup gemGroup;


    public GemGroupLayer(View view, BitmapLoader bitmapLoader){
        transparentView = (TransparentView)view;
        this.bitmapLoader = bitmapLoader;
        linkBitmapsToColorsAndAssignWidths();
    }


    public void setGemGroup(GemGroup gemGroup){
        this.gemGroup = gemGroup;
        setBitmapReferences();
        setDrawItems();
    }


    public GemGroup getGemGroup(){
        return this.gemGroup;
    }


    public void setDrawItems(){
        transparentView.clearDrawableItems();
        List<DrawableItem> gems = new ArrayList<>(gemGroup.getGems());
        transparentView.addDrawableItems(gems);
        transparentView.setTranslateY(gemGroup.getY());
    }


    @Override
    public void drawIfUpdated(){

        if(gemGroup.wasUpdated()) {
            transparentView.setTranslateY(gemGroup.getY());
            transparentView.setTranslateX(gemGroup.getX());
            transparentView.invalidate();
            gemGroup.setUpdated(false);
        }
    }


    public void wipe(){
        gemGroup.setGemsInvisible();
        transparentView.clearDrawableItems();
        transparentView.invalidate();
    }


    private void setBitmapReferences(){
        for(Gem gem: gemGroup.getGems()){
            gem.setBitmap(gemColorMap.get(gem.getColor()));
        }
    }


    private void linkBitmapsToColorsAndAssignWidths(){
        gemColorMap = new HashMap<>();
        link(Gem.Color.BLUE,   R.drawable.jewel_blue);
        link(Gem.Color.YELLOW, R.drawable.jewel_yellow);
        link(Gem.Color.GREEN,  R.drawable.jewel_green);
        link(Gem.Color.RED,    R.drawable.jewel_red);
    }


    private void link(Gem.Color color, int drawableId){
        gemColorMap.put(color, bitmapLoader.get(drawableId));
    }


}
