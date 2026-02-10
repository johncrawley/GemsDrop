package com.jcrawleydev.gemsdrop.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.os.Handler;
import android.os.Looper;

import com.jcrawleydev.gemsdrop.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private final Context context;
    private boolean isMusicEnabled;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> stopPlayerFuture;
    private VolumeShaper.Configuration volumeShaperConfig;
    private VolumeShaper volumeShaper;


    public MusicPlayer(Context context){
        this.context = context;
        setupMediaPlayer();
    }


    public void setMusicEnabled(boolean isSoundEnabled){
        this.isMusicEnabled = isSoundEnabled;
    }


    private void setupMediaPlayer(){
        mediaPlayer = MediaPlayer.create(context, R.raw.music_title_1);
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.reset();
            mp.release();
            mediaPlayer = MediaPlayer.create(context, R.raw.music_title_1);
        });
    }


    private void initVolumeShaper(){
        volumeShaperConfig =
                new VolumeShaper.Configuration.Builder()
                        .setDuration(1000)
                        .setCurve(new float[] {1.f, 1.f}, new float[] {1.f, 0.f})
                        .setInterpolatorType(VolumeShaper.Configuration.INTERPOLATOR_TYPE_LINEAR)
                        .build();

    }


    public void play(){
        mediaPlayer.start();
    }


    public void fadeOut(){
        if(isMusicEnabled){
            try (var shaper = mediaPlayer.createVolumeShaper(volumeShaperConfig)) {
                shaper.apply(VolumeShaper.Operation.PLAY);
            }
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                // mediaPlayer.release(); // only if you're done with it
            }
            volumeShaper.close(); // important - free resources
        }, durationMs + 50);
    }





}
