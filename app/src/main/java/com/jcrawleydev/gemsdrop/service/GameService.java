package com.jcrawleydev.gemsdrop.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.service.audio.SoundEffect;
import com.jcrawleydev.gemsdrop.service.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.service.records.ScoreRecords;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class GameService extends Service {
    IBinder binder = new LocalBinder();
    private MainActivity mainActivity;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> notifyGameOverFuture;
    private SoundPlayer soundPlayer;
    GamePreferenceManager gamePreferenceManager;
    private ScoreRecords scoreRecords;


    public GameService() {
        super();
    }


    public void playSound(SoundEffect soundEffect){
        soundPlayer.playSound(soundEffect);
    }


    public SharedPreferences getScorePrefs(){
        return getSharedPreferences("score_preferences", MODE_PRIVATE);
    }




    public void setLevel(int value){
        if(gamePreferenceManager != null){
            gamePreferenceManager.saveLevel(value);
        }
    }


    public int getLevel(){
        if(gamePreferenceManager != null){
            return gamePreferenceManager.getLevel();
        }
        return 5;
    }


    public void resetScore(){
        if(mainActivity != null) {
            mainActivity.setScore(0);
        }
    }


    public void updateScore(int score){
        if(mainActivity != null){
            mainActivity.setScore(score);
        }
    }


    public void notifyThatGameFinished(){
        if(notifyGameOverFuture != null && !notifyGameOverFuture.isCancelled()){
            notifyGameOverFuture.cancel(false);
        }
    }



    @Override
    public void onCreate() {
        soundPlayer = new SoundPlayer(getApplicationContext());
        scoreRecords = new ScoreRecords(getApplicationContext());
        gamePreferenceManager = new GamePreferenceManager();
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY; // service is not restarted when terminated
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }


    @Override
    public void onRebind(Intent intent) {
    }


    @Override
    public void onDestroy() {
        mainActivity = null;
    }


    public boolean isActivityUnbound(){
        return mainActivity == null;
    }


    public void setActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }


    public void notifyGameViewReady(){
    }


    public class LocalBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

}