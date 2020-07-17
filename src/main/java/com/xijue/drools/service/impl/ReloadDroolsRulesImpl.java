package com.xijue.drools.service.impl;

import com.xijue.drools.config.DroolsAutoConfig;
import com.xijue.drools.entity.RequestDto;
import com.xijue.drools.entity.Rule;
import com.xijue.drools.mapper.RuleMapper;
import com.xijue.drools.service.ReloadDroolsRules;
import com.xijue.drools.utils.KieUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.List;

/**
 * Description： 重新load规则
 * Author: xijue987654
 * Date: Created in 2020/7/13 19:15
 * Version: 1.0
 */
@Slf4j
@Service
public class ReloadDroolsRulesImpl implements ReloadDroolsRules {

    @Autowired
    private RuleMapper ruleMapper;

    @Override
    public void reload(RequestDto request) throws Exception {
        KieServices kieServices = KieUtils.getKieServices();
        KieFileSystem kfs = kieServices.newKieFileSystem();
//        loadFileRules(request.getDrlName(), kfs);
        loadDBRules(kfs);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }

        KieUtils.setKieContainer(kieServices.newKieContainer(KieUtils.getKieServices().getRepository().getDefaultReleaseId()));
        KieSession kieSession = KieUtils.getKieSession();
        kieSession.fireAllRules();
        System.out.println("新规则重载成功");
    }

    @Override
    public void testReload() throws Exception {
        Example example = new Example(Rule.class);
        example.createCriteria().andEqualTo("isDeleted", "0")
                .andEqualTo("packageName", "droolRule");
        List<Rule> rules = ruleMapper.selectByExample(example);
        KieHelper helper = new KieHelper();
        for (Rule rule : rules) {
            helper.addContent(rule.getRuleDetail(), ResourceType.DRL);
        }
        KieContainer kieContainer = helper.getKieContainer();

        KieUtils.setKieContainer(kieContainer);
        KieSession ksession = KieUtils.getKieSession();
        ksession.fireAllRules();
        log.info("testReload 总共加载了{}条规则记录!", rules.size());
    }

    /**
     * 从数据库中加载规则, 待实现
     */
    private void loadDBRules(KieFileSystem kfs) {
        Example example = new Example(Rule.class);
        example.createCriteria().andEqualTo("isDeleted", "0")
                .andEqualTo("packageName", "droolRule");
        List<Rule> rules = ruleMapper.selectByExample(example);
        for (Rule rule : rules) {
            // 从数据库加载的规则
            kfs.write("src/main/resources/" + DroolsAutoConfig.RULES_PATH + rule.getRuleName() + ".drl", rule.getRuleDetail().getBytes());
        }
    }


    private void loadFileRules(String drlName, KieFileSystem kfs) throws IOException {
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
