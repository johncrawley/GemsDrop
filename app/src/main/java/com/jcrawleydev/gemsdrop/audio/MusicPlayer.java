package com.jcrawleydev.gemsdrop.audio;

import android.content.Context;
import android.media.MediaPlayer;
import com.jcrawleydev.gemsdrop.R;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private final Context context;
    private boolean isMusicEnabled;

    public MusicPlayer(Context context){
        this.context = context;
        setupMediaPlayer();
    }


    public void setMusicEnabled(boolean isSoundEnabled){
        this.isMusicEnabled = isSoundEnabled;
    }


    private void setupMediaPlayer(){
        mediaPlayer = MediaPlayer.create(context, R.raw.disappear);
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.reset();
            mp.release();
            mediaPlayer = MediaPlayer.create(context, R.raw.disappear);
        });
    }


    public void playMusic(int multiplier){
        if(isMusicEnabled){
            mediaPlayer.start();
        }
    }


    public void stopMusic(){

    }



}
