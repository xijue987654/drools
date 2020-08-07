package com.xijue.drools.service;


import com.xijue.drools.entity.TestRequest;
import com.xijue.drools.entity.TestResponse;
import com.xijue.drools.entity.biz.RatingRequest;
import com.xijue.drools.entity.biz.RatingResponse;

/**
 * Description： 业务 interface
 * Author: xijue98764
 * Date: Created in 2020/7/31 16:43
 * Version: 1.0
 */
public interface BusinessService {

    /**
     * 计算总的分数
     */
    TestResponse calSumScore(TestRequest request) throws Exception;

    /**
     * biz--计算总分数
     */
    RatingResponse calRatingSumScore(RatingRequest request) throws Exception;

    /**
     * 副本
     */
    RatingResponse calRatingSumScore2(RatingRequest request) throws Exception;
}
