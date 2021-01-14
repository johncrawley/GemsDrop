package com.jcrawleydev.gemsdrop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;

import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.tasks.GemDropTask;
import com.jcrawleydev.gemsdrop.view.TransparentView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TransparentView transparentView;
    private GemGroupFactory gemGroupFactory;
    int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gemGroupFactory = new GemGroupFactory(3, 300, -100, 100);
        assignScreenDimensions();

        transparentView = findViewById(R.id.transparent_view);
        transparentView.setDimensions(width, height);
        transparentView.setOnClickListener(this);
        transparentView.setGemGroup(gemGroupFactory.createGemGroup());

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

    private boolean alreadyStarted = false;
    private void startGemDrop(){
        if(alreadyStarted){
            return;
        }
        taskProfiler = new TaskProfiler();
        ScheduledFuture <?> t;
        alreadyStarted = true;
        GemGroup gemGroup = gemGroupFactory.createGemGroup();
        transparentView.setGemGroup(gemGroup);
        GemDropTask gemDropTask = new GemDropTask(gemGroup, transparentView, this, taskProfiler);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        //threadPoolExecutor.execute(gemDropTask);
        t = executor.scheduleWithFixedDelay(gemDropTask, 0, 300, TimeUnit.MILLISECONDS);
        setFuture(t);
    }
    private ScheduledFuture <?> t;
    private TaskProfiler taskProfiler;

    public void setFuture(ScheduledFuture<?> t){
        this.t = t;
    }

    public void cancelFuture(){
        t.cancel(false);
    }

    public void resetDrop(){
        alreadyStarted = false;
    }

    private void resetGems(){

    }
}
