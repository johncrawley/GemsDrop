package com.jcrawleydev.gemsdrop.view.fragments.game;

import static com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils.setListener;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils;

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
    private LinearLayout gemContainer;
    private int gemDimension = 100;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assignContainerDimensions(container);
        View parentView = inflater.inflate(R.layout.fragment_game, container, false);
        itemsMap = new HashMap<>();
        imageMap = new ImageMap();
        setupViews(parentView);
        setupListeners();
        startGame();
        return parentView;
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


    private void setupViews(View parentView){
        gemContainer = parentView.findViewById(R.id.gemContainer);
        gemContainer.setOnClickListener(v -> rotateGems());
    }


    private void rotateGems(){
        log("Entered rotateGems()");
        getService().ifPresent(GameService::rotateGems);
    }


    private void log(String msg){
        System.out.println("^^^ GameFragment: " + msg);
    }


    private void updateViewsFrom(List<DrawInfo> drawInfoList, Map<Long, ImageView> viewMap, BiConsumer<DrawInfo, ImageView> removalConsumer){
        runOnUiThread(()-> {
            for (DrawInfo drawInfo : drawInfoList) {
               // updateViewFrom(drawInfo, viewMap, removalConsumer, getContext(), gamePane, itemTypeMap);
            }
        });
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
    }


    private void setupListener(FragmentMessage message, Consumer<Bundle> consumer){
        setListener(this, message, consumer);
    }


    private void onServiceConnected(Bundle b){
        startGame();
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
        gem.setLayoutParams(new LinearLayout.LayoutParams(gemDimension, gemDimension, 1.0f));
    }


    private void updateGemCoordinates(ImageView gem, int position, int column){
        log("Entered updateGemCoordinates() position: " + position + " , column: " + column);
        gem.setX(getXForColumn(column));
        gem.setY(getYForPosition(position));
    }


    private int getYForPosition(int position){
        return position * 50;
    }


    private int getXForColumn(int column){
        return column * 100;
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
