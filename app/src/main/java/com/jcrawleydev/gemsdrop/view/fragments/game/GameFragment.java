package com.jcrawleydev.gemsdrop.view.fragments.game;

import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.setListener;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class GameFragment extends Fragment {

    private ImageMap imageMap;
    private Map<Long, ImageView> itemsMap;
    private int containerWidth, containerHeight, smallestContainerDimension;
    private ViewGroup gemContainer;
    private float gemWidth = 10f;
    private int fragmentWidth, fragmentHeight;
    private final int GEM_COLUMNS = 7;
    private final int GEM_ROWS = GEM_COLUMNS * 2;
    private int containerBottomY = 500;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //assignContainerDimensions(container);
        View parentView = inflater.inflate(R.layout.fragment_game, container, false);
        itemsMap = new ConcurrentHashMap<>();
        imageMap = new ImageMap();
        getFragmentDimensions(parentView);
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
                FrameLayout container = view.findViewById(R.id.gemContainer);
                assignGemContainerDimensions(view, container);
                log("gemContainerWidth, containerHeight: " + containerWidth + "," + containerHeight );
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        };
        view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }


    private void assignGemContainerDimensionsOLD(View parent, FrameLayout container){
        if(container != null){

            int parentWidth = parent.getMeasuredWidth();

            int marginWidth = parentWidth / 12;

            containerWidth = parentWidth - marginWidth;
            containerHeight = containerWidth * 2;
            int marginHeight = containerHeight / 16;
            int adjustedContainerHeight = containerHeight - marginHeight;


            int adjustedWith = containerWidth - 100;
            // var layoutParams;// = new FrameLayout.LayoutParams(containerWidth, adjustedContainerHeight);
            containerWidth = 400;
            containerHeight = (int)(containerWidth * 2.4f);
            var layoutParams = new LinearLayout.LayoutParams(containerWidth, containerHeight);
            container.setLayoutParams(layoutParams);
            smallestContainerDimension = Math.min(containerWidth, containerHeight);
            log("assignContainerDimensions() containerWidth, height: " + containerWidth + "," + containerHeight);
            assignGemWidth(containerWidth);
            containerBottomY = (int)container.getY() + containerHeight;
        }
    }


    private void assignGemContainerDimensions(View parent, FrameLayout container){
        if(container != null){
            int otherViewsHeight = 800;
            int remainingAvailableHeight = parent.getMeasuredHeight() - otherViewsHeight;
            int maxWidth = parent.getMeasuredWidth() - 50;
            int numberOfRows = 16;
            int adjustmentCount = 0;

            containerWidth = maxWidth;
            containerHeight = Integer.MAX_VALUE;
            log("^^^^^^^^^^^^^^^^^^^^^^^^^^^^ assignGemContainerDimensions ^^^^^^^^^^^^^^^^^^^");
            while(containerHeight > remainingAvailableHeight){
                adjustmentCount++;
                containerWidth -= 10;
                gemWidth = containerWidth / 7f;
                containerHeight = (int)(gemWidth * numberOfRows);
               // log(" ----> "  + adjustmentCount + " <-----  current container dimensions: " + containerWidth + "," + containerHeight, " available height: ");

            }
            log("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ^^^^^^^^^^^^^^^^^^^");

            var layoutParams = new LinearLayout.LayoutParams(containerWidth, containerHeight);
            container.setLayoutParams(layoutParams);
            smallestContainerDimension = Math.min(containerWidth, containerHeight);
            log("assignContainerDimensions() containerWidth, height: " + containerWidth + "," + containerHeight + " ---- adjustment count: " + adjustmentCount);
            assignGemWidth(containerWidth);
            containerBottomY = (int)container.getY() + containerHeight;
        }
    }


    private void assignGemWidth( int gemContainerWidth){
        gemWidth = gemContainerWidth / 7f;

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


    public void updateItems(List<DrawInfo> drawInfoList) {
      //  updateViewsFrom(drawInfoList, itemsMap, this::removeEnemyShip);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupViews(View parentView){
        gemContainer = parentView.findViewById(R.id.gemContainer);
        gemContainer.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                handleInput(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });
        setupPositionMarkers(gemContainer);
    }


    private void setupPositionMarkers(ViewGroup container){
        for(int i = 30; i >0; i--){
            TextView textView = new TextView(getContext());
            container.addView(textView);
            String positionMarker = String.valueOf(i);
            textView.setText(positionMarker);
            textView.setY(getYForPosition(i));
            textView.setX(20);
            textView.setTextSize(20);
            textView.setTextColor(Color.RED);
        }
    }


    private void handleInput(float x, float y){

        log("handleInput(" + (int)x + "," +  (int)y + ")" + " gemContainer AbsoluteY: "+ gemContainer.getY() + " height: " + gemContainer.getMeasuredHeight());
        int height = gemContainer.getMeasuredHeight();
        int width = gemContainer.getMeasuredWidth();
        if( y < height/4f){
            log("handleInput() move up!");
            moveUp();
            return;
        }
        if( y > (height / 3f) * 2){
            log("handleInput() move down!");
            moveDown();
            return;
        }
        if(x < width / 3f){
            log("handleInput() move left!");
            moveLeft();
            return;
        }
        if(x < width / 1.5f ){
            log("handleInput() rotate!");
            rotateGems();
            return;
        }
        log("handleInput() move right!!");
       moveRight();
    }


    private void moveLeft(){
        runOnService(GameService::moveLeft);
    }


    private void moveRight(){
        runOnService(GameService::moveRight);
    }


    private void moveUp(){
        runOnService(GameService::moveUp);
    }


    private void moveDown(){
        runOnService(GameService::moveDown);
    }


    private void createGems(){
        runOnService(GameService::createGems);
    }


    private void destroyGems(){
        runOnService(GameService::destroyGems);
    }


    private void runOnService(Consumer<GameService> consumer){
        getService().ifPresent(consumer);
    }


    private void rotateGems(){
        log("Entered rotateGems()");
        getService().ifPresent(GameService::rotateGems);
    }


    private void log(String msg){
       System.out.println("^^^ GameFragment: " + msg);
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
        log("entered createAndAddGemView()");
        ImageView imageView = new ImageView(getContext());
        log("createAndAddGemView() colorId: " + colorId);
        setGemDrawable(imageView, colorId);
        updateGemCoordinates(imageView, position, column);
        setGemViewDimensions(imageView, false);
        gemContainer.addView(imageView);
        //itemsMap.put(id, imageView);
        return imageView;
    }


    private void setGemViewDimensions(View gemView){
        log("entered setGemViewDimensions");
        var layoutParams = new LinearLayout.LayoutParams((int)gemWidth, (int)gemWidth);
        gemView.setLayoutParams(layoutParams);
    }

    private void setGemViewDimensions(View gemView, boolean x){
        log("entered setGemViewDimensions for createAndAddGemView()");
        var layoutParams = new LinearLayout.LayoutParams((int)gemWidth, (int)gemWidth);
        gemView.setLayoutParams(layoutParams);
    }


    private void setGemDrawable(ImageView gem, int colorId){
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),imageMap.getDrawableIdFor(colorId), null);
        gem.setImageDrawable(drawable);
    }


    private void updateGemCoordinates(ImageView gem, int position, int column){
       // log("Entered updateGemCoordinates() position: " + position + " , column: " + column);
        gem.setX(getXForColumn(column));
        gem.setY(getYForPosition(position));
        log("updateGemCoordinates() position: " + position + " column: " + column);
    }


    private int getYForPosition(int position){
        return gemContainer.getMeasuredHeight()
                - (int)gemWidth
                - (position * (int)(gemWidth / 2f));
    }


    private int getXForColumn(int column){
        return column * (int) gemWidth;
    }


    private void runOnUiThread(Runnable runnable){
        if(getActivity() == null){
            return;
        }
        getActivity().runOnUiThread(runnable);
    }

    private void setupCreateAndDestroyButtons(View parentView){
        Button createButton = parentView.findViewById(R.id.create);
        createButton.setOnClickListener(v -> createGems());
        Button destroyButton = parentView.findViewById(R.id.destroy);
        destroyButton.setOnClickListener(v -> destroyGems());
    }


}
