package com.jcrawleydev.gemsdrop;

import android.content.Context;

import com.jcrawleydev.gemsdrop.action.ActionMediator;
import com.jcrawleydev.gemsdrop.control.ClickHandler;
import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.view.BitmapLoader;
import com.jcrawleydev.gemsdrop.view.BorderView;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.GemGroupView;
import com.jcrawleydev.gemsdrop.view.ScoreView;
import com.jcrawleydev.gemsdrop.view.TransparentView;


public class Game {

    private final GemGroupFactory gemGroupFactory;
    private GemGroupView gemGroupView;
    private BorderView borderView;
    private final int height;
    private final int width;
    private final int floorY;
    private GemGridLayer gemGridLayer;
    private ClickHandler clickHandler;
    private GemControls gemControls;
    private final int gemWidth;
    private Evaluator evaluator;
    private ActionMediator actionMediator;
    private GemCountTracker gemCountTracker;
    private ScoreView scoreView;
    private final Context context;
    private final int borderWidth;


    public Game(Context context, int screenWidth, int screenHeight, int gemWidth){
        this.width = screenWidth;
        this.height = screenHeight;
        this.gemWidth = gemWidth;
        this.context = context;
        this.borderWidth = gemWidth /2;
        int initialY = this.gemWidth * -4;
        floorY = height - (height /10);

        gemGroupFactory = new GemGroupFactory.Builder()
                .withInitialY(initialY)
                .withGemWidth(gemWidth)
                .withNumerOfGems(3)
                .withInitialPosition(4)
                .withFloorAt(floorY)
                .withBorderWidth(borderWidth)
                .build();
    }


    void click(int x, int y){
        clickHandler.click(x,y);
        actionMediator.createAndDropGems();
    }


    void initGemGridView(TransparentView v){
        GemGrid gemGrid = new GemGrid(7, 12);
        gemGrid.setDropIncrement(gemWidth / 5);
        gemGridLayer = new GemGridLayer(v, gemGrid, gemWidth, width, floorY, borderWidth);
        evaluator = new Evaluator(gemGrid, 3);
        gemControls = new GemControls(gemGrid);
        clickHandler = new ClickHandler(gemControls, width, height);
        gemCountTracker = new GemCountTracker(gemGrid);
    }


    void initGemGroupView(TransparentView v, BitmapLoader bitmapLoader){
        gemGroupView = new GemGroupView(v, bitmapLoader, gemGroupFactory.createGemGroup(), gemWidth);
    }


    void initScoreView(TransparentView transparentView, BitmapLoader bitmapLoader){
        Score score = new Score(100);
        scoreView = new ScoreView(context, transparentView, score, bitmapLoader, width, height);
        scoreView.draw();
    }


    void initBorder(TransparentView transparentView, BitmapLoader bitmapLoader){
        Score score = new Score(100);
        borderView = new BorderView(transparentView, bitmapLoader);
        borderView.setPatternImage(R.drawable.background_pattern_1);
        borderView.draw();
    }


    void init(){
        SoundPlayer soundPlayer = new SoundPlayer();
        actionMediator = new ActionMediator.Builder()
                .evaluator(evaluator)
                .gemControls(gemControls)
                .gemGroupView(gemGroupView)
                .gridView(gemGridLayer)
                .gemGroupFactory(gemGroupFactory)
                .scoreView(scoreView)
                .gemCountTracker(gemCountTracker)
                .soundPlayer(soundPlayer)
                .build();
    }

}
