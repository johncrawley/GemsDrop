package com.jcrawleydev.gemsdrop.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.os.Handler;
import android.os.Looper;

import com.jcrawleydev.gemsdrop.R;

import java.io.IOException;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private boolean isMusicEnabled = true;
    private VolumeShaper.Configuration volumeShaperConfig;
    private final int fadeOutTime = 1500;
    private VolumeShaper volumeShaper;

    public MusicPlayer(Context context, int sessionId){
        if(context == null){
            isMusicEnabled = false;
            return;
        }
        initMediaPlayer(context, sessionId);
        initVolumeShaper();
    }

    public void setMusicEnabled(boolean isSoundEnabled){
        this.isMusicEnabled = isSoundEnabled;
    }


    private void initMediaPlayer(Context context, int sessionId){
        log("entered initMediaPlayer, session ID: " + sessionId);
        if(sessionId == 0){
            isMusicEnabled = false;
            log("initMediaPlayer() sessionID is 0, cannot play");
            return;
        }
        mediaPlayer = new MediaPlayer(context);
        mediaPlayer.setAudioSessionId(sessionId);
        setDataSource(context, R.raw.music_title_1);
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.reset();
            mp.release();
        });
        log("initMediaPlayer() isMediaPlayer playing: " +  mediaPlayer.isPlaying());
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
        if(mediaPlayer.isPlaying()) {
            log("play() returning because MediaPlayer instance is already playing");
            return;
        }
        try{
            mediaPlayer.prepare();
        }catch (IOException e){
            printError(e.getMessage());
            return;
        }
        mediaPlayer.start();
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
