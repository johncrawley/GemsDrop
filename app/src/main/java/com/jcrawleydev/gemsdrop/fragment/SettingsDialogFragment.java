package com.jcrawleydev.gemsdrop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SettingsDialogFragment extends DialogFragment {

    private MainActivity activity;
    private EditText stationNameEditText, stationUrlEditText, linkEditText;
    private Button saveButton;
    private ExecutorService executorService;

    private ActivityResultLauncher<Intent> loadImageActivityResultLauncher;

    public static SettingsDialogFragment newInstance() {
        return new SettingsDialogFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        executorService = Executors.newFixedThreadPool(1);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (MainActivity) getActivity();
        if (activity == null) {
            return;
        }
        SwitchMaterial enableSoundFxSwitch = view.findViewById(R.id.enableSoundFxButton);
    }
}

