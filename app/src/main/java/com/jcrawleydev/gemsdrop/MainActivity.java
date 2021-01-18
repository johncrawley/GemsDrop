package com.jcrawleydev.gemsdrop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.tasks.GemDropTask;
import com.jcrawleydev.gemsdrop.view.GemGridView;
import com.jcrawleydev.gemsdrop.view.GemGroupView;
import com.jcrawleydev.gemsdrop.view.TransparentView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GemGroupFactory gemGroupFactory;
    private GemGroupView gemGroupView;
    int height, width;
    private boolean alreadyStarted = false;
    private ScheduledFuture <?> t;
    private int bottom = 1200;
    private GemGrid gemGrid;
    private GemGridView gemGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gemGroupFactory = new GemGroupFactory(3, 300, -100, 100);
        assignScreenDimensions();

        TransparentView gemGroupTransparentView = findViewById(R.id.gemGroupView);
        TransparentView gemGridTransparentView = findViewById(R.id.gemGridView);
        gemGroupTransparentView.setDimensions(width, height);
        gemGroupTransparentView.translateXToMiddle();
        gemGridTransparentView.setDimensions(width, height);
        gemGroupTransparentView.setOnClickListener(this);
        gemGridTransparentView.setOnClickListener(this);
        gemGroupView = new GemGroupView(gemGroupTransparentView, MainActivity.this, gemGroupFactory.createGemGroup());
        gemGrid = new GemGrid(10,12,3);
        gemGridView = new GemGridView(gemGridTransparentView, gemGrid, 150);
    }


    public void onClick(View v){
        log("Entered onClick");
        startGemDrop();
    }


    private void assignScreenDimensions(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }


    private void startGemDrop(){
        if(alreadyStarted){
            gemGroupView.rotate();
            //t.cancel(false);
            //alreadyStarted = false;
            return;
        }
        TaskProfiler taskProfiler = new TaskProfiler();
        ScheduledFuture <?> t;
        alreadyStarted = true;
        GemGroup gemGroup = gemGroupFactory.createGemGroup();
        gemGroupView.setGemGroup(gemGroup);
        GemDropTask gemDropTask = new GemDropTask(gemGroup, gemGrid, gemGroupView, gemGridView, this, taskProfiler);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        t = executor.scheduleWithFixedDelay(gemDropTask, 0, 300, TimeUnit.MILLISECONDS);
        setFuture(t);
    }


    public void setFuture(ScheduledFuture<?> t){
        this.t = t;
    }

    public void cancelFuture(){
        t.cancel(false);
    }

    public void resetDrop(){
        log("Entered resetDrop()");
        alreadyStarted = false;
    }

    private void log(String msg){
        System.out.println("MainActivity: " + msg);
    }

}
