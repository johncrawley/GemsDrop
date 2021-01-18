package com.jcrawleydev.gemsdrop.view;

import com.jcrawleydev.gemsdrop.GemGrid;
import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.List;

public class GemGridView {

    private GemGrid gemGrid;
    private final int GEM_SIZE;
    private TransparentView transparentView;

    public GemGridView(TransparentView transparentView, GemGrid gemGrid, int gem_size){

        this.gemGrid = gemGrid;
        this.GEM_SIZE = gem_size;
        this.transparentView = transparentView;
        draw();
    }

    private void log(String msg){
        System.out.println("GemGridView " + msg);
    }


    public void draw(){
        int startingX = 50;
        int startingBottomY = 1900;

        List<List<Gem>> columns = gemGrid.getGemColumns();

        for(int columnIndex = 0; columnIndex < columns.size(); columnIndex++){
            List<Gem> column = columns.get(columnIndex);
            for(int rowIndex=0; rowIndex < column.size(); rowIndex++){
                Gem gem = column.get(rowIndex);
                log("Gem size: " + GEM_SIZE + " column index : " + columnIndex +  " GEM_SIZE * columnIndex: " + GEM_SIZE * columnIndex);
                int x = startingX + (GEM_SIZE * columnIndex);
                int y = startingBottomY - ((rowIndex + 1)* GEM_SIZE);
                log("Gem in grid x,y: " + x + "," + y);
                gem.setX(x);
                gem.setY(y);
            }
        }
        transparentView.setDrawItems(gemGrid.getAllGems());
        transparentView.invalidate();
    }



}
