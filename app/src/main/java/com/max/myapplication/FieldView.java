package com.max.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
    public static int  figureH = 7, figureW =5, figureN = 5;
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


        getHolder().addCallback(this);



    }


    @Override public boolean onTouchEvent(MotionEvent event) {
        Log.d("Event", "onTouchEvent " + event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                for (Figure f : figures) {
                    if (f.contains((int) event.getX(), (int) event.getY())) {
                        drawThread.setFigure(f, (int) event.getX(), (int) event.getY());
                        break;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                drawThread.setFigure(null, 0, 0);
                break;

            case MotionEvent.ACTION_MOVE:
                drawThread.setFigureLocation((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("UP", "ACTION_POINTER_UP " + event.getActionIndex());
                Figure activeFigure = drawThread.getActiveFigure();
                if (activeFigure != null) {
                    if(event.getActionIndex() == 0){
                        drawThread.setFigure(null, activeFigure.getX(), activeFigure.getY());
                    } else {
                        drawThread.getActiveFigure().rotate();
                    }
                }
                break;

            default:
                break;
        }
        //if (event.getPointerCount() == 1) {

        //}
        return true;
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



    boolean set = false;
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getContext(), getHolder(), this);
        drawThread.start();
        if(!set) {
            set = true;
            for (Figure f : figures) {
                f.setX(rand.nextInt(this.getWidth() / 3 * 2));
                f.setY(yfield + figureH * Figure.A + rand.nextInt(this.getHeight() - (yfield + figureH * Figure.A)));
            }
        }
        drawThread.addDrawable(this.background);
        drawThread.addDrawable(marker);

        drawThread.setFigures(figures);
        drawThread.fillMarker();
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