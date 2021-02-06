package com.jcrawleydev.gemsdrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GemGroupView implements UpdatableView{

    private TransparentView transparentView;
    private int GEM_WIDTH;
    private Map<Gem.Color, Bitmap> gemColorMap;
    private BitmapLoader bitmapLoader;
    private GemGroup gemGroup;


    public GemGroupView(View view, Context context, GemGroup gemGroup){
        transparentView = (TransparentView)view;
        bitmapLoader = new BitmapLoader(context);
        linkBitmapsToColorsAndAssignWidths();
        setGemGroup(gemGroup);
    }


    public void setGemGroup(GemGroup gemGroup){
        this.gemGroup = gemGroup;
        gemGroup.setGemWidth(GEM_WIDTH);
        setBitmapReferences();
        setDrawItems();
    }


    public void setDrawItems(){
        List<DrawItem> drawItemList = new ArrayList<>(gemGroup.getGems().size());
        for(Gem gem: gemGroup.getGems()){
            drawItemList.add(gem);
        }
        transparentView.setDrawItems(drawItemList);
    }


    @Override
    public void drawIfUpdated(){
        if(gemGroup.wasUpdated()) {
            transparentView.setTranslateY(gemGroup.getY());
            transparentView.setTranslateX(gemGroup.getX());
            transparentView.updateAndDraw();
            transparentView.invalidate();
            gemGroup.setUpdated(false);
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
    }

}
