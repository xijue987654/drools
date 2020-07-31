package com.xijue.drools.service.impl;

import com.xijue.drools.config.CustomAgendaFilter;
import com.xijue.drools.entity.PointDomain;
import com.xijue.drools.service.PointRuleEngineService;
import com.xijue.drools.utils.KieUtils;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

/**
 * 规则接口实现类
 */
@Service
public class PointRuleEngineServiceImpl implements PointRuleEngineService {

    @Override
    public void executePointRule(PointDomain pointDomain) {
        // 从src/main/resource/droolRule下加载规则;
//        KieSession kieSession = KieServices.Factory.get()
//                .getKieClasspathContainer().newKieSession("ksession-rule");

        KieUtils.setKieContainer(KieServices.Factory.get()
                .newKieContainer(KieServices.Factory.get().getRepository().getDefaultReleaseId()));
        KieSession kieSession = KieUtils.getKieSession();
        kieSession.insert(pointDomain);

        int ruleNums = kieSession.fireAllRules(new CustomAgendaFilter("droolRule"));
        System.out.println("触发了" + ruleNums + "条规则!");
        kieSession.dispose();
    }

}