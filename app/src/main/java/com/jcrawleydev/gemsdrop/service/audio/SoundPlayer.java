package com.jcrawleydev.gemsdrop.service.audio;

import static com.jcrawleydev.gemsdrop.service.audio.SoundEffect.*;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.jcrawleydev.gemsdrop.R;

import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {

    private final Context context;
    private SoundPool soundPool;
    private final Map<SoundEffect, Integer> soundsMap;
    private boolean isSoundEnabled = true;

    public SoundPlayer(Context context){
        this.context = context;
        setupSoundPool();
        soundsMap = new HashMap<>();
        loadSounds();
    }


    public void setSoundEnabled(boolean isSoundEnabled){
        this.isSoundEnabled = isSoundEnabled;
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


    public void playSound(SoundEffect soundEffect){
        if(!isSoundEnabled){
            return;
        }
        Integer soundId = soundsMap.get(soundEffect);
        if(soundId != null){
            soundPool.play(soundId, 100, 100, 1, 0, 1);
        }
    }


    private void loadSounds(){
        loadSound(R.raw.disappear_1, GEMS_DISAPPEAR);
        loadSound(R.raw.disappear_2, GEMS_DISAPPEAR_CHAIN_REACTION_1);
        loadSound(R.raw.disappear_3, GEMS_DISAPPEAR_CHAIN_REACTION_2);
        loadSound(R.raw.disappear_4, GEMS_DISAPPEAR_CHAIN_REACTION_3);
        loadSound(R.raw.disappear_5, GEMS_DISAPPEAR_CHAIN_REACTION_4);
        loadSound(R.raw.disappear_6, GEMS_DISAPPEAR_CHAIN_REACTION_5);
        loadSound(R.raw.disappear_7, GEMS_DISAPPEAR_CHAIN_REACTION_6);
        loadSound(R.raw.disappear_wonder, WONDER_GEM_GEMS_DISAPPEAR);
        loadSound(R.raw.gems_greyed_out_1, SoundEffect.GEMS_GREYED_OUT);
    }


    private void loadSound(int soundResId, SoundEffect soundEffect){
        int id = soundPool.load(context, soundResId, 1);
        soundsMap.put(soundEffect, id);
    }


}
