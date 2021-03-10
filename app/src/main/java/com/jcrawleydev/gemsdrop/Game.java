package com.jcrawleydev.gemsdrop;

import com.jcrawleydev.gemsdrop.action.ActionMediator;
import com.jcrawleydev.gemsdrop.control.ClickHandler;
import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.view.BitmapLoader;
import com.jcrawleydev.gemsdrop.view.GemGridView;
import com.jcrawleydev.gemsdrop.view.GemGroupView;
import com.jcrawleydev.gemsdrop.view.TransparentView;


public class Game {

    private GemGroupFactory gemGroupFactory;
    private GemGroupView gemGroupView;
    private int height, width;
    private int floorY;
    private GemGrid gemGrid;
    private GemGridView gemGridView;
    private ClickHandler clickHandler;
    private GemControls gemControls;
    private int gemWidth = 150;
    private Evaluator evaluator;
    private ActionMediator actionMediator;
    private Score score;
    private GemCountTracker gemCountTracker;



    public Game(int screenWidth, int screenHeight){
        this.width = screenWidth;
        this.height = screenHeight;
        int initialY = gemWidth * -2;
        floorY = height - (height /10);


        gemGroupFactory = new GemGroupFactory.Builder()
                .withInitialY(initialY)
                .withGemWidth(gemWidth)
                .withNumerOfGems(3)
                .withInitialPosition(4)
                .withFloorAt(floorY)
                .build();
    }


    void click(int x, int y){
        clickHandler.click(x,y);
        actionMediator.createAndDropGems();
    }


    void initGemGridView(TransparentView v){
        gemGrid = new GemGrid(7,12);
        gemGrid.setDropIncrement(gemWidth / 5);
        gemGridView = new GemGridView(v, gemGrid, 150, floorY);
        evaluator = new Evaluator(gemGrid, 3);
        gemControls = new GemControls(gemGrid);
        clickHandler = new ClickHandler(gemControls, width, height);
        gemCountTracker = new GemCountTracker(gemGrid);
        score = new Score(100);
    }


    void initGemGroupView(TransparentView v, BitmapLoader bitmapLoader){
        gemGroupView = new GemGroupView(v, bitmapLoader, gemGroupFactory.createGemGroup());
    }


    void init(){
        actionMediator = new ActionMediator.Builder()
                .evaluator(evaluator)
                .gemControls(gemControls)
                .gemGroupView(gemGroupView)
                .gridView(gemGridView)
                .gemGroupFactory(gemGroupFactory)
                .score(score)
                .gemCountTracker(gemCountTracker)
                .build();


            //    new ActionMediator(gemGroupView, gemGridView, gemControls, evaluator, gemGroupFactory);

    }



}
