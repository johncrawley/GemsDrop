package com.jcrawleydev.gemsdrop.view.fragments.game;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.MainViewModel;
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.game.gem.GemColor;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils;
import com.jcrawleydev.gemsdrop.view.fragments.utils.GraphicUtils;

import java.util.List;

public class GameFragment extends Fragment implements GameView {

    private ViewGroup gemContainer, gamePane, previewLayout;
    private TextView scoreView;
    private int currentNumberOfGemsRemoved;
    private int numberOfGemsToRemove;
    private Game game;
    private MainViewModel viewModel;
    private GemViewManager gemViewManager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_game, container, false);
        assignViewModel();
        setupViews(parent);
        gemViewManager = new GemViewManager();
        createGame();
        assignLayoutDimensions();
        setBackground(parent);
        FragmentUtils.loadMainMenuOnBackButtonPressed(this, this::quitGame);
        return parent;
    }



    private void setBackground(View parent){
        ViewGroup gameLayout = parent.findViewById(R.id.game_layout);

        var ids = List.of(
                 R.drawable.background_1,
        R.drawable.background_2,
        R.drawable.background_3,
        R.drawable.background_4,
        R.drawable.background_5,
        R.drawable.background_6);

        int randomIndex = viewModel.gameModel.getRandomBackgroundIndex();
        int backgroundIndex = Math.min(randomIndex, ids.size() - 1);

        gameLayout.setBackgroundResource(ids.get(backgroundIndex));
    }


    private void assignViewModel(){
        var mainActivity = (MainActivity)getActivity();
        if(mainActivity != null){
            viewModel = mainActivity.getViewModel();
        }
    }


    private void createGame(){
        game = new Game(viewModel.gameModel, this);
    }


    private void quitGame(){
        game.terminate();
    }


    private void initGame() {
        var soundPlayer = new SoundPlayer(getContext());
        game.init(soundPlayer);
    }


    @Override
    public void onDestroy(){
        if(game != null){
            game.onGameFragmentDestroy();
        }
        super.onDestroy();
    }


    private void assignLayoutDimensions(){
        var listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gamePane.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                assignGemContainerDimensions();
                gemViewManager.assignWidthToExistingGems(gemContainer);
                gemViewManager.setupGemPreviews(previewLayout);
                initGame();
                GraphicUtils.assignGradient(scoreView, getResources(), R.color.score, R.color.score_gradient);
            }
        };
        gamePane.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }


    @Override
    public void createGems(List<Gem> gems) {
        if(gems.isEmpty()){
            return;
        }
        runOnUiThread(() -> {
            for(var gem : gems) {
                gemViewManager.createGemView(gemContainer, getContext(), gem);
            }
        });
    }


    @Override
    public void updateGemsPreview(List<GemColor> gemColors) {
        runOnUiThread(()-> gemViewManager.updateGemsPreview(gemColors, getContext()));
    }


    @Override
    public void updateGems(List<Gem> gems) {
        runOnUiThread(()->{
            for(var gem: gems){
                var id = gem.getId();
                var position = gem.getContainerPosition();
                var col = gem.getColumn();
                gemViewManager.updateGem(gemContainer, getContext(), id, position, col);
            }
        });
    }


    @Override
    public void updateGemsColors(List<Gem> gems) {
        runOnUiThread(()->{
            for(var gem : gems){
               gemViewManager.updateColorOf(gemContainer, getContext(), gem.getId(), gem.getColorId());
            }
        });
    }


    @Override
    public void wipeOut(long[] markedGemIds) {
        numberOfGemsToRemove = markedGemIds.length;
        currentNumberOfGemsRemoved = 0;
        runOnUiThread(()->{
            gemViewManager.stopWonderGemAnimation();
            for(var markedId : markedGemIds){
                var gemLayout = (ViewGroup) gemContainer.findViewWithTag(markedId);
                if(gemLayout != null){
                    GemAnimator.animateRemovalOf(gemLayout, this::cleanupGemView);
                }
            }
        });
    }


    @Override
    public void updateScore(int score) {
        var scoreVal = String.valueOf(score);
        runOnUiThread(()-> scoreView.setText(scoreVal));
    }


    @Override
    public void showHighScores() {
        FragmentUtils.loadHighScores(this);
    }


    private void assignGemContainerDimensions(){
        if(gemContainer == null)  {
            return;
        }
        gemViewManager.reduceGemContainerHeightAndWidth(gamePane);
        gemViewManager.assignGemContainerLayoutParams(gemContainer);
    }


    private void setupViews(View parentView){
        gemContainer = parentView.findViewById(R.id.gemContainer);
        gamePane = parentView.findViewById(R.id.game_pane);
        setupTouchListener();
        setupScoreView(parentView);
        previewLayout = parentView.findViewById(R.id.gemsPreviewLayout);
    }


    private void setupScoreView(View parentView){
        scoreView = parentView.findViewById(R.id.scoreView);
        //scoreView.setColors(Color.RED, Color.BLUE);
        //GraphicUtils.assignGradient(scoreView, getResources(), R.color.score, R.color.score_gradient);
    }


    @Override
    public void loadGameOver(){
        FragmentUtils.loadGameOver(this);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupTouchListener(){
        gamePane.setOnTouchListener((view, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP){
                GameInputHandler.handleInput(event.getX(), event.getY(), gamePane, game);
            }
            return true;
        });
    }


    private void log(String msg){
       System.out.println("^^^ GameFragment: " + msg);
    }


    private void cleanupGemView(ViewGroup gemLayout){
        gemLayout.setVisibility(View.GONE);
        gemContainer.removeView(gemLayout);
        currentNumberOfGemsRemoved++;
        if(currentNumberOfGemsRemoved >= numberOfGemsToRemove){
            game.onGemRemovalAnimationDone();
        }
    }


    private void runOnUiThread(Runnable runnable){
        var activity = getActivity();
        if(activity != null){
            activity.runOnUiThread(runnable);
        }
    }

}
