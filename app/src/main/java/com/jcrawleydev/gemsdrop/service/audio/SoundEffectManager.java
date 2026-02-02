package com.jcrawleydev.gemsdrop.service.audio;

import com.jcrawleydev.gemsdrop.game.score.Score;

public class SoundEffectManager {

    private SoundPlayer soundPlayer;
    private final Score score;

    public SoundEffectManager(Score score){
        this.score = score;
    }


    public void init(SoundPlayer soundPlayer){
        this.soundPlayer = soundPlayer;
    }


    public void playSoundEffect(SoundEffect soundEffect){
        if(soundPlayer != null){
            soundPlayer.playSound(soundEffect);
        }
    }


    public void playGemsRemovedSound(){
        if(soundPlayer == null){
            return;
        }
        var soundEffect = switch (score.getMultiplier()){
            case 1 -> SoundEffect.GEMS_DISAPPEAR;
            case 2 -> SoundEffect.GEMS_DISAPPEAR_CHAIN_REACTION_1;
            case 3 -> SoundEffect.GEMS_DISAPPEAR_CHAIN_REACTION_2;
            case 4 -> SoundEffect.GEMS_DISAPPEAR_CHAIN_REACTION_3;
            case 5 -> SoundEffect.GEMS_DISAPPEAR_CHAIN_REACTION_4;
            case 6 -> SoundEffect.GEMS_DISAPPEAR_CHAIN_REACTION_5;
            default -> SoundEffect.GEMS_DISAPPEAR_CHAIN_REACTION_6;
        };
        soundPlayer.playSound(soundEffect);
    }


    public void playWonderGemRemovedSound(int numberOfGemsRemoved){
        if(soundPlayer == null){
            return;
        }
        var soundEffect = numberOfGemsRemoved > 1
                ? SoundEffect.WONDER_GEM_GEMS_DISAPPEAR
                : SoundEffect.WONDER_GEM_HITS_FLOOR;
        soundPlayer.playSound(soundEffect);
    }

}
