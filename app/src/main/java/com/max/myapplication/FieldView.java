package com.max.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
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
        fill();
        getHolder().addCallback(this);
    }

    public FieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        fill();
        getHolder().addCallback(this);
        // TODO Auto-generated constructor stub
    }

    public FieldView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        fill();
        getHolder().addCallback(this);
        // TODO Auto-generated constructor stub
    }




    private void fill(){
        figures.addAll(Divider.divide(Constants.figureW, Constants.figureH, Constants.figureN));
        boolean[][] background = new boolean[Constants.figureW][Constants.figureH];
        for(int i = 0; i < Constants.figureH; i++){
            for(int j = 0; j < Constants.figureW; j++){
                background[j][i] = true;
            }
        }

        this.background =  new Figure(background, Color.WHITE, xfield, yfield);

        background = new boolean[Constants.figureW][Constants.figureH];
        for(int i = 0; i < Constants.figureH; i++){
            for(int j = 0; j < Constants.figureW; j++){
                background[j][i] = false;
            }
        }
        marker = new Figure(background, Color.GREEN, xfield, yfield);
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
                f.setY(yfield + Constants.figureH * Figure.A + rand.nextInt(this.getHeight() - (yfield + Constants.figureH * Figure.A)));
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