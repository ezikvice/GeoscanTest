package com.mkyong.web.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Dmitry on 12.02.2017.
 */
@Service
public class WaterFillerImpl implements WaterFiller {

    // array of bricks heights
    private Integer[] wall;

    private Integer[] waterArr;


    @Override
    public List<Integer> Compute(List<Integer> bricksList) {

        wall = bricksList.toArray(new Integer[bricksList.size()]);
        waterArr = new Integer[wall.length];

        Integer wallEnd = wall.length;

        Integer curPos = 0;

        //////// MAIN LOOP //////////
        while(curPos < wallEnd){
            // ищем ближайший ноль справа
            Integer nextZero = lookingNextZero(curPos);
            if (nextZero - curPos > 2) {
                // есть остров, пытаемся заполнить водой
                fillPart(curPos, nextZero);
                curPos = nextZero;

            }else{ // ставим нули, сдвигаем курсор и переходим на следующую итерацию цикла
                coverWithWater(curPos, nextZero, 0);
                curPos = nextZero;
            }

        }

        List<Integer> waterList = Arrays.asList(waterArr);
        return waterList;
    }

    /**
     * looking for the next zero in array
     * (from cursor to the first zero or end of the wall)
     *
     * @param cursor
     * @return next zero in array
     */
    private Integer lookingNextZero(Integer cursor) {

        int wallEnd = wall.length;

        if(cursor >= wallEnd){
            return wallEnd;
        }

        for (int curPos = cursor+1; curPos < wallEnd; curPos++) {
            if (wall[curPos] == 0) {
                return curPos;
            }
        }

        return wallEnd;
    }


    /**
     * looking for the next left wall in the bricks
     * left wall - is when the height of the next brick
     * is less than the height of the current one
     *
     * @param start
     * @return
     */
    private Integer lookingForLeftWall(Integer start, Integer end) {

        if(start+1>=end){
            return end;
        }

        for (int current = start; current < end; current++) {
            if (wall[current] > wall[current + 1]) {
                return current;
            }
        }
        return end;
    }

    /**
     * looking for the next right wall in the bricks until next zero position
     * right wall is when the height of the next brick >= current height
     *
     * @param start
     * @return
     */
    private Integer lookingForRightWall(Integer start, Integer end) {

        int curHeight = wall[start];
        int currentLeft = start;

        while(currentLeft < end){
            while(curHeight>1){
                int cursor = currentLeft;
                if(wall[currentLeft]>=curHeight){
                    cursor = findEmptyLeft(currentLeft, end);
                    currentLeft = cursor;
                }
                while(cursor < end){
                    if(wall[cursor]>=curHeight){
                        return cursor;
                    }
                    cursor++;
                }
                curHeight--;
            }
            return end;
//            currentLeft++;
        }
        return end;
    }

    Integer findEmptyLeft(int start, int end){
        for(int curPos = start+1; curPos < end; curPos++){
            if(wall[curPos] < wall[start]){
                return curPos;
            }
        }
        return end;
    }

    /**
     * filling the part of aquarium by water (from left to right limits)
     *
     * @param leftPos
     * @param rightPos
     * @param height
     */
    private void coverWithWater(Integer leftPos, Integer rightPos, Integer height) {
        for (int i = leftPos; i < rightPos; i++) {
            if (height > wall[i]) {
                waterArr[i] = height;
            } else waterArr[i] = 0;
        }
    }

    /**
     * filling the area between places with zero height
     * @param start
     * @param end
     */
    // заполняем область между нулями
    private void fillPart(Integer start, Integer end) {

        // находим левую стенку
        Integer leftWall = lookingForLeftWall(start, end);

        // заполняем нулями от старта до левой стенки
        coverWithWater(start, leftWall, 0);

        // если мы еще не в конце, то ищем правую стенку
        if (leftWall < end) {

            Integer rightWall = lookingForRightWall(leftWall, end);

            // если правая стенка не в конце (а в конце нулевая высота),
            // то заполняем водой от левой стенки до правой
            if (rightWall < end) {
                Integer height = Integer.min(wall[leftWall],wall[rightWall]);
                coverWithWater(leftWall, rightWall, height);

                // дальше правую стенку считаем началом
                // и вызываем fillPart для части от нового начала до конца
                fillPart(rightWall, end);
            }
            // а если правая стенка в конце, то
            if (rightWall == end){
                coverWithWater(leftWall, rightWall, 0);
            }
        } else // а если leftWall >= end..
            {
                // то левую стенку не нашли и заполняем оставшееся нулями
                coverWithWater(start, end, 0);
                return;
        }

    }


}
