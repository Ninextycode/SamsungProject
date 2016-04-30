package com.max.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;

import com.max.figure.Figure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 3/20/2016.
 */
public class DrawThread extends Thread {
    private Object syncRoot = new Object();
    private SurfaceHolder surfaceHolder;
    private Figure activeFigure = null;
    private  FieldView myView;
    Figure oldMarker;
    private volatile boolean running = true;
    //флаг для остановки потока
    public DrawThread(Context context, SurfaceHolder surfaceHolder, FieldView f) {
        this.surfaceHolder = surfaceHolder;
        myView = f;
        oldMarker = myView.getMarker();

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
            fillMarker();


            boolean matched = true;
            for(boolean[] b: oldMarker.getBasement()){
                for(boolean b1:b){
                    matched = matched && b1;
                }
            }

            if(matched){
                synchronized (figures) {
                    synchronized (toDraw) {
                        for (Figure fig : figures) {
                            fig.setX(((int) Math.round(((double) (fig.getX() + myView.xfield)) / Figure.A)-1) * Figure.A);
                            fig.setY(((int) Math.round(((double) (fig.getY() + myView.yfield)) / Figure.A)-1) * Figure.A);
                            toDraw.add(fig);
                        }
                        figures = new ArrayList<>();
                        myView.setFigures(figures);
                    }
                }
            }
         //   }
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

    public void fillMarker() {
        Figure background = myView.getBackground();
        boolean[][] newBasement = new boolean[Constants.figureW][Constants.figureH];
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

        //  if (!Arrays.deepEquals(newBasement, oldMarker.getBasement())) {
        synchronized (toDraw) {
            toDraw.remove(oldMarker);
            oldMarker = new Figure(newBasement, oldMarker.getColor(), oldMarker.getX(), oldMarker.getY());
            toDraw.add(oldMarker);
        }
    }
}