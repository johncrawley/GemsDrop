package com.jcrawleydev.gemsdrop.view.fragments.utils;

import android.content.res.Resources;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.widget.TextView;


public class GraphicUtils {

    public static void assignGradient(TextView view, Resources resources, int resId1, int resId2){

        int startColor = resources.getColor(resId1, null);
        int endColor = resources.getColor(resId2, null);
        float y1 = view.getMeasuredHeight() /2f;
        System.out.println("^^^ GraphicUtils.assignGradient() y1: " + y1);
        var shader = new LinearGradient(0, 0, 0, y1,
                new int[]{startColor, endColor},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        view.getPaint().setShader(shader);
    }
}
