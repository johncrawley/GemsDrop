package com.jcrawleydev.gemsdrop;

import android.view.View;

import com.jcrawleydev.gemsdrop.control.ClickHandler;
import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gameState.GameStateManager;
import com.jcrawleydev.gemsdrop.gameState.GameStateManagerImpl;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
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
    private GameStateManager gameStateManager;
    private final BitmapLoader bitmapLoader;
    private final int height;
    private final int width;
    private final int floorY;
    private GemGridLayer gemGridLayer;
    private ClickHandler clickHandler;
    private GemControls gemControls;
    private final float gemWidth;
    private Evaluator evaluator;
    private GemCountTracker gemCountTracker;
    private ScoreBoardLayer scoreboardLayer;
    private final int borderWidth;
    private final int scoreBarHeight;
    private GameState currentGameState;
    private GameState titleState;
    private GameState gameOverState;
    private GameState inGameState;
    private final View titleView;
    private final View gameOverView;
    private final MainActivity activity;
    private final int maxRows;
    private boolean isGameOver;
    private final MainViewModel viewModel;
    private final  GemGrid gemGrid;


    public Game(MainActivity activity,
                BitmapLoader bitmapLoader,
                int screenWidth,
                int screenHeight,
                float gemWidth,
                float dropValue,
                int gemGridBorder,
                int numberOfColumns,
                int scoreBarHeight,
                int floorY,
                View titleView,
                View gameOverView){

        this.width = screenWidth;
        this.height = screenHeight;
        this.gemWidth = gemWidth;
        this.activity = activity;
        this.viewModel = activity.getViewModel();
        this.bitmapLoader = bitmapLoader;
        this.borderWidth = gemGridBorder;
        this.scoreBarHeight = scoreBarHeight;
        this.floorY = floorY;
        int numberOfGems = 3;
        maxRows = activity.getResources().getInteger(R.integer.maximum_rows);
        float initialY = floorY - ((maxRows + numberOfGems) * gemWidth);
        this.titleView = titleView;
        this.gameOverView = gameOverView;
        gemGrid = new GemGrid(numberOfColumns, maxRows);

        gemGroupFactory = new GemGroupFactory.Builder()
                .withInitialY(initialY)
                .withGemWidth(gemWidth)
                .dropValue(dropValue)
                .withNumberOfGems(numberOfGems)
                .withInitialPosition(numberOfColumns /2)
                .withFloorAt(floorY)
                .withBorderWidth(borderWidth)
                .withGemGrid(gemGrid)
                .build();
    }


    public Score getScore(){
        return scoreboardLayer.getScore();
    }


    public void onPause(){
        if(gameStateManager != null){
            gameStateManager.stopAllThreads();
        }
    }


    public void onResume(){
        if(gameStateManager != null){
            gameStateManager.resumeCurrentState();
        }
    }


    public void loadGameOverState(){
        log("Entered loadGameOverState()");
        if(currentGameState != inGameState || isGameOver){
            boolean isCurrentStateNotInGame = currentGameState != inGameState;
            log("currentState != inGameState ? : " + isCurrentStateNotInGame );
            log("isGameOver : " + isGameOver);
            return;
        }
        log("setting isGameOVer to true");
        isGameOver = true;
        log("current state is: " + currentGameState.toString() + " ... switching to gameOverState...");
        switchToState(gameOverState);
    }

    private void log(String msg){
        System.out.println("^^^ Game : " + msg);
    }


    public void loadTitleState(){
        switchToState(titleState);
    }


    public void loadInGameState(){
        isGameOver = false;
        switchToState(inGameState);
    }


    private void switchToState(GameState gameState){
        currentGameState.stop();
        currentGameState = gameState;
        currentGameState.start();
    }


    void click(int x, int y){
        currentGameState.click(x,y);
    }


    void initGemGridView(TransparentView v){
        gemGrid.setDropIncrement((int)(gemWidth / getInt(R.integer.gem_grid_gravity_drop_distance_factor)));
        gemGridLayer = new GemGridLayer(v, bitmapLoader, gemGrid, gemWidth, width, floorY, borderWidth);
        evaluator = new Evaluator(gemGrid, 3);
        gemControls = new GemControls(gemGrid);
        clickHandler = new ClickHandler(gemControls, width, height);
        gemCountTracker = new GemCountTracker(gemGrid);
    }


    void initGemGroupLayer(TransparentView v, BitmapLoader bitmapLoader){
        gemGroupView = new GemGroupLayer(v, bitmapLoader, gemWidth);
    }


    void initScoreboardLayer(TransparentView transparentView){
        if(viewModel.score == null) {
            viewModel.score = new Score(100);
        }
        scoreboardLayer = new ScoreBoardLayer(activity, transparentView, viewModel.score, width, height, scoreBarHeight);
        scoreboardLayer.draw();
    }


    void initBorder(TransparentView transparentView, BitmapLoader bitmapLoader){
        BorderLayer borderView = new BorderLayer(transparentView, bitmapLoader);
        borderView.setPatternImage(R.drawable.background_pattern_1);
        borderView.draw();
    }


    void init(){
        SoundPlayer soundPlayer = new SoundPlayer(activity);
        createGameStateManager(soundPlayer);
        initGameStates();
    }


    private void createGameStateManager(SoundPlayer soundPlayer){
        gameStateManager = GameStateManagerImpl.Builder.newInstance()
                .game(this)
                .viewModel(viewModel)
                .evaluator(evaluator)
                .gemControls(gemControls)
                .gemGroupView(gemGroupView)
                .gridView(gemGridLayer)
                .gemGroupFactory(gemGroupFactory)
                .scoreView(scoreboardLayer)
                .gemCountTracker(gemCountTracker)
                .soundPlayer(soundPlayer)
                .maxColumnHeight(maxRows)
                .build();
        gameStateManager.init();
    }


    private void initGameStates(){
        titleState = new TitleState(activity.getMainView(), activity.getApplicationContext(), this, titleView, height);
        inGameState = new InGameState(this, gameStateManager, clickHandler);
        gameOverState = new GameOverState(this, gameOverView, titleView, height);
        currentGameState = titleState;
        currentGameState.start();
    }


    private int getInt(int resId){
        return activity.getResources().getInteger(resId);
    }

}
