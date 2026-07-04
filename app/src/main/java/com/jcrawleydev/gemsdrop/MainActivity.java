package com.jcrawleydev.gemsdrop;

import android.os.Bundle;

import com.jcrawleydev.gemsdrop.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.game.GameModel;
import com.jcrawleydev.gemsdrop.view.fragments.MainMenuFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;


public class MainActivity extends AppCompatActivity{

    private MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupInsets();
        setupViewModel();
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
        viewModel.musicPlayer.init(getApplication());
        viewModel.highScores.init(getApplication());
        if(viewModel.gameModel == null){
            viewModel.soundPlayer = new SoundPlayer(getApplicationContext());
            viewModel.gameModel = new GameModel(getApplicationContext(), viewModel.highScores, viewModel.soundPlayer);
        }
    }


    private void setupFragmentsIf(boolean isSavedStateNull) {
        if(!isSavedStateNull){
            return;
        }
        var mainMenuFragment = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainMenuFragment)
                .commit();
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

}
