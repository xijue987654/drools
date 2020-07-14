package com.xijue.drools.service.impl;

import com.xijue.drools.entity.PointDomain;
import com.xijue.drools.service.PointRuleEngineService;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 规则接口实现类
 */
@Service
public class PointRuleEngineServiceImpl implements PointRuleEngineService {

    @Autowired
    private KieContainer kieContainer;

    @Override
    public void executeAddPointRule(PointDomain pointDomain) {
        KieSession kieSession = KieServices.Factory.get()
                .getKieClasspathContainer().newKieSession("ksession-rule");
//        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(pointDomain);
        int ruleNums = kieSession.fireAllRules();
        System.out.println("触发了" + ruleNums + "条规则!");
        kieSession.dispose();
    }

    @Override
    public void executeSubPointRule(PointDomain pointDomain) {
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(pointDomain);
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}