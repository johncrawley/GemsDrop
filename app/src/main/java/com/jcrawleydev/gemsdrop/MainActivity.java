package com.jcrawleydev.gemsdrop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.tasks.GemDropTask;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gemGroupFactory = new GemGroupFactory(3, 300, -100, 100);
        assignScreenDimensions();

        TransparentView transparentView = findViewById(R.id.transparent_view);
        transparentView.setDimensions(width, height);
        transparentView.setOnClickListener(this);
        gemGroupView = new GemGroupView(transparentView, MainActivity.this, gemGroupFactory.createGemGroup());
    }


    public void onClick(View v){
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
        GemDropTask gemDropTask = new GemDropTask(gemGroup, gemGroupView, this, taskProfiler);
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
        alreadyStarted = false;
    }

}
