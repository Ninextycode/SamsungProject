package com.max.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 3/20/2016.
 */
public class FieldView extends SurfaceView implements SurfaceHolder.Callback {
    public FieldView(Context context) {
        super(context);
        boolean[][] f_array = new boolean[][]{
                {false, false, true},
                {false, false, true},
                {false, true,  true},
        };
        figures.add(new Figure(f_array, Color.WHITE, 250, 250));
        f_array = new boolean[][]{
                {true,  true,  true},
                {false, false, true},
                {false, true,  true},
        };
        figures.add(new Figure(f_array, Color.WHITE, 250 - Figure.A, 250 - Figure.A));
        f_array = new boolean[][]{
                {false, true, true},
                {false, true, false},
                {false, true, true},
        };
        figures.add(new Figure(f_array, Color.WHITE, 250, 250 - Figure.A * 2));
        figures.get(0).setX(0);
        figures.get(1).setX(100);
        figures.get(2).setX(200);


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
            if(drawThread.getActiveFigure() != null && drawThread.getActiveFigure().matched())
                figures.remove(drawThread.getActiveFigure());
            drawThread.setFigure(null, 0, 0);
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
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getContext(), getHolder());
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        for(Drawable d:figures)
            drawThread.addDrawable(d);
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