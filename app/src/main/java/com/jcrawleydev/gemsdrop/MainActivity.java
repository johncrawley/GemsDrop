package com.jcrawleydev.gemsdrop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;

import com.jcrawleydev.gemsdrop.view.TransparentView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private int width,height;
    private SurfaceView surfaceView1;
    // private Animator animator;
    private TransparentView transparentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignScreenDimensions();
        //stateManager = new StateManager(this, width, height);
        //drawSurface = new DrawSurface(this, stateManager, width, height);
        surfaceView1 = findViewById(R.id.surfaceView);
        transparentView = findViewById(R.id.transparent_view);
        // surfaceView1.setZOrderOnTop(true);    // necessary
        //animator = new Animator(MainActivity.this, surfaceView1);
        transparentView.setOnClickListener(this);


    }

    public void onClick(View v){

    }

    private void assignScreenDimensions(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

}