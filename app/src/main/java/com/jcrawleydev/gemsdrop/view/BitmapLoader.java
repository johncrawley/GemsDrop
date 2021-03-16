package com.jcrawleydev.gemsdrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;

public class BitmapLoader {

    private Context context;
    private int width;

    public BitmapLoader(Context context, int width){

        this.context = context;
        this.width = width;
    }



    public Bitmap get(int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId, opts);

        return Bitmap.createScaledBitmap(bm,width, width, true);
    }


/* Possible way to load vector drawables in the future

    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
*/
}
