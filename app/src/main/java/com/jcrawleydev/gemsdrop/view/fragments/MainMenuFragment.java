package com.jcrawleydev.gemsdrop.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.service.audio.SoundEffect;
import com.jcrawleydev.gemsdrop.view.SettingsActivity;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainMenuFragment extends Fragment {

    private final AtomicBoolean isGameStartInitiated = new AtomicBoolean(false);
    private TitleGemsAnimator titleGemsAnimator;


    public MainMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isGameStartInitiated.set(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_main_menu, container, false);
        isGameStartInitiated.set(false);
        setupButtons(parent);
        titleGemsAnimator = new TitleGemsAnimator(parent, getContext());
        titleGemsAnimator.start();
        return parent;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(titleGemsAnimator != null){
            titleGemsAnimator.stop();
        }
    }


    private void setupButtons(View parent){
        setupButton(parent, R.id.newGameButton, this::startGame);
        setupButton(parent, R.id.optionsButton, this::startSettingsActivity);
        setupButton(parent, R.id.aboutButton, this::goToAboutPage);
    }


    private void setupButton(View parent, int buttonId, Runnable onClick){
        Button button = parent.findViewById(buttonId);
        button.setOnClickListener(v -> {
            playMenuButtonSound();
            onClick.run();
        });
    }


    private void startGame(){
        if(isGameStartInitiated.get() || getActivity() == null){
            return;
        }
        isGameStartInitiated.set(true);
        FragmentUtils.loadGame(this);
    }


    private void goToAboutPage(){
        FragmentUtils.loadAbout(this);
    }


    private void startSettingsActivity(){
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }


    private void playMenuButtonSound(){
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.playSound(SoundEffect.MENU_BUTTON);
        }
    }


}
