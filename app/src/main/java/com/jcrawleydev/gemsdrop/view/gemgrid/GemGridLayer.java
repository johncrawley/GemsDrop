package com.jcrawleydev.gemsdrop.view.gemgrid;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.view.TransparentView;
import com.jcrawleydev.gemsdrop.view.UpdatableView;

public class GemGridLayer implements UpdatableView {

    private final GemGrid gemGrid;
    private final TransparentView transparentView;
    private boolean wasUpdated;
    BackgroundItem backgroundItem;


    public GemGridLayer(TransparentView transparentView, GemGrid gemGrid, int gemSize, int viewWidth, int floorY, int borderWidth){
        this.gemGrid = gemGrid;
        int topBorderWidth = 0;
        this.transparentView = transparentView;
        BackgroundItem backgroundItem = new BackgroundItem(viewWidth, borderWidth, topBorderWidth, floorY);
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


    public void draw(){
        transparentView.setDrawItems(gemGrid.getAllGems());
        transparentView.invalidate();
    }


    private void log(String msg){
        System.out.println("GemGridView: " + msg);
        System.out.flush();
    }



}
