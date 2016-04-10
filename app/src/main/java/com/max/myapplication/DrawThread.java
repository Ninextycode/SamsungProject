package com.max.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 3/20/2016.
 */
public class DrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private Figure activeFigure = null;
    private volatile boolean running = true;
    //флаг для остановки потока
    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }
    public void requestStop() {
        running = false;
    }
    List<Drawable> toDraw = new ArrayList<>();
    @Override
    public void run() {
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    Paint p = new Paint();
                    p.setColor(Color.BLACK);
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p);
                    p.setColor(Color.RED);
                    canvas.drawCircle(250, 250, 10, p);
                    for(Drawable d : toDraw)
                        d.draw(canvas);
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    private int activeXoffset = 0, activeYoffset = 0;
    public void setFigure(Figure f, int firstx, int firsty) {
        if (f == null && activeFigure != null) {
            if(activeFigure.matched()){
                activeFigure.moveToTrue();
            }
        }
        this.activeFigure = f;
        if(f != null) {
            activeXoffset = f.getX() - firstx;
            activeYoffset = f.getY() - firsty;
        }

    }
    public void addDrawable(Drawable d){
        if(!toDraw.contains(d))
            toDraw.add(d);
    }

    public  void removeDrawable(Drawable d){
        toDraw.remove(d);
    }
    public void setFigureLocation(int x, int y){
        if(activeFigure != null){
            activeFigure.setX(x + activeXoffset);
            activeFigure.setY(y + activeYoffset);
        }
    }
    public Figure getActiveFigure(){
        return  activeFigure;
    }
}