package com.jcrawleydev.gemsdrop.audio;

import static com.jcrawleydev.gemsdrop.audio.SoundEffect.*;

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
        playSound(soundEffect, 0);
    }


    public void playSound(SoundEffect soundEffect, int repeats){
        playSound(soundEffect, repeats, 88);
    }

    public void playSound(SoundEffect soundEffect, int repeats, int volume){
        if(!isSoundEnabled){
            return;
        }
        Integer soundId = soundsMap.get(soundEffect);
        if(soundId != null){
            soundPool.play(soundId, volume, volume, 1, repeats, 1);
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
        loadSound(R.raw.game_over_1, GAME_OVER);
        loadSound(R.raw.ground_hit_2, GEM_HITS_FLOOR);
        playSound(GEM_HITS_FLOOR, 0, 1);
    }


    private void loadSound(int soundResId, SoundEffect soundEffect){
        int id = soundPool.load(context, soundResId, 1);
        soundsMap.put(soundEffect, id);
    }


}
