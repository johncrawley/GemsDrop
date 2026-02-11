package com.jcrawleydev.gemsdrop.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.os.Handler;
import android.os.Looper;

import com.jcrawleydev.gemsdrop.R;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private final Context context;
    private boolean isMusicEnabled = true;
    private VolumeShaper.Configuration volumeShaperConfig;
    private final int fadeOutTime = 1500;

    public MusicPlayer(Context context){
        this.context = context;
        setupMediaPlayer();
        initVolumeShaper();
    }


    public void setMusicEnabled(boolean isSoundEnabled){
        this.isMusicEnabled = isSoundEnabled;
    }


    private void setupMediaPlayer(){
        mediaPlayer = MediaPlayer.create(context, R.raw.music_title_1);
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.reset();
            mp.release();
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
        setupMediaPlayer();
        mediaPlayer.start();

    }

    private VolumeShaper volumeShaper;


    public void fadeOut(){
        if(isMusicEnabled){

                volumeShaper = mediaPlayer.createVolumeShaper(volumeShaperConfig);
                volumeShaper.apply(VolumeShaper.Operation.PLAY);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (mediaPlayer.isPlaying()) {
                    // volumeShaper.close();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            }, fadeOutTime + 50);

        }
    }





}
