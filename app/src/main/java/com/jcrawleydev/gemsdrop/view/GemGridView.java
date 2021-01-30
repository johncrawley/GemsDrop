package com.jcrawleydev.gemsdrop.view;

import com.jcrawleydev.gemsdrop.GemGrid;
import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.List;

public class GemGridView {

    private GemGrid gemGrid;
    private final int GEM_SIZE;
    private TransparentView transparentView;
    private int floorY;

    public GemGridView(TransparentView transparentView, GemGrid gemGrid, int gem_size, int floorY){

        this.gemGrid = gemGrid;
        this.GEM_SIZE = gem_size;
        this.transparentView = transparentView;
        this.floorY = floorY;
        draw();
    }

    private void log(String msg){
        System.out.println("GemGridView " + msg);
    }


    public void draw(){
        int startingX = 0;
        int startingBottomY = floorY;

        List<List<Gem>> columns = gemGrid.getGemColumns();

        for(int columnIndex = 0; columnIndex < columns.size(); columnIndex++){
            List<Gem> column = columns.get(columnIndex);
            for(int rowIndex=0; rowIndex < column.size(); rowIndex++){
                Gem gem = column.get(rowIndex);
                int x = startingX + (GEM_SIZE * columnIndex);
                int y = startingBottomY - ((rowIndex + 1)* GEM_SIZE);
                gem.setX(x);
                gem.setY(y);
            }
        }
        transparentView.setDrawItems(gemGrid.getAllGems());
        transparentView.invalidate();
    }



}
