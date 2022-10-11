package com.jcrawleydev.gemsdrop.view.gemgrid;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.view.BitmapLoader;
import com.jcrawleydev.gemsdrop.view.TransparentView;
import com.jcrawleydev.gemsdrop.view.UpdatableView;
import com.jcrawleydev.gemsdrop.view.item.DrawableItem;


public class GemGridLayer implements UpdatableView, DrawableItem {

    private final GemGrid gemGrid;
    private final TransparentView transparentView;
    private final BitmapLoader bitmapLoader;

    public GemGridLayer(TransparentView transparentView, BitmapLoader bitmapLoader, GemGrid gemGrid, float gemSize, int viewWidth, int floorY, int borderWidth){
        this.gemGrid = gemGrid;
        int topBorderWidth = 0;
        this.transparentView = transparentView;
        this.bitmapLoader = bitmapLoader;
        transparentView.addBackgroundDrawableItem(new BackgroundItem(viewWidth, borderWidth, topBorderWidth, floorY));
        gemGrid.setFloorY(floorY);
        gemGrid.setGemSize(gemSize);
        gemGrid.setStartingX(borderWidth);
        draw();
    }


    @Override
    public void drawIfUpdated(){
        draw();
    }


    public GemGrid getGemGrid(){
        return gemGrid;
    }


    public void turnAllGemsGrey(){
       gemGrid.turnAllGemsGrey();
       draw();
    }


    public void clearGemGrid(){
        gemGrid.clear();
        draw();
    }

    public void flickerGemsMarkedForDeletion(){
        gemGrid.flickerGemsMarkedForDeletion();
        draw();
    }


    public void draw(){
        transparentView.clearDrawableItems();
        transparentView.addDrawableItem(this);
        transparentView.invalidate();
    }


    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public boolean isVisible() {
        return true;
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        for(Gem gem : gemGrid.getAllGemsInGrid()) {
            if (gem != null && gem.isVisible()) {
                canvas.drawBitmap(bitmapLoader.get(gem.getColor()), gem.getX(), gem.getY(), paint);
            }
        }
    }
}
