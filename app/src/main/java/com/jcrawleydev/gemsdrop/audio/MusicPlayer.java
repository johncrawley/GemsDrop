package com.jcrawleydev.gemsdrop.audio;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.os.Handler;
import android.os.Looper;

import com.jcrawleydev.gemsdrop.R;


public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private boolean isMusicEnabled = true;
    private VolumeShaper.Configuration volumeShaperConfig;
    private final int fadeOutTime = 1500;
    private VolumeShaper volumeShaper;
    private boolean isInitialized;
    private int currentPosition = 0;
    private enum MusicState { STOPPED, PLAYING, PAUSED}
    private MusicState musicState = MusicState.STOPPED;


    public void init(Application application){
        if(isInitialized){
            return;
        }
        var context = application.getApplicationContext();
        if(context == null){
            isMusicEnabled = false;
            return;
        }
        initMediaPlayer(application);
        initVolumeShaper();
        isInitialized = true;
    }


    public void setMusicEnabled(boolean isSoundEnabled){
        this.isMusicEnabled = isSoundEnabled;
    }


    private void initMediaPlayer(Context context){
        mediaPlayer = MediaPlayer.create(context, R.raw.music_title_1);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.reset();
            mp.release();
            musicState = MusicState.STOPPED;
        });
    }


    private void initVolumeShaper(){
        volumeShaperConfig =
                new VolumeShaper.Configuration.Builder()
                        .setDuration(fadeOutTime)
                        .setCurve(new float[] {0.f, 1.f}, new float[] {1.f, 0.f})
                        .setInterpolatorType(VolumeShaper.Configuration.INTERPOLATOR_TYPE_LINEAR)
                        .build();

    }


    public void play(){
        if(musicState == MusicState.STOPPED) {
            mediaPlayer.start();
            musicState = MusicState.PLAYING;
        }
    }


    public void pause(){
        if(musicState == MusicState.PLAYING) {
            currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            musicState = MusicState.PAUSED;
        }
    }


    public void resume(){
        if(musicState == MusicState.PAUSED) {
            mediaPlayer.seekTo(currentPosition);
            mediaPlayer.start();
            musicState = MusicState.PLAYING;
        }
    }


    public void fadeOut(){
        if(isMusicEnabled){
            volumeShaper = mediaPlayer.createVolumeShaper(volumeShaperConfig);
            volumeShaper.apply(VolumeShaper.Operation.PLAY);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    musicState = MusicState.STOPPED;
                }
            }, fadeOutTime + 50);
        }
    }


}
