package com.jcrawleydev.gemsdrop.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.SoundPlayer;
import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.score.ScoreRecords;
import com.jcrawleydev.gemsdrop.service.game.score.ScoreStatistics;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameService extends Service {
    IBinder binder = new LocalBinder();
    private MainActivity mainActivity;
    private final Game game;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> notifyGameOverFuture;
    private SoundPlayer soundPlayer;
    GamePreferenceManager gamePreferenceManager;
    private ScoreRecords scoreRecords;


    public GameService() {
        super();
        game = new Game();
    }


    public void playSound(SoundPlayer.Sound sound){
        soundPlayer.playSound(sound);
    }


    public SharedPreferences getScorePrefs(){
        return getSharedPreferences("score_preferences", MODE_PRIVATE);
    }


    public void rotateGems(){
        if(game != null){
            System.out.println("^^^ Entered rotateGems()");
            game.rotateGems();
        }
    }


    public void moveLeft(){
        game.moveLeft();
    }


    public void moveRight(){
        game.moveRight();
    }


    public void moveUp(){
        game.moveUp();
    }


    public void createGems(){
        game.createGems();
    }


    public void evalGems(){
        game.evalGems();
    }



    public void destroyGems(){
        game.destroyGems();
    }



    public void moveDown(){
        game.moveDown();
    }


    public void setLevel(int value){
        if(gamePreferenceManager != null){
            gamePreferenceManager.saveLevel(value);
        }
        if(game != null){
          //  game.setDifficulty(value);
        }
    }

    public void create(){
        game.create();
    }


    public int getLevel(){
        if(gamePreferenceManager != null){
            return gamePreferenceManager.getLevel();
        }
        return 5;
    }


    public void quitGame(){
        game.quit();
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


    public void onGameOver(ScoreStatistics scoreStatistics){
        ScoreStatistics fullScoreStats = scoreRecords.getCompleteScoreStatsAndSaveRecords(scoreStatistics);
        notifyGameOverFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> mainActivity.onGameOver(fullScoreStats), 0, 2, TimeUnit.SECONDS);
    }


    public void notifyThatGameFinished(){
        notifyGameOverFuture.cancel(false);
    }


    public void startGame(){
        game.startGame();
    }


    @Override
    public void onCreate() {
        game.init();
        soundPlayer = new SoundPlayer(getApplicationContext());
        gamePreferenceManager = new GamePreferenceManager(this);
        setupScoreRecords();
    }


    private void setupScoreRecords(){
        scoreRecords = new ScoreRecords();
       // scoreRecords.setScorePreferences(new ScorePreferencesImpl(getScorePrefs()));
       // scoreRecords.setCurrentDateCreator(new CurrentDateGeneratorImpl());
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
        game.onDestroy();
    }


    public boolean isActivityUnbound(){
        return mainActivity == null;
    }


    public void setActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        game.setView(mainActivity);
    }


    public class LocalBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

}