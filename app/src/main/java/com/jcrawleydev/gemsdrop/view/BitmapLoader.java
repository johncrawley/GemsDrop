package com.jcrawleydev.gemsdrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.HashMap;
import java.util.Map;

public class BitmapLoader {

    private final Context context;
    private final int width;
    private final Map<Gem.Color, Bitmap> bitmaps;
    private Bitmap defaultGem;

    public BitmapLoader(Context context, int width){
        this.context = context;
        this.width = width;
        bitmaps = new HashMap<>();
        loadBitmaps();
    }


    private void loadBitmaps(){
        bitmaps.put(Gem.Color.BLUE, get(R.drawable.jewel_blue));
        bitmaps.put(Gem.Color.RED, get(R.drawable.jewel_red));
        bitmaps.put(Gem.Color.GREEN, get(R.drawable.jewel_green));
        bitmaps.put(Gem.Color.YELLOW, get(R.drawable.jewel_yellow));
        bitmaps.put(Gem.Color.PURPLE, get(R.drawable.jewel_purple));
        bitmaps.put(Gem.Color.GREY, get(R.drawable.jewel_grey));
        defaultGem = get(R.drawable.jewel_temp);
    }


    public Bitmap get(Gem.Color color){
        if(!bitmaps.containsKey(color)){
            return defaultGem;
        }
        return bitmaps.get(color);
    }


    public Bitmap get(int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId, opts);

        return Bitmap.createScaledBitmap(bm, width, width, true);
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
