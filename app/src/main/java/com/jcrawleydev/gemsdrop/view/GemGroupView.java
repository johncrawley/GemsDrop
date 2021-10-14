package com.jcrawleydev.gemsdrop.view;

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

    private final TransparentView transparentView;
    private int gemWidth;
    private Map<Gem.Color, Bitmap> gemColorMap;
    private final BitmapLoader bitmapLoader;
    private GemGroup gemGroup;


    public GemGroupView(View view, BitmapLoader bitmapLoader, GemGroup gemGroup, int gemWidth){
        transparentView = (TransparentView)view;
        this.bitmapLoader = bitmapLoader;
        this.gemWidth = gemWidth;
        linkBitmapsToColorsAndAssignWidths();
        setGemGroup(gemGroup);
    }


    public void setGemGroup(GemGroup gemGroup){
        this.gemGroup = gemGroup;
        gemGroup.setGemWidth(gemWidth);
        setBitmapReferences();
        setDrawItems();
    }


    public GemGroup getGemGroup(){
        return this.gemGroup;
    }


    public void setDrawItems(){
        List<DrawItem> drawItemList = new ArrayList<>(gemGroup.getGems().size());
        drawItemList.addAll(gemGroup.getGems());
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

    public void wipe(){
        gemGroup.setGemsInvisible();
        transparentView.invalidate();
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
        //assignWidths(bm);
    }


    private void assignWidths(Bitmap bm){
        gemWidth = bm.getWidth();
    }

}
