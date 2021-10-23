package com.jcrawleydev.gemsdrop;

import android.content.Context;

import com.jcrawleydev.gemsdrop.action.ActionMediator;
import com.jcrawleydev.gemsdrop.control.ClickHandler;
import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.gemgroup.SpeedController;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.state.GameOverState;
import com.jcrawleydev.gemsdrop.state.GameState;
import com.jcrawleydev.gemsdrop.state.InGameState;
import com.jcrawleydev.gemsdrop.state.TitleState;
import com.jcrawleydev.gemsdrop.view.BitmapLoader;
import com.jcrawleydev.gemsdrop.view.BorderLayer;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;
import com.jcrawleydev.gemsdrop.view.ScoreBoardLayer;
import com.jcrawleydev.gemsdrop.view.TransparentView;


public class Game {

    private final GemGroupFactory gemGroupFactory;
    private GemGroupLayer gemGroupView;
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
    private ScoreBoardLayer scoreView;
    private final Context context;
    private final int borderWidth;
    private final int scoreBarHeight;
    private GameState currentGameState;
    private GameState titleState;
    private GameState gameOverState;
    private GameState inGameState;


    public Game(Context context, int screenWidth, int screenHeight, int gemWidth, int gemGridBorder, int numberOfColumns, int scoreBarHeight, int floorY){
        this.width = screenWidth;
        this.height = screenHeight;
        this.gemWidth = gemWidth;
        this.context = context;
        this.borderWidth = gemGridBorder;
        this.scoreBarHeight = scoreBarHeight;
        this.floorY = floorY;
        int numberOfGems = 3;
        int maxRows = context.getResources().getInteger(R.integer.maximum_rows);
        int initialY = floorY - ((maxRows + numberOfGems) * gemWidth);


        gemGroupFactory = new GemGroupFactory.Builder()
                .withInitialY(initialY)
                .withGemWidth(gemWidth)
                .withNumerOfGems(numberOfGems)
                .withInitialPosition(numberOfColumns /2)
                .withFloorAt(floorY)
                .withBorderWidth(borderWidth)
                .build();

        initGameStates();
    }


    private void initGameStates(){
        titleState = new TitleState(this);
        inGameState = new InGameState(this);
        gameOverState = new GameOverState(this);
        currentGameState = titleState;
    }


    public void loadGameOverState(){
        currentGameState.stop();
        currentGameState = gameOverState;
        currentGameState.start();
    }


    public void loadTitleState(){
        currentGameState.stop();
        currentGameState = titleState;
        currentGameState.start();
    }


    public void loadInGameState(){
        currentGameState.stop();
        currentGameState = inGameState;
        currentGameState.start();
    }




    void click(int x, int y){
        clickHandler.click(x,y);
        currentGameState.click(x,y);
        actionMediator.createAndDropGems();
    }


    void initGemGridView(TransparentView v){
        GemGrid gemGrid = new GemGrid(7, 12);
        gemGrid.setDropIncrement(gemWidth / getInt(R.integer.gem_grid_gravity_drop_distance_factor));
        gemGridLayer = new GemGridLayer(v, gemGrid, gemWidth, width, floorY, borderWidth);
        evaluator = new Evaluator(gemGrid, 3);
        gemControls = new GemControls(gemGrid);
        clickHandler = new ClickHandler(gemControls, width, height);
        gemCountTracker = new GemCountTracker(gemGrid);
    }


    void initGemGroupLayer(TransparentView v, BitmapLoader bitmapLoader){
        gemGroupView = new GemGroupLayer(v, bitmapLoader);
    }


    void initScoreView(TransparentView transparentView){
        Score score = new Score(100);
        scoreView = new ScoreBoardLayer(context, transparentView, score, width, height, scoreBarHeight);
        scoreView.draw();
    }


    void initBorder(TransparentView transparentView, BitmapLoader bitmapLoader){
        BorderLayer borderView = new BorderLayer(transparentView, bitmapLoader);
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
                .speedController(new SpeedController(context))
                .gravityInterval(getInt(R.integer.gravity_interval))
                .flickerMarkedGemsTime(getInt(R.integer.disappearing_gems_flicker_time))
                .gridGravityDistanceFactor(getInt(R.integer.gem_grid_gravity_drop_distance_factor))
                .build();
    }


    private int getInt(int resId){
        return context.getResources().getInteger(resId);
    }

}
