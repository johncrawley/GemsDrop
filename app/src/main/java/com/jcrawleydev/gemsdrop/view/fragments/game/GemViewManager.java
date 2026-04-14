package com.jcrawleydev.gemsdrop.view.fragments.game;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.gem.GemColor;

import java.util.ArrayList;
import java.util.List;

public class GemViewManager {


    private AnimationDrawable wonderGemAnimation;
    private final ImageMap imageMap = new ImageMap();
    private float gemWidth = 10f;
    private int containerWidth, containerHeight;
    private ImageView gemPreview1, gemPreview2, gemPreview3;
    private AnimationHelper animationHelper;


    public void createGemView(ViewGroup gemContainer, Context context, Gem gem){
        var id = gem.getId();
        var position = gem.getContainerPosition();
        var col = gem.getColumn();
        if (gem.isWonderGem()) {
            createWonderGemView(gemContainer, context, id, position, col);
        }
        else{
            createGemView(gemContainer, context, id, position, col, gem.getColorId());
        }
    }


    public void assignGemContainerLayoutParams(ViewGroup gemContainer){
        var params = new LinearLayout.LayoutParams(containerWidth, containerHeight);
        gemContainer.setLayoutParams(params);
    }



    public void assignWidthToExistingGems(ViewGroup gemContainer){
        for(int i = 0; i < gemContainer.getChildCount(); i++){
            var gemLayout = gemContainer.getChildAt(i);
            if(gemLayout.getTag() != null){
                setGemViewDimensions(gemLayout);
            }
        }
    }


    public void updateGem(ViewGroup gemContainer, Context context, long id, int position, int column){
        var gemLayout = (ViewGroup) gemContainer.findViewWithTag(id);
        if(gemLayout == null){
            gemLayout = createAndAddGemLayout(gemContainer, context, id, position, column);
        }
        if(gemLayout != null){
            updateGemCoordinates(gemContainer, gemLayout, position, column);
        }
    }


    private void createGemView(ViewGroup gemContainer, Context context, long id, int position, int column, int colorId){
        var existingGemLayout = gemContainer.findViewWithTag(id);
        if(existingGemLayout == null){
            var gemLayout = createGemView(gemContainer, context, id, position, column);
            updateGemColor(gemLayout, context,  colorId);
        }
    }


    private void updateGemColor(ViewGroup gemLayout, Context context, int colorId){
        var gemImageView = getGemViewFrom(gemLayout);
        setGemDrawable(gemImageView, context, colorId);
    }


    private void setGemDrawable(ImageView gem, Context context, int colorId){
        var id = imageMap.getDrawableIdFor(colorId);
        var drawable = getDrawableFor(context, id);
        gem.setImageDrawable(drawable);
    }


    private Drawable getDrawableFor(Context context, int id){
        return ResourcesCompat.getDrawable(context.getResources(), id, null);
    }


    private ImageView getGemViewFrom(ViewGroup gemLayout){
        return (ImageView) gemLayout.getChildAt(0);
    }


    private int getYForPosition(ViewGroup gemContainer, int position){
        float containerBottom = gemContainer.getY() + containerHeight;
        return (int)containerBottom
                - ((int)gemWidth + (position * (int)(gemWidth / 2f)));
    }


    private int getXForColumn(int column){
        return column * (int) gemWidth;
    }


    public void reduceGemContainerHeightAndWidth(ViewGroup gamePane){
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


    private ViewGroup createGemView(ViewGroup gemContainer, Context context, long id, int position, int column){
        return createAndAddGemLayout(gemContainer, context, id, position, column);
    }


    private ViewGroup createAndAddGemLayout(ViewGroup gemContainer, Context context, long id, int position, int column){
        if(context == null){
            return null;
        }
        var gemLayout = new LinearLayout(context);
        gemLayout.setTag(id);
        var imageView = new ImageView(context);
        updateGemCoordinates(gemContainer, gemLayout, position, column);
        setGemViewDimensions(imageView);
        gemLayout.addView(imageView);
        setLayoutParamsOn(gemLayout);
        gemContainer.addView(gemLayout);
        return gemLayout;
    }


    public void updateColorOf(ViewGroup gemContainer, Context context, long id, int colorId){
        ViewGroup gemLayout = gemContainer.findViewWithTag(id);
        if(gemLayout != null){
            updateGemColor(gemLayout, context, colorId);
        }
    }


    private void setGemViewDimensions(View gemView){
        var layoutParams = new LinearLayout.LayoutParams((int)gemWidth, (int)gemWidth);
        gemView.setLayoutParams(layoutParams);
    }


    private void updateGemCoordinates(ViewGroup gemContainer, ViewGroup gemLayout, int position, int column){
        gemLayout.setX(getXForColumn(column));
        gemLayout.setY(getYForPosition(gemContainer, position));
    }


    private void createWonderGemView(ViewGroup gemContainer, Context context, long id, int position, int column){
        var wonderGemLayout = createGemView(gemContainer, context, id, position, column);
        if(wonderGemLayout != null){
            startWonderGemAnimation(wonderGemLayout);
        }
    }


    public void stopWonderGemAnimation(){
        if(wonderGemAnimation != null && wonderGemAnimation.isRunning()){
            wonderGemAnimation.stop();
        }
    }


    private void setLayoutParamsOn(ViewGroup gemLayout){
        var params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.TOP;
        gemLayout.setLayoutParams(params);
    }


    private void startWonderGemAnimation(ViewGroup gemLayout){
        ImageView wonderGemView = (ImageView) gemLayout.getChildAt(0);
        wonderGemView.setBackgroundResource(R.drawable.wonder_gem_animation);
        wonderGemAnimation = (AnimationDrawable) wonderGemView.getBackground();
        wonderGemAnimation.start();
    }


    public void updateGemsPreview(List<GemColor> gemColors, Context context){
        var updates = new ArrayList<GemPreviewUpdate>();
        var imageViews = List.of(gemPreview1, gemPreview2, gemPreview3);

        for(int i = 0; i < imageViews.size(); i++){
            var imageView = imageViews.get(i);
            var drawable = getDrawableFor(gemColors.get(i), context);
            updates.add(new GemPreviewUpdate(imageView, drawable ));
        }
        animationHelper.animatePreviewChangeFor(updates);
    }


    private Drawable getDrawableFor(GemColor gemColor, Context context){
        var id = imageMap.getDrawableIdFor(gemColor.ordinal());
        return getDrawableFor(context, id);
    }


    public void setupGemPreviews(View parentView) {
        ViewGroup previewLayout = parentView.findViewById(R.id.gemsPreviewLayout);
        animationHelper = new AnimationHelper(previewLayout);
        gemPreview1 = setupGemPreview(previewLayout, 0);
        gemPreview2 = setupGemPreview(previewLayout, 1);
        gemPreview3 = setupGemPreview(previewLayout, 2);
    }


    private ImageView setupGemPreview(ViewGroup previewLayout, int index){
        ImageView gemPreview = (ImageView) previewLayout.getChildAt(index);
        setPreviewGemDimensions(gemPreview);
        return gemPreview;
    }


    private void setPreviewGemDimensions(View gemView){
        var layoutParams = new LinearLayout.LayoutParams((int)(gemWidth / 1.5), (int)(gemWidth / 1.5));
        gemView.setLayoutParams(layoutParams);
    }
}
