package com.xijue.drools.service;

import com.xijue.drools.entity.PointDomain;

/**
 * Author: mss13072
 * Date: Created in 2020/7/10 15:28
 * Version: 1.0
 */
public interface PointRuleEngineService {

    /**
     *  增加积分规则
     */
    void executeAddPointRule(PointDomain pointDomain);

    /**
     *  扣减积分规则
     */
    void executeSubPointRule(PointDomain pointDomain);

}
