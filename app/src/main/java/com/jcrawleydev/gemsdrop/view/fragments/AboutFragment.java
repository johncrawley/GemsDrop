package com.jcrawleydev.gemsdrop.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.R;


public class AboutFragment extends Fragment {




    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_about, container, false);
        return parent;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }


}
