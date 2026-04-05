package com.jcrawleydev.gemsdrop.view.fragments.game;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.MainViewModel;
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class GameFragment extends Fragment implements GameView {

    private final ImageMap imageMap = new ImageMap();
    private final Map<Long, ViewGroup> itemsMap = new ConcurrentHashMap<>();
    private int containerWidth;
    private int containerHeight;
    private ViewGroup gemContainer, gamePane;
    private float gemWidth = 10f;
    private TextView scoreView;
    private AnimationDrawable wonderGemAnimation;
    private int currentNumberOfGemsRemoved;
    private int numberOfGemsToRemove;
    private Game game;
    private MainViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_game, container, false);
        assignViewModel();
        setupViews(parent);
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
        int backgroundIndex = Math.min(randomIndex, ids.size()-1);

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
        ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                assignGemContainerDimensions();
                assignWidthToExistingGems();
                initGame();
                gamePane.getViewTreeObserver().removeOnGlobalLayoutListener(this);

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
                createGemView(gem);
            }
        });
    }


    private void createGemView(Gem gem){
        var id = gem.getId();
        var position = gem.getContainerPosition();
        var col = gem.getColumn();
        if (gem.isWonderGem()) {
            createWonderGemView(id, position, col);
        }
        else{
            createGemView(id, position, col, gem.getColorId());
        }
    }

    @Override
    public void updateGems(List<Gem> gems) {
        runOnUiThread(()->{
            for(var gem: gems){
                var id = gem.getId();
                var position = gem.getContainerPosition();
                var col = gem.getColumn();
                updateGem(id, position, col);
            }
        });
    }


    @Override
    public void updateGemsColors(List<Gem> gems) {
        runOnUiThread(()->{
            for(var gem : gems){
                updateColorOf(gem.getId(), gem.getColorId());
            }
        });
    }


    @Override
    public void wipeOut(long[] markedGemIds) {
        numberOfGemsToRemove = markedGemIds.length;
        currentNumberOfGemsRemoved = 0;
        runOnUiThread(()->{
            stopWonderGemAnimation();
            doActionOnGemIdsFrom(markedGemIds, gemLayout -> GemAnimator.animateRemovalOf(gemLayout, this::cleanupGemView));
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
        if(gemContainer == null) {
            return;
        }
        reduceGemContainerHeightAndWidth();
        assignGemContainerLayoutParams();
    }


    private void reduceGemContainerHeightAndWidth(){
        int availableHeight = gamePane.getMeasuredHeight();
        int minimumBorder = 50;
        containerWidth = gamePane.getMeasuredWidth() - minimumBorder;
        containerHeight = Integer.MAX_VALUE;
        int numberOfRows = 15;
        while(containerHeight > (availableHeight - minimumBorder)){
            containerWidth -= 2;
            gemWidth = containerWidth / 7f;
            containerHeight = (int)(gemWidth * numberOfRows);
        }
    }


    private void assignGemContainerLayoutParams(){
        var params = new LinearLayout.LayoutParams(containerWidth, containerHeight);
        gemContainer.setLayoutParams(params);
    }


    private void assignWidthToExistingGems(){
        for(View gemView : itemsMap.values()){
            setGemViewDimensions(gemView);
        }
    }


    private void setupViews(View parentView){
        gemContainer = parentView.findViewById(R.id.gemContainer);
        gamePane = parentView.findViewById(R.id.game_pane);
        setupTouchListener();
        scoreView = parentView.findViewById(R.id.scoreView);
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


    private void stopWonderGemAnimation(){
        if(wonderGemAnimation != null && wonderGemAnimation.isRunning()){
            wonderGemAnimation.stop();
        }
    }


    private void doActionOnGemIdsFrom(long[] gemIds, Consumer<ViewGroup> consumer){
        for(long gemId : gemIds){
            var gemLayout = itemsMap.get(gemId);
            if(gemLayout != null){
                consumer.accept(gemLayout);
            }
        }
    }


    private void cleanupGemView(ViewGroup gemLayout){
        long id = (long)gemLayout.getTag();
        gemLayout.setVisibility(View.GONE);
        gemContainer.removeView(gemLayout);
        itemsMap.remove(id);
        currentNumberOfGemsRemoved++;
        if(currentNumberOfGemsRemoved >= numberOfGemsToRemove){
            game.onGemRemovalAnimationDone();
        }
    }


    private void createWonderGemView(long id, int position, int column){
        var wonderGemLayout = createGemView(id, position, column);
        startWonderGemAnimation(wonderGemLayout);
    }


    private void startWonderGemAnimation(ViewGroup gemLayout){
        ImageView wonderGemView = (ImageView) gemLayout.getChildAt(0);
        wonderGemView.setBackgroundResource(R.drawable.wonder_gem_animation);
        wonderGemAnimation = (AnimationDrawable) wonderGemView.getBackground();
        wonderGemAnimation.start();
    }


    private void updateColorOf(long id, int colorId){
        if(itemsMap.containsKey(id)){
            updateGemColor(itemsMap.get(id), colorId);
        }
    }


    private void createGemView(long id, int position, int column, int colorId){
        if(itemsMap.containsKey(id)){
            return;
        }
        var gemLayout = createGemView(id, position, column);
        updateGemColor(gemLayout, colorId);
    }


    private ViewGroup createGemView(long id, int position, int column){
        var gemLayout = createAndAddGemLayout(id, position, column);
        itemsMap.put(id, gemLayout);
        return gemLayout;
    }


    private void updateGem(long id, int position, int column){
        var gemView = itemsMap.computeIfAbsent(id, k -> createAndAddGemLayout(id, position, column));
        updateGemCoordinates(gemView, position, column);
    }



    private ViewGroup createAndAddGemLayout(long id, int position, int column){
        var gemLayout = new LinearLayout(getContext());
        gemLayout.setTag(id);
        var imageView = new ImageView(getContext());
        updateGemCoordinates(gemLayout, position, column);
        setGemViewDimensions(imageView);
        gemLayout.addView(imageView);
        setLayoutParamsOn(gemLayout);
        gemContainer.addView(gemLayout);
        return gemLayout;
    }


    private void setLayoutParamsOn(ViewGroup gemLayout){
        var params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.TOP;
        gemLayout.setLayoutParams(params);
    }


    private void setGemViewDimensions(View gemView){
        var layoutParams = new LinearLayout.LayoutParams((int)gemWidth, (int)gemWidth);
        gemView.setLayoutParams(layoutParams);
    }


    private void setGemDrawable(ImageView gem, int colorId){
        var id = imageMap.getDrawableIdFor(colorId);
        var drawable = getDrawableFor(id);
        gem.setImageDrawable(drawable);
    }


    private Drawable getDrawableFor(int id){
        return ResourcesCompat.getDrawable(getResources(), id, null);
    }


    private void updateGemCoordinates(ViewGroup gemLayout, int position, int column){
        gemLayout.setX(getXForColumn(column));
        gemLayout.setY(getYForPosition(position));
    }


    private void updateGemColor(ViewGroup gemLayout, int colorId){
        var gemImageView = getGemViewFrom(gemLayout);
        setGemDrawable(gemImageView, colorId);
    }


    private ImageView getGemViewFrom(ViewGroup gemLayout){
        return (ImageView) gemLayout.getChildAt(0);
    }


    private int getYForPosition(int position){
        float containerBottom = gemContainer.getY() + containerHeight;
        return (int)containerBottom
                - ((int)gemWidth + (position * (int)(gemWidth / 2f)));
    }


    private int getXForColumn(int column){
        return column * (int) gemWidth;
    }


    private void runOnUiThread(Runnable runnable){
        var activity = getActivity();
        if(activity != null){
            activity.runOnUiThread(runnable);
        }
    }

}
