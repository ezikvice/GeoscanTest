package com.mkyong.web.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Dmitry on 12.02.2017.
 */
@Service
public class WaterFillerImpl implements WaterFiller {

    // array of bricks heights
    private Integer[] wall;

    // array of water heights
    private Integer[] waterArr;


    /**
     * computing water heights in the given wall
     * @param bricksList
     * @return list of water heights
     */
    @Override
    public List<Integer> Compute(List<Integer> bricksList) {

        wall = bricksList.toArray(new Integer[bricksList.size()]);
        waterArr = new Integer[wall.length];

        Integer wallEnd = wall.length;

        Integer curPos = 0;

        //////// MAIN LOOP //////////
        while(curPos < wallEnd){
            // looking for the nearest zero height to the right hand
            Integer nextZero = lookingNextZero(curPos);
            if (nextZero - curPos > 2) {
                // if there is 3 or more squares, we can try to fill them with water
                fillPart(curPos, nextZero);
                curPos = nextZero;

            }else{ // there is no water here. Filling with zeroes and go to the next loop
                fillWithWater(curPos, nextZero, 0);
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
                if(currentLeft >= end){
                    return end;
                }
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

    /**
     * finds empty position
     * @param start
     * @param end
     * @return first left position from start that not in the wall
     */
    private Integer findEmptyLeft(int start, int end){
        for(int curPos = start+1; curPos < end; curPos++){
            if(wall[curPos] < wall[start]){
                return curPos;
            }
        }
        return end;
    }

    /**
     * filling the part of aquarium by water (from leftPos to rightPos)
     *
     * @param leftPos start
     * @param rightPos end
     * @param height
     */
    private void fillWithWater(Integer leftPos, Integer rightPos, Integer height) {
        for (int i = leftPos; i < rightPos; i++) {
            if (height > wall[i]) {
                waterArr[i] = height;
            } else waterArr[i] = 0;
        }
    }

    /**
     * filling the area between places with zero height
     * places with zero height must be found beforehand
     * @param start
     * @param end
     */
    private void fillPart(Integer start, Integer end) {

        Integer leftWall = lookingForLeftWall(start, end);

        // squares before the left wall must be filled with zeroes
        fillWithWater(start, leftWall, 0);

        if (leftWall < end) {

            Integer rightWall = lookingForRightWall(leftWall, end);

            // if the right wall is not in the end
            // then filling with water
            if (rightWall < end) {
                Integer height = Integer.min(wall[leftWall],wall[rightWall]);
                fillWithWater(leftWall, rightWall, height);

                // and then trying to fill the part from right wall till the end
                fillPart(rightWall, end);
            }
            if (rightWall == end){
                fillWithWater(leftWall, rightWall, 0);
            }
        } else // if leftWall >= end..
            {
                // it means that we coudn`t find left wall till the end of wall
                // so, filling the remaining part with zeroes
                fillWithWater(start, end, 0);
                return;
        }

    }


}
