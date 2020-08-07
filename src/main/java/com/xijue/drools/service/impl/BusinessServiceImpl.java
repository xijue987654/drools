package com.xijue.drools.service.impl;

import com.xijue.drools.api.CommonConstant;
import com.xijue.drools.config.CustomAgendaFilter;
import com.xijue.drools.entity.Framework;
import com.xijue.drools.entity.TestRequest;
import com.xijue.drools.entity.TestResponse;
import com.xijue.drools.entity.biz.Rating;
import com.xijue.drools.entity.biz.RatingRequest;
import com.xijue.drools.entity.biz.RatingResponse;
import com.xijue.drools.service.BusinessService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Description： 业务接口实现类
 * Author: xijue987654
 * Date: Created in 2020/7/31 16:44
 * Version: 1.0
 */
@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    private List<Rating> ratingList;

    /**
     * 加载规则目录
     */
    private final String BUILD_PATH = "src/main/resources/";

    /**
     * Kiebase
     */
    private final String KEI_BASE = "kieBase_";

    /**
     * Kiebase
     */
    private final String SESSION = "session_";

    @Autowired
    private KieServices kieServices;

    @Autowired
    private KieFileSystem kieFileSystem;

    @Autowired
    private KieModuleModel kieModuleModel;

    @Autowired
    private KieRepository kieRepository;


    @Override
    public TestResponse calSumScore(TestRequest request) throws Exception {
        KieContainer kContainer = kieServices.newKieClasspathContainer("rules");
        KieSession kieSession = kContainer.newKieSession("ksession-rule");

        kieSession.insert(request);
        kieSession.insert(new Framework());

        TestResponse response = new TestResponse();
        response.setSumScore(new BigDecimal("0"));
        kieSession.insert(response);

        int ruleNums = kieSession.fireAllRules(new CustomAgendaFilter(CommonConstant.PACKAGE_NAME_BUSINESS));
        log.info("BusinessServiceImpl.calSumScore触发了{}条规则! 计算得到的结果为:{}", ruleNums, response.getSumScore());
        kieSession.dispose();
        return response;
    }

    @Override
    public RatingResponse calRatingSumScore(RatingRequest request) throws Exception {
        KieContainer kContainer = kieServices.newKieClasspathContainer();
        KieSession kieSession = kContainer.newKieSession("ksession-rule");

        // 对象1
        kieSession.insert(request);

        // 对象2
        Rating rating = new Rating();
        kieSession.insert(rating);

        // 对象3
        List<String> list = new ArrayList<>();
        list.add("0");
        kieSession.insert(list);

        // 对象4
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("tempStr", "0");
        tempMap.put("tempScore", new BigDecimal("0"));
        kieSession.insert(tempMap);

        // 对象5
        RatingResponse response = new RatingResponse();
        response.setSumScore(new BigDecimal("0"));
        kieSession.insert(response);

        int ruleNums = kieSession.fireAllRules(new CustomAgendaFilter(CommonConstant.PACKAGE_NAME_BUSINESS));
        BigDecimal sumScore = response.getSumScore();
        BigDecimal bigDecimal = sumScore.setScale(2, RoundingMode.HALF_UP);
        response.setSumScore(bigDecimal);
        log.info("BusinessServiceImpl.calRatingSumScore触发了{}条规则! 计算得到的结果为:{}", ruleNums, response.getSumScore());
        kieSession.dispose();
        return response;

//        RatingResponse result = new RatingResponse();
//        this.ratingList = request.getLists();
//        BigDecimal sumScore = calSumScore("0");
//        result.setSumScore(sumScore.setScale(2, RoundingMode.HALF_UP));
//        return result;
    }

    @Override
    public RatingResponse calRatingSumScore2(RatingRequest request) throws Exception {
        // 加载外部规则文件
        buildRules();

        KieContainer kContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
        KieSession kieSession = kContainer.newKieSession(SESSION + "ratingTest2");

        // 对象1
        kieSession.insert(request);

        // 对象2
        Rating rating = new Rating();
        kieSession.insert(rating);

        // 对象3
        List<String> list = new ArrayList<>();
        list.add("0");
        kieSession.insert(list);

        // 对象4
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("tempStr", "0");
        tempMap.put("tempScore", new BigDecimal("0"));
        kieSession.insert(tempMap);

        // 对象5
        RatingResponse response = new RatingResponse();
        response.setSumScore(new BigDecimal("0"));
        kieSession.insert(response);

        int ruleNums = kieSession.fireAllRules(new CustomAgendaFilter(CommonConstant.PACKAGE_NAME_EXT_RATING));
        BigDecimal sumScore = response.getSumScore();
        BigDecimal bigDecimal = sumScore.setScale(2, RoundingMode.HALF_UP);
        response.setSumScore(bigDecimal);
        log.info("BusinessServiceImpl.calRatingSumScore2触发了{}条规则! 计算得到的结果为:{}", ruleNums, response.getSumScore());
        kieSession.dispose();

        return response;
    }

    /**
     * 查找下级，并计算结果
     */
    private BigDecimal calSumScore(String parentPointId) {
        BigDecimal sumScore = new BigDecimal("0");
        for (Rating rating : ratingList) {
            if (rating.getKeypointParentId().equals(parentPointId)) {
                if (rating.getScore() == null) {
                    rating.setScore(calSumScore(rating.getKeypointId()));
                }
                sumScore = sumScore.add(rating.getWeight().multiply(rating.getScore()));
            }
        }

        return sumScore;
    }

    public void buildRules() {
        File filePath = new File("D:\\work\\drlPackage\\");
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

}
