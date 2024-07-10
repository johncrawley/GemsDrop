package com.jcrawleydev.gemsdrop.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainMenuFragment extends Fragment {

    public static final String FRAGMENT_TAG = "main_menu_fragment";
    private final AtomicBoolean isGameStartInitiated = new AtomicBoolean(false);


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
        setupTitleText(parent);
        return parent;
    }


    private void setupButtons(View parent){
        setupButton(parent, R.id.startGameButton, this::startGame);
        setupButton(parent, R.id.settingsButton, this::startSettingsActivity);
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
            mainActivity.playSound(Sound.MENU_BUTTON);
        }
    }


    private void setupTitleText(View parentView){
        TextView titleText = parentView.findViewById(R.id.titleText);
     //   setTextForLandscape(this, R.string.title_text_landscape, titleText);
     //   addGradientTo(titleText, getContext(), isInLandscapeMode(this));
    }

}
