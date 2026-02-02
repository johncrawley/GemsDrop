package com.jcrawleydev.gemsdrop;

import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_COLOR_IDS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_COLUMNS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_IDS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_POSITIONS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.SCORE;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.CREATE_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.SHOW_GAME_OVER_MESSAGE;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.SHOW_HIGH_SCORES;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_COLORS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_SCORE;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.addIntArrayTo;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.addIntTo;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.addLongArrayTo;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.createBundleOf;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.sendMessage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.service.GamePreferenceManager;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.score.ScoreStatistics;
import com.jcrawleydev.gemsdrop.service.audio.SoundEffect;
import com.jcrawleydev.gemsdrop.service.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.service.records.ScoreRecords;
import com.jcrawleydev.gemsdrop.view.GameView;
import com.jcrawleydev.gemsdrop.view.fragments.MainMenuFragment;
import com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.function.ToIntFunction;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, GameView {

    private MainViewModel viewModel;
    private Game game;
    private SoundPlayer soundPlayer;
    private ScoreRecords scoreRecords;
    private GamePreferenceManager gamePreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewModel();
        hideActionBar();
        initGame();
        setupFragmentsIf(savedInstanceState == null);
    }


    public void initGame() {
        game = new Game(viewModel.gameModel);
        game.setView(this);
        soundPlayer = new SoundPlayer(getApplicationContext());
        scoreRecords = new ScoreRecords(getApplicationContext());
        game.init(soundPlayer, scoreRecords);
        gamePreferenceManager = new GamePreferenceManager();
    }


    private void setupViewModel(){
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
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


    public void onGameOver(ScoreStatistics scoreStatistics){

    }


    public void showHighScores(){
        sendMessage(this, SHOW_HIGH_SCORES);
    }


    public void setScore(int score){
        Bundle bundle = createBundleOf(BundleTag.SCORE, score);
        sendMessage(this, UPDATE_SCORE, bundle);
    }


    @Override
    public void createGems(List<Gem> gems) {
        var bundle = createGemUpdateBundleFor(gems);
        addGemColorIdsTo(bundle, gems);
        sendMessage(this, CREATE_GEMS, bundle);
    }


    @Override
    public void updateScore(int score){
        var bundle = new Bundle();
        addIntTo(bundle, SCORE, score);
        sendMessage(this, UPDATE_SCORE, bundle);
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


    @Override
    public void showGameOverAnimation(){
        sendMessage(this, SHOW_GAME_OVER_MESSAGE);
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


    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }


    @Override
    public void onPause(){

        super.onPause();
    }


    @Override
    public void onResume(){
        super.onResume();
    }


    /*public Optional<GameService> getGameService(){
        return Optional.ofNullable(gameService);
    }


     */

    @NonNull
    public Game getGame(){
        return game;
    }


    public void playSound(SoundEffect soundEffect){
        soundPlayer.playSound(soundEffect);
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
