package com.max.figure;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by Max on 3/20/2016.
 */
public class Figure extends Drawable {
    public static final int A = 40;
    private boolean[][] figure;
    private int x, y;
    private int color;
    public int getX() {
        return x;
    }
    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public Figure(boolean[][] figure, int color, int truex, int truey){
        this.color = color;
        this.figure = figure;
    }
    @Override
    public void draw(Canvas canvas) {
        Bitmap image = Bitmap.createBitmap(figure.length * A, figure[0].length * A,
                Bitmap.Config.ARGB_4444 );
        //image.d
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        paint.setColor(color);
        for(int i = 0; i < figure.length; i++){
            for(int j = 0; j < figure[i].length; j++){
                if(figure[i][j])
                    canvas.drawRect(new Rect(x+i*A,y+j*A,x+i*A +A,y+j*A+A), paint);
            }
        }


    }

    @Override
    public void setAlpha(int alpha) {
        //TODO
    }
    ColorFilter colorFilter;
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        this.colorFilter = colorFilter;
    }

    @Override
    public int getOpacity() {
        //TODO
        return 0;
    }

    @Override
    public Rect getDirtyBounds(){
        return new Rect(x,y,x+figure.length*A,y+figure[0].length*A);
    }

    public boolean contains(int x, int y){
        for(int i = 0; i < figure.length; i++){
            for(int j = 0; j < figure[i].length; j++) {
                if(figure[i][j]){
                    if(new Rect(this.x+i*A,this.y+j*A,this.x+i*A +A,this.y+j*A+A)
                            .contains(x, y)){
                        Log.d("Hit", i + " " + j);
                        return true;
                    }

                }
            }
        }
        return false;
    }
}
