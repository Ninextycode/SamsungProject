package com.max.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;

import com.max.figure.Figure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Max on 3/20/2016.
 */
public class DrawThread extends Thread {
    private Object syncRoot = new Object();
    private SurfaceHolder surfaceHolder;
    private Figure activeFigure = null;
    private  FieldView myView;
    private volatile boolean running = true;
    //флаг для остановки потока
    public DrawThread(Context context, SurfaceHolder surfaceHolder, FieldView f) {
        this.surfaceHolder = surfaceHolder;
        myView = f;
    }
    public void requestStop() {
        running = false;
    }
    List<Drawable> toDraw = new ArrayList<>();
    List<Figure> figures = new ArrayList<>();
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
                    synchronized (toDraw) {
                        for (Drawable d : toDraw)
                            d.draw(canvas);
                    }
                    synchronized (figures) {
                        for (Drawable d : figures) {
                            d.draw(canvas);
                        }
                    }
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    private int activeXoffset = 0, activeYoffset = 0;

    public void setFigure(Figure f, int firstx, int firsty) {

        if (f == null && activeFigure != null) {
            Figure oldMarker = myView.getMarker();
            Figure background = myView.getBackground();
            boolean[][] newBasement = new boolean[myView.figureW][myView.figureH];
            for (int i = 0; i < newBasement.length; i++) {
                for (int j = 0; j < newBasement[i].length; j++) {
                    newBasement[i][j] = false;
                }
            }

            synchronized (figures) {
                for (Figure fig : figures) {
                    boolean[][] match = Figure.whereALiesOnB(fig, background);
                    for (int i = 0; i < newBasement.length; i++) {
                        for (int j = 0; j < newBasement[i].length; j++) {
                            newBasement[i][j] = newBasement[i][j] || match[i][j];
                        }
                    }
                }
            }

            if (!Arrays.deepEquals(newBasement, oldMarker.getBasement())) {
                for (int i = 0; i < newBasement.length; i++) {
                    for (int j = 0; j < newBasement[i].length; j++) {
                        newBasement[i][j] = newBasement[i][j] || oldMarker.getBasement()[i][j];
                    }
                }
                synchronized (toDraw) {
                    toDraw.remove(oldMarker);
                    toDraw.add(new Figure(newBasement, oldMarker.getColor(), oldMarker.getX(), oldMarker.getY()));
                }
            }
        }
        this.activeFigure = f;

        if (f != null) {
            synchronized (figures) {
                figures.remove(f);
                figures.add(f);
                myView.setFigures(figures);
            }
            activeXoffset = f.getX() - firstx;
            activeYoffset = f.getY() - firsty;
        }
    }

    public void addDrawable(Drawable d){
        if(!toDraw.contains(d))
            toDraw.add(d);
    }
    public void addFigure(Figure f){
        if(!figures.contains(f))
            figures.add(f);
    }

    public void setFigures(List<Figure> fs){
            figures = fs;
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