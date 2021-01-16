package com.jcrawleydev.gemsdrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemRotater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GemGroupView {

    private TransparentView transparentView;
    private int GEM_WIDTH, HALF_WIDTH;
    private Map<Gem.Color, Bitmap> gemColorMap;
    private BitmapLoader bitmapLoader;
    private GemGroup gemGroup;
    private int numberOfGems;
    private GemRotater gemRotater;

    public GemGroupView(View view, Context context, GemGroup gemGroup){
        transparentView = (TransparentView)view;
        bitmapLoader = new BitmapLoader(context);
        linkBitmapsToColorsAndAssignWidths();
        gemRotater = new GemRotater(gemGroup, GEM_WIDTH);
        setGemGroup(gemGroup);
    }


    public void setGemGroup(GemGroup gemGroup){
        this.gemGroup = gemGroup;
        numberOfGems = gemGroup.getGems().size();
        setBitmapReferences();
        gemRotater.setGemCoordinates(gemGroup);
        setDrawItems();
        gemGroup.setDropIncrement(HALF_WIDTH);
    }


    public void updateAndDraw(){
        transparentView.setTranslateY(gemGroup.getY());
        transparentView.updateAndDraw();
        transparentView.invalidate();

    }

    public void rotate(){
        gemRotater.rotate();
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


    public void setDrawItems(){
        List<DrawItem> drawItemList = new ArrayList<>(gemGroup.getGems().size());
        for(Gem gem: gemGroup.getGems()){
            drawItemList.add(gem);
        }

        transparentView.setDrawItems(drawItemList);
    }


}
