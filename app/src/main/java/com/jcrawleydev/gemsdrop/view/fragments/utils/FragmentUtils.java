package com.jcrawleydev.gemsdrop.view.fragments.utils;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.view.fragments.AboutFragment;
import com.jcrawleydev.gemsdrop.view.fragments.HighScoresFragment;
import com.jcrawleydev.gemsdrop.view.fragments.game.GameFragment;
import com.jcrawleydev.gemsdrop.view.fragments.GameOverFragment;
import com.jcrawleydev.gemsdrop.view.fragments.main.MainMenuFragment;


public class FragmentUtils {


    public static void showDialog(Fragment parentFragment, DialogFragment dialogFragment, String tag, Bundle bundle){
        FragmentManager fragmentManager = parentFragment.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentTransaction, tag);
    }


    public static void loadGame(Fragment parentFragment){
        loadFragment(parentFragment, new GameFragment(), "game_fragment");
    }


    public static void loadHighScores(Fragment parentFragment){
        loadFragmentFade(parentFragment, new HighScoresFragment(), "game_fragment");
    }


    public static void loadAbout(Fragment parentFragment){
        loadFragment(parentFragment, new AboutFragment(), "about_fragment");
    }


    public static void loadGameOver(Fragment parentFragment){
        loadFragmentFade(parentFragment, new GameOverFragment(), "game_over_fragment");
    }


    public static void loadMainMenu(Fragment parentFragment){
        loadFragment(parentFragment, new MainMenuFragment(), "main_menu_fragment");
    }


    public static OnBackPressedCallback loadMainMenuOnBackButtonPressed(Fragment parentFragment){
       return handleBackButtonPress(parentFragment, () -> loadMainMenu(parentFragment));
    }


    public static OnBackPressedCallback loadMainMenuOnBackButtonPressed(Fragment parentFragment, Runnable runnable){
        return handleBackButtonPress(parentFragment, () -> {
            runnable.run();
            loadMainMenu(parentFragment);
        });
    }



    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag, Bundle bundle){
        loadFragment(parentFragment,fragment, tag, bundle, R.anim.slide_in, R.anim.slide_out);
    }


    public static void loadFragmentFade(Fragment parentFragment, Fragment fragment, String tag, Bundle bundle){
        loadFragment(parentFragment,fragment, tag, bundle, R.anim.fade_in, R.anim.fade_out);
    }


    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag, Bundle bundle, int animationInRes, int animationOutRes){
        var fragmentManager = parentFragment.getParentFragmentManager();
        var fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .setCustomAnimations(animationInRes, animationOutRes, R.anim.pop_enter, R.anim.pop_exit )
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }


    public static OnBackPressedCallback handleBackButtonPress(Fragment parentFragment, Runnable action){
        var callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                action.run();
            }
        };
        parentFragment.requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(parentFragment.getViewLifecycleOwner(), callback);
        return callback;
    }


    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag){
        loadFragment(parentFragment, fragment, tag, new Bundle());
    }

    public static void loadFragmentFade(Fragment parentFragment, Fragment fragment, String tag){
        loadFragmentFade(parentFragment, fragment, tag, new Bundle());
    }


    private static void removePreviousFragmentTransaction(FragmentManager fragmentManager, String tag, FragmentTransaction fragmentTransaction){
        var prev = fragmentManager.findFragmentByTag(tag);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
    }

}