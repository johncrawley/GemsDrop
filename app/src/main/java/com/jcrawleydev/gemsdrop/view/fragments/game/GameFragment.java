package com.jcrawleydev.gemsdrop.view.fragments.game;

import static com.jcrawleydev.gemsdrop.view.fragments.game.GemAnimator.animateAppearanceOf;
import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.setListener;

import android.annotation.SuppressLint;
import android.graphics.Color;
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

import java.util.List;
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
        startGame();
        setupCreateAndDestroyButtons(parentView);
        return parentView;
    }


    private void assignLayoutDimensions(){
        ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                assignGemContainerDimensions();
                assignWidthToExistingGems();
               // setupPositionMarkers();
               // setupTestLayouts();
                log("gemContainerWidth, containerHeight: " + containerWidth + "," + containerHeight );
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
        int numberOfRows = 16;
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


    public void updateItems(List<DrawInfo> drawInfoList) {
      //  updateViewsFrom(drawInfoList, itemsMap, this::removeEnemyShip);
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
    }


    private void setupPositionMarkers(){
        for(int i = 0; i < 30; i++){
            TextView textView = new TextView(getContext());
            gemContainer.addView(textView);
            String positionMarker = String.valueOf(i);
            int textX = 20;
            int textY = getYForPosition(i);
            textView.setText(positionMarker);
            textView.setX(textX);
            textView.setY(textY);

            textView.setTextSize(20);
            textView.setTextColor(Color.RED);
        }
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
            ViewGroup gemLayout= itemsMap.get(gemId);
            if(gemLayout != null){
                GemAnimator.animateRemovalOf(gemLayout, this::cleanupGem);
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
        //gemContainer.removeView(gemLayout);
      //  itemsMap.remove(id);
    }


    private void updateGem(Bundle bundle){
        int position = bundle.getInt(BundleTag.GEM_POSITION.toString(), -1);
        int column = bundle.getInt(BundleTag.GEM_COLUMN.toString(), 0);
        long id = bundle.getLong(BundleTag.GEM_ID.toString(), -1L);
        int colorId = bundle.getInt(BundleTag.GEM_COLOR.toString(),0 );

        var gemView = itemsMap.computeIfAbsent(id, k -> createAndAddGemLayout(id, position, column, colorId));
        updateGemCoordinates(gemView, position, column);
    }


    private ViewGroup createAndAddGemLayout(long id, int position, int column, int colorId){
        log("entered createAndAddGemView()");
        LinearLayout gemLayout = new LinearLayout(getContext());
        gemLayout.setTag(id);
        ImageView imageView = new ImageView(getContext());
        log("createAndAddGemView() colorId: " + colorId);
        setGemDrawable(imageView, colorId);
        updateGemCoordinates(gemLayout, position, column);
        setGemViewDimensions(imageView);
        gemLayout.addView(imageView);
        setLayoutParamsOn(gemLayout);
        gemContainer.addView(gemLayout);
        return gemLayout;
    }


    private void setLayoutParamsOn(ViewGroup gemLayout){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.TOP;
        gemLayout.setLayoutParams(params);
    }


    private void setGemViewDimensions(View gemView){
        var layoutParams = new LinearLayout.LayoutParams((int)gemWidth, (int)gemWidth);
        gemView.setLayoutParams(layoutParams);
    }


    private void setGemDrawable(ImageView gem, int colorId){
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),imageMap.getDrawableIdFor(colorId), null);
        gem.setImageDrawable(drawable);
    }


    private void updateGemCoordinates(ViewGroup gemLayout, int position, int column){
       // log("Entered updateGemCoordinates() position: " + position + " , column: " + column);
        gemLayout.setX(getXForColumn(column));
        gemLayout.setY(getYForPosition(position));
        log("updateGemCoordinates() position: " + position + " column: " + column);
    }


    private int getYForPosition(int position){
        /*
        log("getYForPosition(" + position + ") gemContainer measuredHeight: "
                + gemContainer.getMeasuredHeight()
                + " gem container y: " + gemContainer.getY()
                + " ; gem container height: " + containerHeight
                + " gem width: " + gemWidth ); */
        float containerBottom = gemContainer.getY() + containerHeight;
        int retVal = (int)containerBottom
                - ((int)gemWidth + (position * (int)(gemWidth / 2f)));
       // log("getYForPosition(" + position + ") returnValue: " + retVal);
        return retVal;
    }


    private void setupTestLayout(int color, int x, int y){
        LinearLayout view = new LinearLayout(getContext());
        view.setBackgroundColor(color);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)gemWidth, (int)gemWidth);
        view.setLayoutParams(params);
        gemContainer.addView(view);
        view.setX(x);
        view.setY(y);

    }


    private void setupTestHalfwayLayout(int color, int x, int y){
        LinearLayout view = new LinearLayout(getContext());
        view.setBackgroundColor(color);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(containerWidth, 2);
        view.setLayoutParams(params);
        gemContainer.addView(view);
        view.setX(x);
        view.setY(y);

    }

    private void setupLineLayout(int color, int x, int y){
        LinearLayout view = new LinearLayout(getContext());
        view.setBackgroundColor(color);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(containerWidth/2, 2);
        view.setLayoutParams(params);
        gemContainer.addView(view);
        view.setX(x);
        view.setY(y);

    }

    private void setupTestLayouts(){
        int testWidth = (int)gemWidth;
        setupTestLayout(Color.RED, 400, 10);
        setupTestLayout(Color.GREEN, 200, containerHeight - (testWidth));
        setupTestLayout(Color.YELLOW, 100, 0);
        setupTestLayout(Color.BLUE, 500, containerHeight - (testWidth + 10));
        setupTestHalfwayLayout(Color.GREEN, (containerWidth /2), (containerHeight / 2));

        log("setupTestLayouts() containerHeight: " + containerHeight);

        for(int i = 0; i < containerHeight; i = i + (int)gemWidth){
            log("setupTestLayouts() drawing line at y: " + i);
            setupLineLayout(Color.MAGENTA, 0, i);
        }
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
        Button animateButton = parentView.findViewById(R.id.animate);
        animateButton.setOnClickListener(v -> animateGems());

    }


    private void createGems(){
        getService().ifPresent(GameService::createGems);
    }


    private void destroyGems(){
        getService().ifPresent(GameService::destroyGems);
    }

}
