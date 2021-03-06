package com.mkyong.web.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Dmitry on 12.02.2017.
 */
@Service
public class WaterFillerMockImpl implements WaterFiller {

    /**
     * this implementation do not really compute water level,
     * it just put fake data in return
     * @param bricksList
     * @return
     */
    @Override
    public List<Integer> Compute(List<Integer> bricksList) {

        List<Integer> waterList = Arrays.asList(0, 0, 0, 0, 0, 5, 5, 0, 8, 8,
                0, 10, 10, 10, 10, 10, 10, 10, 0, 0);


        return waterList;
    }
}
