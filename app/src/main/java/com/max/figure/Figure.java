package com.max.figure;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Pair;

/**
 * Created by Max on 3/20/2016.
 */
public class Figure extends Drawable implements  Cloneable{
    public  static  boolean[][] whereALiesOnB(Figure A, Figure B){
        B = new Figure(B.basement, Color.argb(0,0,0,0), B.getX()-Figure.A/2, B.getY()-Figure.A/2);
        boolean[][] where = B.getBasement();
        for(int i = 0; i < where.length; i++) {
            for (int j = 0; j < where[i].length; j++) {
                where[i][j] = false;
            }
        }

        boolean[][] baseA = A.getBasement();
        for(int i = 0; i < baseA.length; i++){
            for(int j = 0; j < baseA[i].length; j++){
                if(baseA[i][j]) {
                    Pair<Integer, Integer> point =
                            B
                                    .whereContains(A.getX() + Figure.A * i,
                                            A.getY() + Figure.A * j);
                    if (point != null) {
                        where[point.first][point.second] = true;
                    }
                }
            }
        }
        return where;
    }


    public static final int A = 60;
    private boolean[][] basement;
    public  boolean[][] getBasement(){
        boolean [][] myBase = new boolean[basement.length][];
        for(int i = 0; i < basement.length; i++)
            myBase[i] = basement[i].clone();

        return myBase;
    }

    @Override
    public Figure clone(){
        return new Figure(basement, this.color);
    }
    private int x, y;
    private int color;
    public  int getColor(){
        return color;
    }
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
    public Figure(boolean[][] basement, int color){
        this.color = color;
        this.basement = basement;
    }

    public Figure(boolean[][] basement, int color, int x, int y){
        this(basement, color);
        this.x =x;
        this.y = y;
    }
    @Override
    public void draw(Canvas canvas) {
        Bitmap image = Bitmap.createBitmap(basement.length * A, basement[0].length * A,
                Bitmap.Config.ARGB_4444 );
        //image.d
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        paint.setColor(color);
        for(int i = 0; i < basement.length; i++){
            for(int j = 0; j < basement[i].length; j++){
                if(basement[i][j])
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
        return new Rect(x,y,x+basement.length*A,y+basement[0].length*A);
    }

    public boolean contains(int x, int y){
        for(int i = 0; i < basement.length; i++){
            for(int j = 0; j < basement[i].length; j++) {
                if(basement[i][j]){
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

    public Pair<Integer, Integer> whereContains(int x, int y){
        for(int i = 0; i < basement.length; i++){
            for(int j = 0; j < basement[i].length; j++) {
                if(basement[i][j]){
                    if(new Rect(this.x+i*A,this.y+j*A,this.x+i*A +A,this.y+j*A+A)
                            .contains(x, y)){
                        Log.d("Hit", i + " " + j);
                        return new Pair<Integer, Integer>(i,j);
                    }

                }
            }
        }
        return null;
    }

    public void rotate() {
        for(int i = 0; i < basement.length; i++){
            for(int j = 0; j < basement[i].length/2; j++) {
                boolean temp =  basement[i][j];
                basement[i][j] = basement[i][ basement[i].length-1-j];
                basement[i][basement[i].length-1-j] = temp;
            }
        }
        boolean[][] newBasement = new boolean[basement[0].length][basement.length];
        for(int i = 0; i < basement.length; i++){
            for(int j = 0; j < basement[i].length; j++) {
                newBasement[j][i] = basement[i][j];
            }
        }
        basement = newBasement;
        return;
    }
}
