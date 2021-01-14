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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private int HALF_WIDTH = GEM_WIDTH /2;

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

    private int numberOfGems;

    public void setGemGroup(GemGroup gemGroup){
        this.gemGroup = gemGroup;
        numberOfGems = gemGroup.getGems().size();
        initXCoords();
    }

    private void init(Context context, AttributeSet attrs) {
        gemColorMap = new HashMap<>();
        link(Gem.Color.BLUE, R.drawable.jewel_blue);
        link(Gem.Color.YELLOW, R.drawable.jewel_yellow);
        link(Gem.Color.GREEN, R.drawable.jewel_green);
        link(Gem.Color.RED, R.drawable.jewel_red);

        initPaint();


    }

    private void initPaint(){

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

    }
    private List<Integer> xCoords;

    private void initXCoords(){
        xCoords = new ArrayList<>();
        int initialX = - HALF_WIDTH - (numberOfGems / 2 * GEM_WIDTH);
        for(int i =0; i< numberOfGems; i++){
            xCoords.add( initialX + i * GEM_WIDTH);
        }

    }

    private void link(Gem.Color color, int drawableId){
        gemColorMap.put(color, getBitmap(drawableId));
        Bitmap bm = getBitmap(drawableId);
        GEM_WIDTH = bm.getWidth();
        HALF_WIDTH = GEM_WIDTH /2;
        gemY = HALF_WIDTH;

    }
    private int angle = 0;

    public void updateAndDraw(){
       // angle = (angle + 15) % 360;
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

    private int width, height;

    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
        canvasTranslateX = width / 2;
    }

    int canvasTranslateX;
    int canvasTranslateY;
    int gemY;
    Paint paint;
    Canvas canvasBitmap;
    private Bitmap bitmapDraw() {

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);
        canvasBitmap = new Canvas(bitmap);
        canvasBitmap.save();
        canvasTranslateY =  gemGroup.getY();
        canvasBitmap.translate(canvasTranslateX, canvasTranslateY);
        canvasBitmap.rotate(angle);

        int numberOfGems = gemGroup.getGems().size();

        List<Gem> gems = gemGroup.getGems();

        for(int i=0; i< numberOfGems; i++){
            Gem gem = gems.get(i);
            Bitmap gemBitmap = gemColorMap.get(gem.getColor());
            canvasBitmap.drawBitmap(gemBitmap, xCoords.get(i), gemY, paint);
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