package com.max.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.max.figure.Divider;
import com.max.figure.Figure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Max on 3/20/2016.
 */
public class FieldView extends SurfaceView implements SurfaceHolder.Callback {
    Random rand = new Random();

    int xfield = Figure.A, yfield = Figure.A;
    public static int  figureH = 6, figureW = 5, figureN = 5;
    private Figure background;
    private Figure marker;
    public  Figure getBackground(){
        synchronized (background) {
            return background;
        }
    }

    public  Figure getMarker(){
        synchronized (marker) {
            return marker;
        }
    }
    public FieldView(Context context) {
        super(context);
        figures.addAll(Divider.divide(figureW, figureH, figureN));

        getHolder().addCallback(this);
    }


    @Override public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            for (Figure f : figures) {
                if (f.contains((int) event.getX(), (int) event.getY())) {
                    drawThread.setFigure(f,(int) event.getX(), (int) event.getY());
                    break;
                }
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            drawThread.setFigure(null ,0,0);
        }
        drawThread.setFigureLocation((int) event.getX(), (int) event.getY());
        return true ;
    }
    @Override public boolean onDragEvent(DragEvent  event) {
        drawThread.setFigureLocation((int) event.getX(), (int) event.getY());
        return true ;
    }
    private DrawThread drawThread;
    private List<Figure> figures = new ArrayList<>();
    public void  setFigures(List<Figure> f){
        synchronized (figures){
            figures = f;
        }
    }

    public void  setMarker(Figure m) {
        marker = m;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getContext(), getHolder(), this);
        drawThread.start();

        for(Figure f: figures){
            f.setX(rand.nextInt(this.getWidth() / 3*2));
            f.setY(yfield + figureH * Figure.A + rand.nextInt(this.getHeight() - (yfield + figureH * Figure.A)));
        }

        boolean[][] background = new boolean[figureW][figureH];
        for(int i = 0; i < figureH; i++){
            for(int j = 0; j < figureW; j++){
                background[j][i] = true;
            }
        }

        this.background =  new Figure(background, Color.WHITE, xfield, yfield);

        background = new boolean[figureW][figureH];
        for(int i = 0; i < figureH; i++){
            for(int j = 0; j < figureW; j++){
                background[j][i] = false;
            }
        }
        marker = new Figure(background, Color.GREEN, xfield, yfield);
        drawThread.addDrawable(this.background);
        drawThread.addDrawable(marker);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawThread.setFigures(figures);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}