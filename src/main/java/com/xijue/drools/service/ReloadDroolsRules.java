package com.xijue.drools.service;

import com.xijue.drools.entity.RequestDto;

/**
 * Description： 重新load规则
 * Author: xijue987654
 * Date: Created in 2020/7/13 19:16
 * Version: 1.0
 */
public interface ReloadDroolsRules {

    void reload(RequestDto request) throws Exception;

    void testReload() throws Exception;
}
