package com.xijue.drools.service.impl;

import com.xijue.drools.entity.RuleResult;
import com.xijue.drools.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Author: xijue98764
 * Date: Created in 2020/7/15 15:51
 * Version: 1.0
 */
@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

    @Value("${drl.path}")
    private String drlPath;

    /**
     * 加载规则目录
     */
    private final String BUILD_PATH = "src/main/resources/";

    /**
     * Kiebase
     */
    private final String KEI_BASE = "kieBase";

    /**
     * Kiebase
     */
    private final String SESSION = "session_";

    private final KieServices kieServices;

    private final KieFileSystem kieFileSystem;

    private final KieModuleModel kieModuleModel;

    private final KieRepository kieRepository;

    @Autowired
    public CommonServiceImpl(KieServices kieServices,
                             KieFileSystem kieFileSystem,
                             KieModuleModel kieModuleModel,
                             KieRepository kieRepository) {
        this.kieServices = kieServices;
        this.kieFileSystem = kieFileSystem;
        this.kieModuleModel = kieModuleModel;
        this.kieRepository = kieRepository;
    }

    @Override
    public void buildAllRules() {
        log.info("build rule start");
        File filePath = new File(drlPath);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        Iterator<File> files = FileUtils.iterateFiles(filePath, null, false);
        while (files.hasNext()) {
            File file = files.next();
            if (file.getName().endsWith(".drl")) {
                log.info("build file with filename {}", file.getName());
                String packageName = file.getName().split("\\.")[0];
                KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel(KEI_BASE + packageName);
                kieBaseModel.addPackage(packageName);
                kieBaseModel.newKieSessionModel(SESSION + packageName);
                try {
                    kieFileSystem.write(BUILD_PATH + "/" + packageName + "/" + file.getName(), FileUtils.readFileToByteArray(file));
                } catch (IOException e) {
                    log.error("read file error with file name {}", file.getName());
                }
            }
        }
        //这边主要目的是定义keyModel让不同的package对应不同的session，这样可以只触发某个session下的规则
        String xml = kieModuleModel.toXML();
        kieFileSystem.writeKModuleXML(xml);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        //装载至库
        kieBuilder.buildAll();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            log.error("build rules error {}", kieBuilder.getResults().toString());
        }
        log.info("build rule end");
    }

    @Override
    public void buildRules(String fileName, String code) {
        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(code)) {
            return;
        }
        log.info("build file with filename {}", fileName);
        KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel(KEI_BASE + fileName);
        kieBaseModel.addPackage(fileName);
        kieBaseModel.newKieSessionModel(SESSION + fileName);
        kieFileSystem.write(BUILD_PATH + "/" + fileName + "/" + fileName + ".drl", code);
        //这边主要目的是定义keyModel让不同的package对应不同的session，这样可以只触发某个session下的规则
        String xml = kieModuleModel.toXML();
        kieFileSystem.writeKModuleXML(xml);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        //装载至库
        kieBuilder.buildAll();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            log.error("build rules error {}", kieBuilder.getResults().toString());
        }
    }

    @Override
    public void deleteRules(String fileName) {
        kieModuleModel.removeKieBaseModel(KEI_BASE + fileName);
        String xml = kieModuleModel.toXML();
        kieFileSystem.writeKModuleXML(xml);
        kieFileSystem.delete(BUILD_PATH + "/" + fileName + "/" + fileName + ".drl");
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        //装载至库
        kieBuilder.buildAll();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            log.error("build rules error {}", kieBuilder.getResults().toString());
        }
    }

    @Override
    public List<RuleResult> fireRules(Map<String, Object> eventParam, String packageName) {
        log.info("fire rule start with packageName {}  param {} ", packageName, eventParam);
        KieContainer kContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
        KieSession kSession = kContainer.newKieSession(SESSION + packageName);
        kSession.insert(getRuleParam(eventParam, packageName));
        kSession.fireAllRules();
        Collection c = kSession.getObjects(new ObjectFilter() {
            public boolean accept(Object object) {
                if (object instanceof RuleResult) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        kSession.dispose();
        return new ArrayList<RuleResult>(c);
    }

    /**
     * 生成规则入参
     */
    private Map<String, Object> getRuleParam(Map<String, Object> eventParam, String packageName) {
        Map<String, Object> ruleParam = new HashMap<>();
        eventParam.putAll(eventParam);
        eventParam.put("", packageName);
        return ruleParam;
    }

}
