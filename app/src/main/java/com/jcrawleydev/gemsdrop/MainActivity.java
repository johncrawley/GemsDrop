package com.jcrawleydev.gemsdrop;

import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.sendMessage;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.service.GameService;
import com.jcrawleydev.gemsdrop.service.score.ScoreStatistics;
import com.jcrawleydev.gemsdrop.view.BitmapLoader;
import com.jcrawleydev.gemsdrop.view.GameView;
import com.jcrawleydev.gemsdrop.view.TransparentView;
import com.jcrawleydev.gemsdrop.view.fragments.MainMenuFragment;
import com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, GameView {

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
    private GameService gameService;

    private final AtomicBoolean isServiceConnected = new AtomicBoolean(false);


    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            gameService = binder.getService();
            gameService.setActivity(MainActivity.this);
            sendMessage(MainActivity.this, FragmentMessage.NOTIFY_OF_SERVICE_CONNECTED);
            isServiceConnected.set(true);
        }


        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isServiceConnected.set(false);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideActionBar();
        assignScreenDimensions();
        setupGameService();
        setupFragmentsIf(savedInstanceState == null);
        initViewModel();
    }


    private void setupFragmentsIf(boolean isSavedStateNull) {
        if(!isSavedStateNull){
            return;
        }
        Fragment mainMenuFragment = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainMenuFragment)
                .commit();
    }


    private void setupGameService() {
        Intent intent = new Intent(getApplicationContext(), GameService.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent, connection, 0);
    }


    public void onGameOver(ScoreStatistics scoreStatistics){

    }


    public void setScore(int score){

    }


    @Override
    public void updateGems(Gem... gems){
        for(Gem gem : gems){
        }
    }


    private void updateGem(Gem gem){
        Bundle bundle = new Bundle();
        bundle.putInt(BundleTag.GEM_POSITION.toString(), gem.getPosition());
        bundle.putInt(BundleTag.GEM_COLUMN.toString(), gem.getColumn());
        bundle.putLong(BundleTag.GEM_ID.toString(), gem.getId());
        sendMessage(this, FragmentMessage.UPDATE_GEM, bundle);
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
