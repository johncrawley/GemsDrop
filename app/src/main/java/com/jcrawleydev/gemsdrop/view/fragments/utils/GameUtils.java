package com.jcrawleydev.gemsdrop.view.fragments.utils;

import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.MainActivity;

import java.util.Collections;
import java.util.List;

public class GameUtils {

    public static String getFinalScoreString(Fragment fragment){
        var mainActivity = (MainActivity)fragment.getActivity();
        if(mainActivity != null){
            var viewModel = mainActivity.getViewModel();
            var gameModel = viewModel.gameModel;
            return gameModel.getScore().getStr();
        }
        else{
            log("getFinalScoreStr() main activity is null!");
        }
        return "999";
    }

    private static void log(String msg){
        System.out.println("^^^ GameUtils: ! " + msg);
    }


    public static List<String> getHighScores(Fragment fragment){
        var mainActivity = (MainActivity)fragment.getActivity();
        if(mainActivity != null){
            var viewModel = mainActivity.getViewModel();
            return viewModel.highScores.getOrderedHighScores();
        }
        return Collections.emptyList();
    }
}
