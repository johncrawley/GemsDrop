package com.jcrawleydev.gemsdrop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.jcrawleydev.gemsdrop.service.score.ScoreStatistics;
import com.jcrawleydev.gemsdrop.view.BitmapLoader;
import com.jcrawleydev.gemsdrop.view.TransparentView;
import com.jcrawleydev.gemsdrop.view.fragments.MainMenuFragment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private int height, width;
    private Game game;
    private float gemWidth;
    private float dropValue;
    private int gemGridBorder;
    private int numberOColumns;
    private int scoreBarHeight;
    private int floorY;
    private MainViewModel viewModel;
    private TransparentView gemGroupTransparentView, gemGridTransparentView, scoreTransparentView, borderView, titleBackgroundView, gameOverBackgroundView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideActionBar();
        assignScreenDimensions();
        initViewModel();
        setupTransparentViews();
        initGame();
    }


    private void setupFragments() {
        Fragment mainMenuFragment = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainMenuFragment)
                .commit();
    }

    public void onGameOver(ScoreStatistics scoreStatistics){

    }

    public void setScore(int score){

    }


    public void playSound(SoundPlayer.Sound sound){

    }


    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }


    private void setupTransparentViews(){
        setupGemGroupView();
        setupGemGridView();
        setupScoreView();
        setupBorderView();
        setupTitleBackgroundView();
        setupGameOverBackgroundView();
    }


    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupGemGroupView(){
        gemGroupTransparentView = findViewById(R.id.gemGroupView);
        gemGroupTransparentView.setDimensions(width, height);
        gemGroupTransparentView.translateXToMiddle();
        gemGroupTransparentView.setOnTouchListener(this);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupGemGridView(){
        gemGridTransparentView = findViewById(R.id.gemGridView);
        gemGridTransparentView.setDimensions(width - gemGridBorder, height);
        gemGridTransparentView.setOnTouchListener(this);
    }


    private void setupScoreView(){
        scoreTransparentView = findViewById(R.id.scoreView);
        scoreTransparentView.setDimensions(width, height);
    }


    private void setupTitleBackgroundView(){
        titleBackgroundView = findViewById(R.id.titleBackgroundView);
        titleBackgroundView.setDimensions(width, height);
    }


    private void setupBorderView(){
        borderView = findViewById(R.id.borderView);
        borderView.setDimensions(width, height);
    }


    private void setupGameOverBackgroundView(){
        gameOverBackgroundView = findViewById(R.id.gameOverBackgroundView);
        gameOverBackgroundView.setDimensions(width, height);
    }


    @Override
    public void onPause(){
        if(game != null){
            game.onPause();
        }
        super.onPause();
    }


    @Override
    public void onResume(){
        if(game != null){
            // game.onResume();
        }
        super.onResume();
    }


    private void initGame(){
        View titleView = findViewById(R.id.titleViewInclude);
        View gameOverView = findViewById(R.id.gameOverViewInclude);
        BitmapLoader bitmapLoader = new BitmapLoader(this, gemWidth);
        game = new Game(this, bitmapLoader, width, height, gemWidth,
                dropValue, gemGridBorder, numberOColumns, scoreBarHeight, floorY, titleView, gameOverView);
        game.initGemGridView(gemGridTransparentView);
        game.initGemGroupLayer(gemGroupTransparentView, bitmapLoader);
        game.initScoreboardLayer(scoreTransparentView);
        game.initBorder(borderView, bitmapLoader);
        game.initBorder(titleBackgroundView, bitmapLoader);
        game.initBorder(gameOverBackgroundView, bitmapLoader);
        game.init();
    }

    public View getMainView(){
        return findViewById(R.id.titleViewInclude);
    }


    public MainViewModel getViewModel(){
        return this.viewModel;
    }


    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View v, MotionEvent e){
        if(e.getAction() != MotionEvent.ACTION_DOWN){
            return true;
        }
        int x = (int)e.getX();
        int y = (int)e.getY();
        game.click(x, y);
        return true;
    }


    private void assignScreenDimensions(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        numberOColumns = getResources().getInteger(R.integer.number_of_columns);
        scoreBarHeight = height / 15;
        int bottomBorderHeight = 50 + (height / 18);
        floorY = height - bottomBorderHeight;
        int maxRows = getResources().getInteger(R.integer.maximum_rows);
        gemWidth = (float)(height - (scoreBarHeight + bottomBorderHeight)) / maxRows;
        dropValue = gemWidth /2;
        gemGridBorder = (int)(this.width - (gemWidth * numberOColumns)) / 2;
    }

}
