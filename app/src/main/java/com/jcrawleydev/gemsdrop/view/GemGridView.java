package com.jcrawleydev.gemsdrop.view;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;

public class GemGridView implements UpdatableView{

    private final GemGrid gemGrid;
    private final TransparentView transparentView;
    private boolean wasUpdated;

    public GemGridView(TransparentView transparentView, GemGrid gemGrid, int gem_size, int floorY, int borderWidth){
        this.gemGrid = gemGrid;
        this.transparentView = transparentView;

        gemGrid.setFloorY(floorY);
        gemGrid.setGemSize(gem_size);
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
