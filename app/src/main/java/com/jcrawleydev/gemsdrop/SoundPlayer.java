package com.jcrawleydev.gemsdrop;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {

    private final MainViewModel viewModel;
    private MediaPlayer mediaPlayer;
    private final Context context;

    public enum Sound {}

    public SoundPlayer(Context context, MainViewModel viewModel){
        this.context = context;
        this.viewModel = viewModel;
        setupMediaPlayer();
    }

    private void setupMediaPlayer(){
        mediaPlayer = MediaPlayer.create(context, R.raw.disappear);
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.reset();
            mp.release();
            mediaPlayer = MediaPlayer.create(context, R.raw.disappear);
        });
    }


    public void playGemDisappearSound(int multiplier){
        if(viewModel.isSoundEnabled){
            mediaPlayer.start();
        }
    }

}
