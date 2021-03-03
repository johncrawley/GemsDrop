package com.jcrawleydev.gemsdrop.view;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;

public class GemGridView implements UpdatableView{

    private GemGrid gemGrid;
    private final int GEM_SIZE;
    private TransparentView transparentView;
    private int floorY;
    private boolean wasUpdated;

    public GemGridView(TransparentView transparentView, GemGrid gemGrid, int gem_size, int floorY){

        this.gemGrid = gemGrid;
        this.GEM_SIZE = gem_size;
        this.transparentView = transparentView;
        this.floorY = floorY;
        gemGrid.setFloorY(floorY);
        gemGrid.setGemSize(GEM_SIZE);
        gemGrid.setStartingX(0);
        draw();
    }


    public void drawIfUpdated(){
        if(!wasUpdated){
            return;
        }
        draw();
    }

    public GemGrid getGemGrid(){
        return gemGrid;
    }


    public void draw(){
        transparentView.setDrawItems(gemGrid.getAllGems());
        log("Just set draw items: number of gems: " + gemGrid.gemCount());
        transparentView.invalidate();
    }

    private void log(String msg){
        System.out.println("GemGridView: " + msg);
        System.out.flush();
    }



}
