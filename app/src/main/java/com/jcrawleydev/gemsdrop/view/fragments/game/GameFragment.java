package com.jcrawleydev.gemsdrop.view.fragments.game;

import static com.jcrawleydev.gemsdrop.view.fragments.game.GameViewUtils.updateViewFrom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class GameFragment extends Fragment {

    private ImageMap imageMap;
    private Map<Long, ImageView> itemsMap;
    private int containerWidth, containerHeight, smallestContainerDimension;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getContainerDimensions(container);
        View parentView = inflater.inflate(R.layout.fragment_game, container, false);
        itemsMap = new HashMap<>();
        imageMap = new ImageMap();

        return parentView;
    }


    public void updateItems(List<DrawInfo> drawInfoList) {
      //  updateViewsFrom(drawInfoList, itemsMap, this::removeEnemyShip);
    }


    private void updateViewsFrom(List<DrawInfo> drawInfoList, Map<Long, ImageView> viewMap, BiConsumer<DrawInfo, ImageView> removalConsumer){
        runOnUiThread(()-> {
            for (DrawInfo drawInfo : drawInfoList) {
               // updateViewFrom(drawInfo, viewMap, removalConsumer, getContext(), gamePane, itemTypeMap);
            }
        });
    }


    private void runOnUiThread(Runnable runnable){
        if(getActivity() == null){
            return;
        }
        getActivity().runOnUiThread(runnable);
    }



    private void getContainerDimensions(ViewGroup container){
        if(container != null){
            containerWidth = container.getMeasuredWidth();
            containerHeight = container.getMeasuredHeight();
            smallestContainerDimension = Math.min(containerWidth, containerHeight);
        }
    }
}
