package com.max.figure;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
        List<Pair<Integer, Integer>> possibleSteps = new ArrayList<>();

        int figureID = 1;
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(row[i][j] == 0)
                    possibleSteps.add(new Pair<Integer, Integer>(i,j));

                int thisSize = 0;
                while (possibleSteps.size() > 0 && thisSize < size) {
                    Pair<Integer, Integer> nextStep = possibleSteps
                            .get(ThreadLocalRandom.current().nextInt(0, possibleSteps.size()));
                    row[nextStep.first][nextStep.second] = figureID;
                    thisSize ++;
                    if(nextStep.first > 0) {
                        if(nextStep.second > 0 && row[nextStep.first - 1][nextStep.second -1 ] == 0) {
                            Pair<Integer, Integer> add = new Pair(nextStep.first - 1, nextStep.second- 1);
                            if (!possibleSteps.contains(add))
                                possibleSteps.add(add);
                        }
                        if(nextStep.second < height-1 && row[nextStep.first - 1][nextStep.second + 1] == 0) {
                            Pair<Integer, Integer> add = new Pair(nextStep.first - 1, nextStep.second + 1);
                            if (!possibleSteps.contains(add))
                                possibleSteps.add(add);
                        }
                    }
                    if(nextStep.first < width - 1) {
                        if(nextStep.second > 0 && row[nextStep.first + 1][nextStep.second - 1] == 0) {
                            Pair<Integer, Integer> add = new Pair(nextStep.first + 1, nextStep.second - 1);
                            if (!possibleSteps.contains(add))
                                possibleSteps.add(add);
                        }
                        if(nextStep.second < height-1 && row[nextStep.first + 1][nextStep.second + 1] == 0) {
                            Pair<Integer, Integer> add = new Pair(nextStep.first + 1, nextStep.second + 1);
                            if (!possibleSteps.contains(add))
                                possibleSteps.add(add);
                        }
                    }
                }
                figureID++;
            }
        }
        for(int[] r : row){
            for(int val : r){
                Log.d("field",val+" ");
            }
            Log.d("field","\n");
        }
        return figures;
    }
}
