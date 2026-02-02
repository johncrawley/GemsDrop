package com.jcrawleydev.gemsdrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.game.gem.GemColor;

import java.util.HashMap;
import java.util.Map;


public class BitmapLoader {

    private final Context context;
    private final float width;
    private final Map<GemColor, Bitmap> bitmaps;
    private Bitmap defaultGem;


    public BitmapLoader(Context context, float width){
        this.context = context;
        this.width = width;
        bitmaps = new HashMap<>();
        loadBitmaps();
    }


    private void loadBitmaps(){
        bitmaps.put(GemColor.BLUE, get(R.drawable.jewel_blue));
        bitmaps.put(GemColor.RED, get(R.drawable.jewel_red));
        bitmaps.put(GemColor.GREEN, get(R.drawable.jewel_green));
        bitmaps.put(GemColor.YELLOW, get(R.drawable.jewel_yellow));
        bitmaps.put(GemColor.PURPLE, get(R.drawable.jewel_purple));
        bitmaps.put(GemColor.GREY, get(R.drawable.jewel_grey));
        defaultGem = get(R.drawable.jewel_temp);
    }


    public Bitmap get(GemColor color){
        if(!bitmaps.containsKey(color)){
            return defaultGem;
        }
        return bitmaps.get(color);
    }


    public Bitmap get(int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId, opts);

        return Bitmap.createScaledBitmap(bm, (int)width, (int)width, true);
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
