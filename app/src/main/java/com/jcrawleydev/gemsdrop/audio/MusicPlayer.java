package com.jcrawleydev.gemsdrop.audio;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.os.Handler;
import android.os.Looper;

import com.jcrawleydev.gemsdrop.R;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private boolean isMusicEnabled = true;
    private VolumeShaper.Configuration volumeShaperConfig;
    private final int fadeOutTime = 1500;
    private VolumeShaper volumeShaper;
    private boolean isInitialized;


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
        //setDataSource(context, R.raw.music_title_1);
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.reset();
            mp.release();
        });
    }


    private void log(String msg){
        System.out.println("^^^ MusicPlayer: " + msg);
    }


    private void setDataSource(Context context, int resId){
        var afd = context.getResources().openRawResourceFd(resId);
        if (afd == null){
            log("setDataSource() AudioFileDescriptor is null, cannot assign resource");
            return;
        }
        try {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
        }catch (IOException e){
            printError(e.getMessage());
        }
    }


    private void printError(String msg){
        System.out.print("^^^ ERROR: " + msg);
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
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
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
                }
            }, fadeOutTime + 50);
        }
    }





}
