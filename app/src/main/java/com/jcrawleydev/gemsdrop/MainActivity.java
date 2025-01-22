package com.jcrawleydev.gemsdrop;

import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_COLOR_IDS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_COLUMNS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_IDS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_POSITIONS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.CREATE_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_COLORS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_SCORE;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.addIntArrayTo;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.addLongArrayTo;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.createBundleOf;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.sendMessage;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;

import com.jcrawleydev.gemsdrop.service.GameService;
import com.jcrawleydev.gemsdrop.service.game.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.score.ScoreStatistics;
import com.jcrawleydev.gemsdrop.view.GameView;
import com.jcrawleydev.gemsdrop.view.fragments.MainMenuFragment;
import com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.ToIntFunction;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, GameView {

    private MainViewModel viewModel;
    private GameService gameService;

    private final AtomicBoolean isServiceConnected = new AtomicBoolean(false);


    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            gameService = binder.getService();
            gameService.setActivity(MainActivity.this);
            sendMessage(MainActivity.this, FragmentMessage.NOTIFY_OF_SERVICE_CONNECTED);
            isServiceConnected.set(true);
        }


        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isServiceConnected.set(false);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideActionBar();
        setupGameService();
        setupFragmentsIf(savedInstanceState == null);
        initViewModel();
    }


    private void setupFragmentsIf(boolean isSavedStateNull) {
        if(!isSavedStateNull){
            return;
        }
        Fragment mainMenuFragment = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mainMenuFragment)
                .commit();
    }


    private void setupGameService() {
        Intent intent = new Intent(getApplicationContext(), GameService.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent, connection, 0);
    }


    public void onGameOver(ScoreStatistics scoreStatistics){

    }


    public void setScore(int score){
        Bundle bundle = createBundleOf(BundleTag.SCORE, score);
        sendMessage(this, UPDATE_SCORE, bundle);
    }


    @Override
    public void createGems(List<Gem> gems) {
        var bundle = createGemUpdateBundleFor(gems);
        addGemColorIdsTo(bundle, gems);
        log("createGems() bundle created, about to send to fragment");
        sendMessage(this, CREATE_GEMS, bundle);
    }

    private void log(String msg){
        System.out.println("^^^ MainActivity: " +  msg);
    }


    @Override
    public void updateGems(List<Gem> gems){
        var bundle = createGemUpdateBundleFor(gems);
        sendMessage(this, UPDATE_GEMS, bundle);
    }


    @Override
    public void updateGemsColors(List<Gem> gems) {
        Bundle bundle = new Bundle();
        addGemIdsTo(bundle, gems);
        addGemColorIdsTo(bundle, gems);
        sendMessage(this, UPDATE_COLORS, bundle);
    }


    private Bundle createGemUpdateBundleFor(List<Gem> gems){
        var bundle = new Bundle();
        addGemIdsTo(bundle, gems);
        addTo(bundle, GEM_POSITIONS, gems, Gem::getContainerPosition);
        addTo(bundle, GEM_COLUMNS, gems, Gem::getColumn);
        return bundle;
    }


    private void addGemIdsTo(Bundle bundle, List<Gem> gems){
       addLongArrayTo(bundle, GEM_IDS, getGemIds(gems));
    }


    private void addTo(Bundle bundle, BundleTag tag, List<Gem> gems, ToIntFunction<Gem> func){
        addIntArrayTo(bundle, tag, getFrom(gems, func));
    }


    private long[] getGemIds(List<Gem> gems){
        return gems.stream().mapToLong(Gem::getId).toArray();
    }


    private void addGemColorIdsTo(Bundle bundle, List<Gem> gems){
        addIntArrayTo(bundle, GEM_COLOR_IDS, getGemColorIds(gems));
    }


    private int[] getGemColorIds(List<Gem> gems){
        return getFrom(gems, Gem::getColorId);
    }


    private int[] getFrom(List<Gem> gems, ToIntFunction<Gem> consumer){
        return gems.stream().mapToInt(consumer).toArray();
    }


    @Override
    public void wipeOut(long[] markedGemIds){
        Bundle bundle = new Bundle();
        bundle.putLongArray(GEM_IDS.toString(), markedGemIds);
        sendMessage(this, FragmentMessage.REMOVE_GEMS, bundle);
    }


    public void playSound(SoundPlayer.Sound sound){

    }


    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }


    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }


    @Override
    public void onPause(){
        /*
        if(game != null){
            game.onPause();
        }
        */

        super.onPause();
    }


    @Override
    public void onResume(){
        /*
        if(game != null){
            // game.onResume();
        }

         */
        super.onResume();
    }


    public View getMainView(){
        return null;
      //  return findViewById(R.id.titleViewInclude);
    }


    public Optional<GameService> getGameService(){
        return Optional.ofNullable(gameService);
    }


    public MainViewModel getViewModel(){
        return this.viewModel;
    }


    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View v, MotionEvent e){
        if(e.getAction() != MotionEvent.ACTION_DOWN){
            return true;
        }
        int x = (int)e.getX();
        int y = (int)e.getY();
        //game.click(x, y);
        return true;
    }

}
