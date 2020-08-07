package com.xijue.drools.utils;

import com.xijue.drools.entity.biz.Rating;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author: xijue98764
 * Date: Created in 2020/8/5 10:55
 * Version: 1.0
 */
public class BusinessUtils {

    /**
     * 查找下级，并计算结果
     */
    public static BigDecimal calSumScore(List<Rating> ratingList, String parentPointId) {
        BigDecimal sumScore = new BigDecimal("0");
        for (Rating rating : ratingList) {
            if (rating.getKeypointParentId().equals(parentPointId)) {
                if (rating.getScore() == null) {
                    rating.setScore(calSumScore(ratingList, rating.getKeypointId()));
                }
                sumScore = sumScore.add(rating.getWeight().multiply(rating.getScore()));
            }
        }

        return sumScore;
    }
}
