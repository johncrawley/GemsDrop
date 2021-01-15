package com.jcrawleydev.gemsdrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapLoader {

    private Context context;

    public BitmapLoader(Context context){
        this.context = context;
    }

    public Bitmap get(int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        return BitmapFactory.decodeResource(context.getResources(), resId, opts);
    }
}
