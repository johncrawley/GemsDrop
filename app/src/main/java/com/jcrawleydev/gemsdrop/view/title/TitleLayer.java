package com.jcrawleydev.gemsdrop.view.title;

import android.graphics.Bitmap;
import android.view.View;

import com.jcrawleydev.gemsdrop.gem.GemColor;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.BitmapLoader;
import com.jcrawleydev.gemsdrop.view.TransparentView;
import com.jcrawleydev.gemsdrop.view.UpdatableView;
import com.jcrawleydev.gemsdrop.view.item.DrawableItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TitleLayer implements UpdatableView {

    private final TransparentView transparentView;
    private Map<GemColor, Bitmap> gemColorMap;
    private final BitmapLoader bitmapLoader;
    private GemGroup gemGroup;


    public TitleLayer(View view, BitmapLoader bitmapLoader){
        transparentView = (TransparentView)view;
        this.bitmapLoader = bitmapLoader;
    }


    public void setDrawItems(){
        transparentView.clearDrawableItems();
        List<DrawableItem> gems = new ArrayList<>(gemGroup.getGems());
        transparentView.addDrawableItems(gems);
        transparentView.setTranslateY((int)gemGroup.getY());
    }


    @Override
    public void drawIfUpdated(){

        if(gemGroup.wasUpdated()) {
            transparentView.setTranslateY((int)gemGroup.getY());
            transparentView.setTranslateX((int)gemGroup.getX());
            transparentView.invalidate();
            gemGroup.setUpdated(false);
        }
    }




}