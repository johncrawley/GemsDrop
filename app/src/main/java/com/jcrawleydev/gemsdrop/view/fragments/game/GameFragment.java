package com.jcrawleydev.gemsdrop.view.fragments.game;

import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.setListener;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.service.GameService;
import com.jcrawleydev.gemsdrop.view.fragments.utils.BundleTag;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GameFragment extends Fragment {

    private ImageMap imageMap;
    private Map<Long, ImageView> itemsMap;
    private int containerWidth, containerHeight, smallestContainerDimension;
    private ViewGroup gemContainer;
    private float gemDimension = 100f;
    private int fragmentWidth, fragmentHeight;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assignContainerDimensions(container);
        View parentView = inflater.inflate(R.layout.fragment_game, container, false);
        itemsMap = new HashMap<>();
        imageMap = new ImageMap();
        //getFragmentDimensions(parentView);
        setupViews(parentView);
        setupListeners();
        startGame();
        return parentView;
    }


    private void getFragmentDimensions(View view){
        ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fragmentWidth = view.getMeasuredWidth();
                fragmentHeight = view.getMeasuredHeight();
                log("fragment dimensions: " + fragmentWidth + "," + fragmentHeight);
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        };
        view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }


    private void startGame(){
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.getGameService().ifPresent(GameService::startGame);
        }
    }


    public void updateItems(List<DrawInfo> drawInfoList) {
      //  updateViewsFrom(drawInfoList, itemsMap, this::removeEnemyShip);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupViews(View parentView){
        gemContainer = parentView.findViewById(R.id.gemContainer);
        getFragmentDimensions(gemContainer);
        gemContainer.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                handleInput(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });
    }


    private void handleInput(float x, float y){
        log("handleInput(" + (int)x + "," +  (int)y + ")");
        if( y < 350f){
            log("handleInput() move up!");
            moveUp();
            return;
        }
        if( y > 1100f){
            log("handleInput() move down!");
            moveDown();
            return;
        }
        if(x < fragmentWidth / 3f){
            log("handleInput() move left!");
            moveLeft();
            return;
        }
        if(x < fragmentWidth / 1.5f ){
            log("handleInput() rotate!");
            rotateGems();
            return;
        }
        log("handleInput() move right!!");
       moveRight();
    }


    private void moveLeft(){
        getService().ifPresent(GameService::moveLeft);
    }


    private void moveRight(){
        getService().ifPresent(GameService::moveRight);
    }


    private void moveUp(){
        getService().ifPresent(GameService::moveUp);
    }


    private void moveDown(){
        getService().ifPresent(GameService::moveDown);
    }


    private void rotateGems(){
        log("Entered rotateGems()");
        getService().ifPresent(GameService::rotateGems);
    }


    private void log(String msg){
       // System.out.println("^^^ GameFragment: " + msg);
    }


    private Optional<GameService> getService(){
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null) {
            return mainActivity.getGameService();
        }
        return Optional.empty();
    }


    private void setupListeners(){
        setupListener(FragmentMessage.UPDATE_GEM, this::updateGem);
        setupListener(FragmentMessage.NOTIFY_OF_SERVICE_CONNECTED, this::onServiceConnected);
        setupListener(FragmentMessage.REMOVE_GEMS, this::removeGems);
    }


    private void setupListener(FragmentMessage message, Consumer<Bundle> consumer){
        setListener(this, message, consumer);
    }


    private void onServiceConnected(Bundle b){
        startGame();
    }


    private void removeGems(Bundle bundle){
        long[] gemIds = bundle.getLongArray(BundleTag.GEM_IDS.toString());
        if(gemIds == null){
            return;
        }
        for(long gemId : gemIds){
            ImageView gemView = itemsMap.get(gemId);
            if(gemView != null){
                animateRemovalOf(gemView, gemId);
            }
        }
    }


    private void animateRemovalOf(ImageView gemView, long gemId){
        Animation animation = new ScaleAnimation(
                1f, 0f,
                1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true); // Needed to keep the result of the animation
        animation.setDuration(800);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                itemsMap.remove(gemId);
                gemContainer.removeView(gemView);
            }

            @Override public void onAnimationRepeat(Animation animation) {}
        });

        gemView.startAnimation(animation);
    }


    private void updateGem(Bundle bundle){
        int position = bundle.getInt(BundleTag.GEM_POSITION.toString(), -1);
        int column = bundle.getInt(BundleTag.GEM_COLUMN.toString(), 0);
        long id = bundle.getLong(BundleTag.GEM_ID.toString(), -1L);
        int colorId = bundle.getInt(BundleTag.GEM_COLOR.toString(),0 );

        ImageView gemView = itemsMap.computeIfAbsent(id, k -> createAndAddGemView(id, position, column, colorId));
        updateGemCoordinates(gemView, position, column);
    }


    private ImageView createAndAddGemView(long id, int position, int column, int colorId){
        ImageView imageView = new ImageView(getContext());
        log("createAndAddGemView() colorId: " + colorId);
        setGemDrawable(imageView, colorId);
        setGemDimensions(imageView);
        updateGemCoordinates(imageView, position, column);
        gemContainer.addView(imageView);
        itemsMap.put(id, imageView);
        return imageView;
    }


    private void setGemDrawable(ImageView gem, int colorId){
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),imageMap.getDrawableIdFor(colorId), null);
        gem.setImageDrawable(drawable);
    }


    private void setGemDimensions(ImageView gem){
        gemDimension = containerWidth / 7f;
        gem.setLayoutParams(new LinearLayout.LayoutParams((int)gemDimension, (int)gemDimension, 1.0f));
    }


    private void updateGemCoordinates(ImageView gem, int position, int column){
       // log("Entered updateGemCoordinates() position: " + position + " , column: " + column);
        gem.setX(getXForColumn(column));
        gem.setY(getYForPosition(position));
    }


    private int getYForPosition(int position){
        return (position * (int)(gemDimension / 2f)) - (int)gemDimension;
    }


    private int getXForColumn(int column){
        return column * (int)gemDimension;
    }


    private void runOnUiThread(Runnable runnable){
        if(getActivity() == null){
            return;
        }
        getActivity().runOnUiThread(runnable);
    }


    private void assignContainerDimensions(ViewGroup container){
        if(container != null){
            containerWidth = container.getMeasuredWidth();
            containerHeight = container.getMeasuredHeight();
            smallestContainerDimension = Math.min(containerWidth, containerHeight);
        }
    }
}
