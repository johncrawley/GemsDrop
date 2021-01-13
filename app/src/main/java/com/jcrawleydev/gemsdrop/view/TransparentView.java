package com.jcrawleydev.gemsdrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class TransparentView extends View {

    private final static String TAG = "TransparentCardView";

    private int cardTopMargin = 0;
    private int cardWidth = 200;
    private int cardHeight = 200;
    private int stroke = 10;
    private int mainWidth = 0;
    private int mainHeight = 0;
    private int GEM_WIDTH = 50;

    private Context context;
    private Bitmap yellowGem, greenGem, blueGem, redGem;
    private GemGroup gemGroup;
    private Map<Gem.Color, Bitmap> gemColorMap;

    private boolean isDrawn = false;  //Flag for checking whether view is drawn or not.

    private OnLayoutListener layoutListener;


    public TransparentView(Context context) {
        super(context);
        this.context = context;
    }

    public TransparentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public TransparentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    public void setGemGroup(GemGroup gemGroup){
        this.gemGroup = gemGroup;
    }

    private void init(Context context, AttributeSet attrs) {
        gemColorMap = new HashMap<>();
        link(Gem.Color.BLUE, R.drawable.jewel_blue);
        link(Gem.Color.YELLOW, R.drawable.jewel_yellow);
        link(Gem.Color.GREEN, R.drawable.jewel_green);
        link(Gem.Color.RED, R.drawable.jewel_red);
    }

    private void link(Gem.Color color, int drawableId){
        gemColorMap.put(color, getBitmap(drawableId));
    }
    private int angle = 0;

    public void updateAndDraw(){
        angle = (angle + 15) % 360;
    }

    /**
     * Calculates required parameters for TransparentCardView creation
     */
    private void defaultAttributes() {
        mainWidth = getWidth();
        mainHeight = getHeight();
        cardTopMargin = mainHeight / 10;
        cardWidth = mainWidth - (mainWidth / 5);
        cardHeight = mainHeight / 2;
    }


    //@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isDrawn)
            defaultAttributes();
        isDrawn = true;
        Bitmap bitmap = bitmapDraw();
        canvas.drawBitmap(bitmap, getWidth() / 2f - cardWidth / 2f, cardTopMargin, null);
    }


    private Bitmap getBitmap(int resId) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        return BitmapFactory.decodeResource(context.getResources(), resId, opts);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        defaultAttributes();

        if (this.layoutListener != null && !isDrawn)
            this.layoutListener.onLayout();

        isDrawn = true;
    }


    private Bitmap bitmapDraw() {

        Bitmap bitmap = Bitmap.createBitmap(cardWidth, cardHeight, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);

        Canvas canvasBitmap = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        canvasBitmap.save();
        int jewelX = cardWidth / 2;
        int jewelY =  gemGroup.getY();// (int)(cardWidth / 2f);
        canvasBitmap.translate(jewelX, jewelY);
        canvasBitmap.rotate(angle);
        int middleOffsetX = GEM_WIDTH / 2;
        int middleOffsetY = GEM_WIDTH / 2;
        int firstOffsetX =  GEM_WIDTH + middleOffsetX;
        int firstOffsetY = middleOffsetY;

        int i=0;
        /* 6 gems
            0 1 2 3 4 5
            6/2 = 3

         */

        int numberOfGems = gemGroup.getGems().size();
        boolean evenNumberOfGems = numberOfGems  % 2 == 0;
        for(Gem gem: gemGroup.getGems()){
            if(evenNumberOfGems){
               // if(i < )
            }
            i++;
            Bitmap gemBitmap = gemColorMap.get(gem.getColor());
            int x = (GEM_WIDTH * 3) * i;
            int y= GEM_WIDTH /2;
            canvasBitmap.drawBitmap(gemBitmap, x,y, paint);
        }
        canvasBitmap.restore();
        return bitmap;
    }


    /**
     * Listener for notifying view layout is done.
     */
    public interface OnLayoutListener {
        void onLayout();
    }
}