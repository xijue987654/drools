package com.xijue.drools.service;

import com.xijue.drools.entity.RuleResult;

import java.util.List;
import java.util.Map;

/**
 * Author: xijue98764
 * Date: Created in 2020/7/15 15:50
 * Version: 1.0
 */
public interface CommonService {

    /**
     * build所有规则
     */
    void buildAllRules();

    /**
     * build规则
     */
    void buildRules(String fileName, String code);

    /**
     * 删除规则
     */
    void deleteRules(String fileName);

    /**
     * 触发规则
     */
    List<RuleResult> fireRules(Map<String, Object> eventParam, String packageName);


}
