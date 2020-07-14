package com.xijue.drools.service.impl;

import com.xijue.drools.config.DroolsAutoConfig;
import com.xijue.drools.service.ReloadDroolsRules;
import com.xijue.drools.utils.KieUtils;
import org.apache.commons.lang.StringUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.internal.io.ResourceFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Description： 重新load规则
 * Author: mss13072
 * Date: Created in 2020/7/13 19:15
 * Version: 1.0
 */
@Service
public class ReloadDroolsRulesImpl implements ReloadDroolsRules {

    @Override
    public void reload(String drlName) throws Exception {
        KieServices kieServices = KieUtils.getKieServices();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        loadFileRules(drlName, kfs);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }

        KieUtils.setKieContainer(kieServices.newKieContainer(KieUtils.getKieServices().getRepository().getDefaultReleaseId()));
        System.out.println("新规则重载成功");
    }

    private void loadFileRules(String drlName, KieFileSystem kfs) throws IOException {
        // 从classess/rules加载的规则
        for (Resource file : getRuleFiles(drlName)) {
            kfs.write(ResourceFactory.newClassPathResource(DroolsAutoConfig.RULES_PATH + file.getFilename(), "UTF-8"));
        }
    }

    private Resource[] getRuleFiles(String drlName) throws IOException {
        if (StringUtils.isEmpty(drlName)) {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            return resourcePatternResolver.getResources(DroolsAutoConfig.BASE_RULES_PATH + DroolsAutoConfig.RULES_PATH + "*.*");
        }
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources(DroolsAutoConfig.BASE_RULES_PATH + DroolsAutoConfig.RULES_PATH + drlName + ".*");
    }

}
