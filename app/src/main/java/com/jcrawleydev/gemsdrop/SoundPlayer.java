package com.jcrawleydev.gemsdrop;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {

    private final MainViewModel viewModel;
    private MediaPlayer mediaPlayer;
    private final Context context;
    private SoundPool soundPool;
    public enum Sound { DISAPPEAR, MENU_BUTTON }
    private final Map<Sound, Integer> soundsMap;

    public SoundPlayer(Context context, MainViewModel viewModel){
        this.context = context;
        this.viewModel = viewModel;
        //setupMediaPlayer();
        setupSoundPool();
        soundsMap = new HashMap<>();
        loadSounds();

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


    private void setupSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(attributes)
                .build();
    }


    public void playSound(Sound sound){
        Integer soundId = soundsMap.get(sound);
        if(soundId != null){
            soundPool.play(soundId, 100, 100, 1, 0, 1);
        }
    }


    private void loadSounds(){
        loadSound(R.raw.disappear, Sound.DISAPPEAR);
    }



    private void loadSound(int soundResId, Sound sound){
        int id = soundPool.load(context, soundResId, 1);
        soundsMap.put(sound, id);
    }


}
