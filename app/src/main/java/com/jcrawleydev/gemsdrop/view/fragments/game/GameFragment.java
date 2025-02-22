package com.jcrawleydev.gemsdrop.view.fragments.game;

import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_COLOR_IDS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_COLUMNS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_IDS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_POSITIONS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.CREATE_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.CREATE_WONDER_GEM;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.FREE_FALL_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.NOTIFY_OF_SERVICE_CONNECTED;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.REMOVE_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_COLORS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_SCORE;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.getIntArrayFrom;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.getIntFrom;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.getLongArray;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.getLongArrayFrom;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.setListener;


import android.annotation.SuppressLint;
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
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.service.GameService;
import com.jcrawleydev.gemsdrop.service.game.gem.GemColor;
import com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class GameFragment extends Fragment {

    private ImageMap imageMap;
    private Map<Long, ViewGroup> itemsMap;
    private int containerWidth;
    private int containerHeight;
    private ViewGroup gemContainer, gamePane;
    private float gemWidth = 10f;
    private GameInputHandler gameInputHandler;
    private TextView scoreView;
    private AnimationDrawable wonderGemAnimation;
    private int currentNumberOfGemsRemoved;
    private int numberOfGemsToRemove;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_game, container, false);
        itemsMap = new ConcurrentHashMap<>();
        imageMap = new ImageMap();
        gemContainer = parentView.findViewById(R.id.gemContainer);
        gamePane = parentView.findViewById(R.id.game_pane);
        gameInputHandler = new GameInputHandler(this);
        assignLayoutDimensions();
        setupViews(parentView);
        setupListeners();
        setupCreateAndDestroyButtons(parentView);
        return parentView;
    }


    private void assignLayoutDimensions(){
        ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                assignGemContainerDimensions();
                assignWidthToExistingGems();
                log("assignLayoutDimensions() about to invoke startGame()");
                startGame();
                getService().ifPresent(GameService::notifyGameViewReady);
                log("assignLayoutDimensions() exited startGame()");
                gamePane.getViewTreeObserver().removeOnGlobalLayoutListener(this);
               // createWonderGem(1000000L, 5,5);
            }
        };
        gamePane.getViewTreeObserver().addOnGlobalLayoutListener(listener);
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
        log("assignGemContainerLayoutParams()");
        gemContainer.setLayoutParams(params);
    }


    private void assignWidthToExistingGems(){
        for(View gemView : itemsMap.values()){
            setGemViewDimensions(gemView);
        }
    }


    private void startGame(){
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.getGameService().ifPresent(GameService::startGame);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupViews(View parentView){
        ViewGroup gamePane = parentView.findViewById(R.id.game_pane);
        gamePane.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                gameInputHandler.handleInput(motionEvent.getX(), motionEvent.getY(), gamePane);
            }
            return false;
        });

        scoreView = parentView.findViewById(R.id.scoreView);
    }


    private void log(String msg){
     //  System.out.println("^^^ GameFragment: " + msg);
    }


    public Optional<GameService> getService(){
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null) {
            return mainActivity.getGameService();
        }
        return Optional.empty();
    }


    private void setupListeners(){
        setupListener(CREATE_GEMS, this::createGems);
        setupListener(CREATE_WONDER_GEM, this::createGems);
        setupListener(UPDATE_GEMS, this::updateGems);
        setupListener(UPDATE_COLORS, this::updateGemsColors);
        setupListener(NOTIFY_OF_SERVICE_CONNECTED, this::onServiceConnected);
        setupListener(REMOVE_GEMS, this::removeGems);
        setupListener(UPDATE_SCORE, this::updateScore);
        setupListener(FREE_FALL_GEMS, this::freeFallGems);
    }


    private void setupListener(FragmentMessage message, Consumer<Bundle> consumer){
        setListener(this, message, consumer);
    }


    private void onServiceConnected(Bundle b){
        startGame();
    }


    private void freeFallGems(Bundle bundle){
        doActionOnGemIdsFrom(bundle, this::dropGemLayout);
    }


    private void dropGemLayout(ViewGroup gemLayout){
        float updatedY = gemLayout.getY() + (gemWidth / 2f);
        gemLayout.setY(updatedY);
    }


    private void removeGems(Bundle bundle){
        numberOfGemsToRemove = getLongArrayFrom(bundle, GEM_IDS).length;
        stopWonderGemAnimation();
        doActionOnGemIdsFrom(bundle, gemLayout -> GemAnimator.animateRemovalOf(gemLayout, this::cleanupGem));
    }


    private void stopWonderGemAnimation(){
        if(wonderGemAnimation != null && wonderGemAnimation.isRunning()){
            wonderGemAnimation.stop();
        }
    }


    private void doActionOnGemIdsFrom(Bundle bundle, Consumer<ViewGroup> consumer){
        long[] gemIds = getLongArray(bundle, GEM_IDS);
        if(gemIds == null){
            return;
        }
        for(long gemId : gemIds){
            ViewGroup gemLayout = itemsMap.get(gemId);
            if(gemLayout != null){
                consumer.accept(gemLayout);
            }
        }
    }


    private void cleanupGem(ViewGroup gemLayout){
        long id = (long)gemLayout.getTag();
        log("Entered cleanupGem() id: " + id);
        gemLayout.setVisibility(View.GONE);
        gemContainer.removeView(gemLayout);
        itemsMap.remove(id);
        currentNumberOfGemsRemoved++;
        if(currentNumberOfGemsRemoved >= numberOfGemsToRemove){
            notifyGameOfGemRemovalCompletion();
        }
    }


    private void notifyGameOfGemRemovalCompletion(){
        getService().ifPresent(GameService::onGemRemovalAnimationDone);
    }


    private void createGems(Bundle bundle){
        long[] ids = getLongArrayFrom(bundle, GEM_IDS);
        int[] positions = getIntArrayFrom(bundle, GEM_POSITIONS);
        int[] columns = getIntArrayFrom(bundle, GEM_COLUMNS);
        int[] colorIds = getIntArrayFrom(bundle, GEM_COLOR_IDS);

        if(colorIds[0] == GemColor.WONDER.ordinal()){
            createWonderGem(ids[0], positions[0], columns[0]);
            return;
        }

        for(int i = 0; i < ids.length; i++){
            createGem(ids[i], positions[i], columns[i], colorIds[i]);
        }
    }


    private void createWonderGem(long id, int position, int column){
        ViewGroup wonderGemLayout = createGem(id, position, column);
        startWonderGemAnimation(wonderGemLayout);
    }


    private void startWonderGemAnimation(ViewGroup gemLayout){
        ImageView wonderGemView = (ImageView) gemLayout.getChildAt(0);
        wonderGemView.setBackgroundResource(R.drawable.wonder_gem_animation);
        wonderGemAnimation = (AnimationDrawable) wonderGemView.getBackground();
        wonderGemAnimation.start();
    }


    private void updateGems(Bundle bundle){
        long[] ids = getLongArrayFrom(bundle, GEM_IDS);
        int[] positions = getIntArrayFrom(bundle, GEM_POSITIONS);
        int[] columns = getIntArrayFrom(bundle, GEM_COLUMNS);

        for(int i = 0; i < ids.length; i++){
            updateGem(ids[i], positions[i], columns[i]);
        }
    }


    private void updateGemsColors(Bundle bundle){
        long[] ids = getLongArrayFrom(bundle, GEM_IDS);
        int[] colorIds = getIntArrayFrom(bundle, GEM_COLOR_IDS);

        for(int i = 0; i < ids.length; i++){
            updateColorOf(ids[i], colorIds[i]);
        }
    }


    private void updateColorOf(long id, int colorId){
        if(itemsMap.containsKey(id)){
            updateGemColor(itemsMap.get(id), colorId);
        }
    }


    private void createGem(long id, int position, int column, int colorId){
        var gemLayout = createGem(id, position, column);
        updateGemColor(gemLayout, colorId);
    }


    private ViewGroup createGem(long id, int position, int column){
        var gemLayout = createAndAddGemLayout(id, position, column);
        itemsMap.put(id, gemLayout);
        return gemLayout;
    }


    private void updateGem(long id, int position, int column){
        var gemView = itemsMap.computeIfAbsent(id, k -> createAndAddGemLayout(id, position, column));
        updateGemCoordinates(gemView, position, column);
    }


    private void updateScore(Bundle bundle){
        int score = getIntFrom(bundle, BundleTag.SCORE);
        score++;
        String scoreVal = String.valueOf(score);
        scoreView.setText(scoreVal);
    }


    private ViewGroup createAndAddGemLayout(long id, int position, int column){
        log("entered createAndAddGemView()");
        LinearLayout gemLayout = new LinearLayout(getContext());
        gemLayout.setTag(id);
        ImageView imageView = new ImageView(getContext());
        updateGemCoordinates(gemLayout, position, column);
        setGemViewDimensions(imageView);
       // imageView.getDrawable().setRem(new BlurMaskFilter(8, BlurMaskFilter.Blur.SOLID));
        //imageView.setRenderEffect(RenderEffect.createBlurEffect(2.0f, 2.0f, Shader.TileMode.MIRROR));
        gemLayout.addView(imageView);
        log("createAndAddGemLayout() about to setLayoutParams on gemLayout");
        setLayoutParamsOn(gemLayout);
        gemContainer.addView(gemLayout);
        return gemLayout;
    }


    private void setLayoutParamsOn(ViewGroup gemLayout){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.TOP;
        log("setLayoutParamsOn() about to set layout params, gravity is top, weight is 1.0f, linearLayout.LayoutParams");
        gemLayout.setLayoutParams(params);
    }


    private void setGemViewDimensions(View gemView){
        var layoutParams = new LinearLayout.LayoutParams((int)gemWidth, (int)gemWidth);
        log("setGemViewDimensions(), about to setLayoutParams on gemView");
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


    private void setupCreateAndDestroyButtons(View parentView){

    }

}
