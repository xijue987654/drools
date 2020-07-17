package com.xijue.drools.service;

import com.xijue.drools.entity.PointDomain;

/**
 * Author: xijue987654
 * Date: Created in 2020/7/10 15:28
 * Version: 1.0
 */
public interface PointRuleEngineService {

    /**
     *  积分规则
     */
    void executePointRule(PointDomain pointDomain);

}
