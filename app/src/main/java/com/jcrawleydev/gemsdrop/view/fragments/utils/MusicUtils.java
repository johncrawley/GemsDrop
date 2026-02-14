package com.jcrawleydev.gemsdrop.view.fragments.utils;

import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.audio.MusicPlayer;

public class MusicUtils {

    public static void playTrack(Fragment fragment, int resId){
        var musicPlayer = getMusicPlayer(fragment);
        if(musicPlayer != null){
            musicPlayer.play();
        }
    }


    public static void stopTrack(Fragment fragment){
        var musicPlayer = getMusicPlayer(fragment);
        if(musicPlayer != null){
            musicPlayer.fadeOut();
        }
    }


    private static MusicPlayer getMusicPlayer(Fragment fragment){
        var mainActivity = (MainActivity)fragment.getActivity();
        if(mainActivity != null){
            var viewModel = mainActivity.getViewModel();
            if(viewModel != null){
                return viewModel.musicPlayer;
            }
        }
        return null;
    }
}
