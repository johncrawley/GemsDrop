package com.jcrawleydev.gemsdrop.view.fragments.game;

import static com.jcrawleydev.gemsdrop.view.fragments.game.GemAnimator.animateAppearanceOf;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_COLORS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_COLOR_IDS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_COLUMNS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_IDS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag.GEM_POSITIONS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.CREATE_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.FREE_FALL_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.NOTIFY_OF_SERVICE_CONNECTED;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.REMOVE_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_COLORS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_GEMS;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage.UPDATE_SCORE;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.getIntArrayFrom;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.getLongArray;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.getLongArrayFrom;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.setListener;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.service.GameService;
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
                startGame();
                gamePane.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        };
        gamePane.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }


    private void assignGemContainerDimensions(){
        if(gemContainer == null) {
            log("assignGemContainerDimensions() gemContainer is null, returning");
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
        int containerAdjustmentCount = 0;
        while(containerHeight > (availableHeight - minimumBorder)){
            containerAdjustmentCount++;
            containerWidth -= 2;
            gemWidth = containerWidth / 7f;
            containerHeight = (int)(gemWidth * numberOfRows);
            log("containerHeight adjustment, is now: " + containerHeight);
        }
        log("Exiting reduceGemContainerHeightAndWidth() container height: " + containerHeight + " game pane height: " + gamePane.getMeasuredHeight());
        log("Exiting reduceGemContainerHeightAndWidth() adjustments made: " + containerAdjustmentCount);
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
       System.out.println("^^^ GameFragment: " + msg);
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
        doActionOnGemIdsFrom(bundle, gemLayout -> GemAnimator.animateRemovalOf(gemLayout, this::cleanupGem));
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


    private void animateGems(){
        if(itemsMap.isEmpty()){
            return;
        }
        ViewGroup first = itemsMap.values().stream().findFirst().get();
        if(first.getVisibility() != View.VISIBLE){
            for(ViewGroup gemLayout: itemsMap.values()){
                animateAppearanceOf(gemLayout);
            }
        }
        else{
            for(ViewGroup gemLayout : itemsMap.values()){
                GemAnimator.animateRemovalOf(gemLayout, this::cleanupGem);
            }
        }
    }


    private void cleanupGem(ViewGroup gemLayout){
        long id = (long)gemLayout.getTag();
        gemLayout.setVisibility(View.GONE);
        gemContainer.removeView(gemLayout);
        itemsMap.remove(id);
        notifyGameOfGemRemovalCompletion();
    }


    private void notifyGameOfGemRemovalCompletion(){
        getService().ifPresent(GameService::onGemRemovalAnimationDone);
    }


    private void createGems(Bundle bundle){
        long[] ids = getLongArrayFrom(bundle, GEM_IDS);
        int[] positions = getIntArrayFrom(bundle, GEM_POSITIONS);
        int[] columns = getIntArrayFrom(bundle, GEM_COLUMNS);
        int[] colorIds = getIntArrayFrom(bundle, GEM_COLOR_IDS);

        if(ids == null){
            log("createGems() ids are null!");
        }
        if(positions == null){
            log("createGems() positions are null!");
        }
        if(columns == null){
            log("createGems() columns are null!");
        }
        if(colorIds == null){
            log("createGems() colorIds are null!");
        }

        for(int i = 0; i < ids.length; i++){
            createGem(ids[i], positions[i], columns[i], colorIds[i]);
        }
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
        itemsMap.put(id, createAndAddGemLayout(id, position, column, colorId));
    }


    private void updateGem(long id, int position, int column){
        var gemView = itemsMap.computeIfAbsent(id, k -> createAndAddGemLayout(id, position, column, 0));
        updateGemCoordinates(gemView, position, column);
    }


    private void updateScore(Bundle bundle){
        int score = getIntFrom(bundle, BundleTag.SCORE);
        scoreView.setText(score);
    }


    private int getIntFrom(Bundle bundle, BundleTag tag, int defaultValue){
        return bundle.getInt(tag.toString(), defaultValue);
    }


    private int getIntFrom(Bundle bundle, BundleTag tag){
        return getIntFrom(bundle, tag, 0);
    }


    private ViewGroup createAndAddGemLayout(long id, int position, int column, int colorId){
        log("entered createAndAddGemView()");
        LinearLayout gemLayout = new LinearLayout(getContext());
        gemLayout.setTag(id);
        ImageView imageView = new ImageView(getContext());
        setGemDrawable(imageView, colorId);
        updateGemCoordinates(gemLayout, position, column);
        setGemViewDimensions(imageView);
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
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),imageMap.getDrawableIdFor(colorId), null);
        gem.setImageDrawable(drawable);
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
