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
    private final Map<SoundEffect, Integer> soundsMap = new HashMap<>();
    private boolean isSoundEnabled = true;

    public SoundPlayer(Context context){
        this.context = context;
        setupSoundPool();
        loadSounds();
    }


    public void setSoundEnabled(boolean isSoundEnabled){
        this.isSoundEnabled = isSoundEnabled;
    }


    private void setupSoundPool(){
        var attributes = new AudioAttributes.Builder()
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
            log("playSound() sound effect is not enabled, returning (" + soundEffect.name() + ")");
            return;
        }
        var soundId = soundsMap.get(soundEffect);
        if(soundId != null){
            log("playSound() about to play sound from soundPool");
            soundPool.play(soundId, volume, volume, 1, repeats, 1);
        }else{
            log("playSound() soundId is null!");
        }
    }


    private void loadSounds(){
        log("Entered loadSounds()");
        loadSound(R.raw.ground_hit_2, GEM_HITS_FLOOR);
        loadSound(R.raw.disappear_1, GEMS_DISAPPEAR);
        loadSound(R.raw.disappear_2, GEMS_DISAPPEAR_CHAIN_REACTION_1);
        loadSound(R.raw.disappear_3, GEMS_DISAPPEAR_CHAIN_REACTION_2);
        loadSound(R.raw.disappear_4, GEMS_DISAPPEAR_CHAIN_REACTION_3);
        loadSound(R.raw.disappear_5, GEMS_DISAPPEAR_CHAIN_REACTION_4);
        loadSound(R.raw.disappear_6, GEMS_DISAPPEAR_CHAIN_REACTION_5);
        loadSound(R.raw.disappear_7, GEMS_DISAPPEAR_CHAIN_REACTION_6);
        loadSound(R.raw.disappear_wonder, WONDER_GEM_GEMS_DISAPPEAR);
        loadSound(R.raw.game_over_1, GAME_OVER);
        loadSound(R.raw.silence, SILENCE);
    }


    private void log(String msg){
        System.out.println("^^^ SoundPlayer: " + msg);
    }

    private void loadSound(int soundResId, SoundEffect soundEffect){
        int id = soundPool.load(context, soundResId, 1);
        soundsMap.put(soundEffect, id);
    }


}
