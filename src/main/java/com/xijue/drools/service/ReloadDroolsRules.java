package com.xijue.drools.service;

/**
 * Description： 重新load规则
 * Author: mss13072
 * Date: Created in 2020/7/13 19:16
 * Version: 1.0
 */
public interface ReloadDroolsRules {

    void reload(String drlName) throws Exception;
}
