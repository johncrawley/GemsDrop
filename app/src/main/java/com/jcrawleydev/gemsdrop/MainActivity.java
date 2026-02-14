package com.jcrawleydev.gemsdrop;

import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_SCORE;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.createBundleOf;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.sendMessage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.jcrawleydev.gemsdrop.game.score.ScoreStatistics;

import com.jcrawleydev.gemsdrop.view.fragments.MainMenuFragment;
import com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupInsets();
        setupViewModel();
        initMusicPlayer();
        hideActionBar();
        setupFragmentsIf(savedInstanceState == null);
    }


    @NonNull
    public MainViewModel getViewModel(){
        if(viewModel == null){
            setupViewModel();
        }
        return viewModel;
    }


    private void setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            var systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void setupViewModel(){
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }


    private void initMusicPlayer(){
        viewModel.musicPlayer.init(getApplication());
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


    public void onGameOver(ScoreStatistics scoreStatistics){

    }


    public void setScore(int score){
        Bundle bundle = createBundleOf(BundleTag.SCORE, score);
        sendMessage(this, UPDATE_SCORE, bundle);
    }


    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        viewModel.musicPlayer.pause();
    }


    @Override
    public void onResume(){
        super.onResume();
        if(viewModel != null){
            viewModel.musicPlayer.resume();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View v, MotionEvent e){
        if(e.getAction() != MotionEvent.ACTION_DOWN){
            return true;
        }
        int x = (int)e.getX();
        int y = (int)e.getY();
        //game.click(x, y);
        return true;
    }

}
