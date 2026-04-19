package com.jcrawleydev.gemsdrop.view.fragments.utils;

import android.content.res.Resources;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.widget.TextView;

import java.util.Arrays;


public class GraphicUtils {

    public static void assignGradient(TextView view, Resources resources, int... resIds){
        int[] colors = Arrays.stream(resIds).map(id -> resources.getColor(id, null)).toArray();
        float y1 = view.getMeasuredHeight();

        var shader = new LinearGradient(0, 0, 0, y1,
                colors,
                null,
                Shader.TileMode.CLAMP);
        view.getPaint().setShader(shader);
    }


    private static float[] createPositionsFor(int[] colors){
        if(colors.length == 1){
            return new float[]{1};
        }
        if(colors.length == 2){
            return new float[]{0,1};
        }
        var positions = new float[colors.length];
        float positionIncrement = 1f / (colors.length - 1);
        for(int i  = 0; i < colors.length; i++){
            positions[i] = (i * positionIncrement);
        }
        printPositions(positions);
        return positions;
    }


    private static void printPositions(float[] positions){
        var str = new StringBuilder();
        for(float pos : positions){
            str.append(" ");
            str.append(pos);
        }
        System.out.println("^^^ GraphicUtils: printPositions() : "+ str.toString());
    }
}
