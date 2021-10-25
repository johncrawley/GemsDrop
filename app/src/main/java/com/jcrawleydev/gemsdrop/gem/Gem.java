package com.jcrawleydev.gemsdrop.gem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.view.DrawItem;
import com.jcrawleydev.gemsdrop.view.item.DrawableItem;

import androidx.annotation.NonNull;

public class Gem implements DrawItem, DrawableItem, Cloneable {

    public enum Color {
        BLUE("B", R.drawable.jewel_blue),
        RED("R", R.drawable.jewel_red),
        GREEN("G", R.drawable.jewel_green),
        YELLOW("Y", R.drawable.jewel_yellow),
        PURPLE("P", R.drawable.jewel_purple);

        Color(String str, int resourceId){
            this.str = str;
            this.resourceId = resourceId;
        }
        public String str;
        public int resourceId;

        @NonNull
        public String toString(){
            return str;
        }
    }


    protected Color color;
    private boolean deletionCandidateFlag = false;
    private boolean markedForDeletion = false;
    private int x,y;
    private Bitmap bitmap;
    private boolean visible;
    private final Paint paint;


    public Gem(Color color){
        this.color = color;
        this.visible = true;
        paint = new Paint();
    }


    public Gem(Color color, Paint paint){
        this.color = color;
        this.visible = true;
        this.paint = paint;
    }


    public Color getColor(){
        return color;
    }

    @Override
    public int getX(){
        return x;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }


    @Override
    public Bitmap getBitmap(){
        return this.bitmap;
    }


    @Override
    public int getY(){
        return y;
    }


    public void setX(int x){
        this.x = x;
    }


    public void setXY(int x, int y){
        setX(x);
        setY(y);
    }


    public void setY(int y){
        this.y = y;
    }


    public void incY(int value){this.y += value;}


    public void setMarkedForDeletion(){
        if(deletionCandidateFlag){
            markedForDeletion = true;
        }
    }

    public boolean isDeletionCandidateFlagSet(){
        return deletionCandidateFlag;
    }

    public boolean isMarkedForDeletion(){
        return this.markedForDeletion;
    }


    public boolean isNotSameColorAs(Gem otherGem){
        return this instanceof NullGem || this.getColor() != otherGem.getColor();
    }


    public void setDeleteCandidateFlag(){
        deletionCandidateFlag = true;
    }


    public void resetDeleteCandidateFlag(){
        deletionCandidateFlag = false;
    }


    @Override
    public boolean isVisible(){
        return this.visible;
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        if(!isVisible()){
            return;
        }
        canvas.drawBitmap(bitmap, x, y, this.paint);
    }


    public void setInvisible(){
        this.visible = false;
    }

    public void setVisible(){
        this.visible = true;
    }


    @Override
    public Gem clone(){
        Gem gem = new Gem(Color.BLUE);
        try{
            gem = (Gem) super.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return gem;
    }
}
