package com.jcrawleydev.gemsdrop.view.gemgrid;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.view.BitmapLoader;
import com.jcrawleydev.gemsdrop.view.DrawItem;
import com.jcrawleydev.gemsdrop.view.TransparentView;
import com.jcrawleydev.gemsdrop.view.UpdatableView;
import com.jcrawleydev.gemsdrop.view.item.DrawableItem;

import java.util.List;

public class GemGridLayer implements UpdatableView, DrawableItem {

    private final GemGrid gemGrid;
    private final TransparentView transparentView;
    private boolean wasUpdated;
    private final BackgroundItem backgroundItem;
    private final BitmapLoader bitmapLoader;

    public GemGridLayer(TransparentView transparentView, BitmapLoader bitmapLoader, GemGrid gemGrid, int gemSize, int viewWidth, int floorY, int borderWidth){
        this.gemGrid = gemGrid;
        int topBorderWidth = 0;
        this.transparentView = transparentView;
        this.bitmapLoader = bitmapLoader;
        backgroundItem = new BackgroundItem(viewWidth, borderWidth, topBorderWidth, floorY);
        transparentView.addDrawableItem(backgroundItem);
        gemGrid.setFloorY(floorY);
        gemGrid.setGemSize(gemSize);
        gemGrid.setStartingX(borderWidth);
        draw();
    }


    public void drawIfUpdated(){
        if(wasUpdated){
            draw();
        }
    }


    public GemGrid getGemGrid(){
        return gemGrid;
    }

    public void turnAllGemsGrey(){
       System.out.println("GemGridLayer.turnAllGemsGrey()");
       gemGrid.turnAllGemsGrey();
       draw();
       printGemColors();
    }

    private void printGemColors(){
        StringBuilder str = new StringBuilder("GemColors: " );
        List<DrawableItem> allGems = gemGrid.getAllGems();
        for(DrawableItem item : allGems){
            Gem gem = (Gem)item;
            str.append(gem.getColor().str);
            str.append(" ");
        }
        System.out.println(str.toString());
    }


    public void clearGemGrid(){
        gemGrid.clear();
        draw();
    }


    public void draw(){
        transparentView.clearDrawableItems();
        transparentView.addDrawableItem(backgroundItem);
        transparentView.addDrawableItem(this);
        transparentView.invalidate();
    }


    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public boolean isVisible() {
        return true;
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        for(DrawableItem item : gemGrid.getAllGems()) {
            Gem gem = (Gem) item;
            if (gem != null && gem.isVisible()) {
                canvas.drawBitmap(bitmapLoader.get(gem.getColor()), gem.getX(), gem.getY(), paint);
            }
        }
    }
}
