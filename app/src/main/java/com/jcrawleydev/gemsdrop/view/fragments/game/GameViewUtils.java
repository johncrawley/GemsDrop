package com.jcrawleydev.gemsdrop.view.fragments.game;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Map;
import java.util.function.BiConsumer;

public class GameViewUtils {


    public static void updateViewFrom(DrawInfo drawInfo,
                                      Map<Long, ImageView> map,
                                      BiConsumer<DrawInfo, ImageView> removalConsumer,
                                      Context context,
                                      ViewGroup gamePane,
                                      Map<ItemType, Integer> itemTypeMap){
        long id = drawInfo.getId();
        if(map == null){
            return;
        }
        if(!map.containsKey(id)){
            ImageView item = createItemAndAddToGamePane(drawInfo, context, gamePane, itemTypeMap);
            map.put(id, item);
        }
        ImageView view = map.get(id);
        if(view != null){
            view.setX(drawInfo.getX());
            view.setY(drawInfo.getY());
            view.setRotation(drawInfo.getCurrentRotation());
            drawInfo.incrementRotation();
            removalConsumer.accept(drawInfo, view);
        }
    }


    static ImageView createItemAndAddToGamePane(DrawInfo drawInfo,
                                                Context context,
                                                ViewGroup gamePane,
                                                Map<ItemType, Integer> itemTypeMap) {
        ImageView itemView = createImageViewWithDimensionsFrom(drawInfo, context);
        gamePane.addView(itemView);
        setImage(itemView, drawInfo, itemTypeMap);
        setImageViewCoordinates(itemView, drawInfo);
        return itemView;
    }


    static ImageView createImageViewWithDimensionsFrom(DrawInfo drawInfo, Context context){
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(drawInfo.getWidth(), drawInfo.getHeight()));
        return imageView;
    }


    static void setImage(ImageView imageView, DrawInfo drawInfo, Map<ItemType, Integer> itemTypeMap){
        Integer imageResource = itemTypeMap.get(drawInfo.getItemType());
        if(imageResource != null){
            imageView.setImageResource(imageResource);
        }
    }


    static void setImageViewCoordinates(ImageView imageView, DrawInfo drawInfo){
        imageView.setY(drawInfo.getY());
        imageView.setX(drawInfo.getX());
    }




}
