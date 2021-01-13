package com.jcrawleydev.gemsdrop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;

import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.view.TransparentView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private int width,height;
    private SurfaceView surfaceView1;
    // private Animator animator;
    private TransparentView transparentView;
    private GemGroupFactory gemGroupFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gemGroupFactory = new GemGroupFactory(3, 300, -100, 100);
        assignScreenDimensions();
        //stateManager = new StateManager(this, width, height);
        //drawSurface = new DrawSurface(this, stateManager, width, height);
        surfaceView1 = findViewById(R.id.surfaceView);
        transparentView = findViewById(R.id.transparent_view);
        // surfaceView1.setZOrderOnTop(true);    // necessary
        //animator = new Animator(MainActivity.this, surfaceView1);
        transparentView.setOnClickListener(this);
        transparentView.setGemGroup(gemGroupFactory.createGemGroup());
        ;

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
        alreadyStarted = true;
        GemGroup gemGroup = gemGroupFactory.createGemGroup();
        transparentView.setGemGroup(gemGroup);
        GemDropTask gemDropTask = new GemDropTask(gemGroup, transparentView, this);
        ExecutorService threadPoolExecutor = Executors.newScheduledThreadPool(1);
        threadPoolExecutor.execute(gemDropTask);
    }

    public void resetDrop(){
        alreadyStarted = false;
    }

    private void resetGems(){

    }
}

class GemDropTask implements Runnable{
    private GemGroup gemGroup;
    private TransparentView transparentView;
    private MainActivity mainActivity;

    public GemDropTask(GemGroup gemGroup, TransparentView transparentView, MainActivity mainActivity){
        this.gemGroup = gemGroup;
        this.transparentView = transparentView;
        this.mainActivity = mainActivity;
    }

    public void run(){
        while(gemGroup.getY() < 1000){
            gemGroup.drop();
            transparentView.updateAndDraw();
            transparentView.invalidate();
            try {
                Thread.sleep(30);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        mainActivity.resetDrop();
    }


}