package com.max.figure;

import android.graphics.Color;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Max on 4/10/2016.
 */
public class Divider {
    public  static List<Figure> divide(int width, int height, int size){
        if(size < 1){
            throw  new IllegalArgumentException();
        }
        List<Figure> figures = new ArrayList<>();
        int[][] row = new int[width][height];
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                row[i][j] = 0;
            }
        }

        Random rand = new Random();

        int figureID = 0;
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                List<Pair<Integer, Integer>> thisFigure = new ArrayList<>();
                List<Pair<Integer, Integer>> possibleSteps = new ArrayList<>();
                if(row[i][j] == 0){
                    figureID++;
                    possibleSteps.add(new Pair<Integer, Integer>(i,j));
                }


                int thisSize = 0;
                while (possibleSteps.size() > 0 && thisSize < size) {

                    Pair<Integer, Integer> nextStep = possibleSteps
                            .get(rand.nextInt((possibleSteps.size())));
                    possibleSteps.remove(nextStep);
                    row[nextStep.first][nextStep.second] = figureID;
                    thisFigure.add(nextStep);
                    thisSize++;
                    if (nextStep.second > 0 && row[nextStep.first][nextStep.second - 1] == 0) {
                        Pair<Integer, Integer> add = new Pair(nextStep.first, nextStep.second - 1);
                        if (!possibleSteps.contains(add))
                            possibleSteps.add(add);
                    }

                    if (nextStep.second < height - 1 && row[nextStep.first][nextStep.second + 1] == 0) {
                        Pair<Integer, Integer> add = new Pair(nextStep.first, nextStep.second + 1);
                        if (!possibleSteps.contains(add))
                            possibleSteps.add(add);
                    }

                    if (nextStep.first > 0 && row[nextStep.first - 1][nextStep.second] == 0) {
                        Pair<Integer, Integer> add = new Pair(nextStep.first - 1, nextStep.second);
                        if (!possibleSteps.contains(add))
                            possibleSteps.add(add);
                    }

                    if (nextStep.first < width - 1 && row[nextStep.first + 1][nextStep.second] == 0) {
                        Pair<Integer, Integer> add = new Pair(nextStep.first + 1, nextStep.second);
                        if (!possibleSteps.contains(add))
                            possibleSteps.add(add);
                    }
                }

                if(thisFigure.size() > 0) {
                    int maxs = 0, mins = height, maxf = 0, minf = width;


                    for (Pair<Integer, Integer> node : thisFigure) {
                        if (node.first > maxf)
                            maxf = node.first;
                        if (node.first < minf)
                            minf = node.first;
                        if (node.second > maxs)
                            maxs = node.second;
                        if (node.second < mins)
                            mins = node.second;
                    }

                    boolean[][] basement = new boolean[maxf - minf + 1][maxs - mins + 1];

                    for (int k = 0; k < basement.length; k++) {
                        for (int l = 0; l < basement[k].length; l++) {
                            basement[k][l] = false;
                        }
                    }
                    for (Pair<Integer, Integer> node : thisFigure) {
                        basement[node.first - minf][node.second - mins] = true;
                    }

                    figures.add(new Figure(basement, Color.argb(0xffffff,
                            rand.nextInt() / 2 + Integer.MAX_VALUE / 2,
                            rand.nextInt() / 2 + Integer.MAX_VALUE / 2,
                            rand.nextInt() / 2 + Integer.MAX_VALUE / 2)));
                }
                for(int[] r : row){
                    String s = "";
                    for(int val : r){
                        s += (val+" ");
                    }
                    Log.d("field",s + "\n");
                }
                Log.d("field","=============\n");
            }
        }
        for(int[] r : row){
            String s = "";
            for(int val : r){
                s += (val+" ");
            }
            Log.d("field",s + "\n");
        }
        return figures;
    }
}
