package com.xijue.drools.controller;

import com.xijue.drools.entity.RequestDto;
import com.xijue.drools.service.ReloadDroolsRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: mss13072
 * Date: Created in 2020/7/13 18:59
 * Version: 1.0
 */
@RestController
@RequestMapping(value = "/common", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonController {

    @Autowired
    private ReloadDroolsRules reloadDroolsRules;

    @RequestMapping("/reload")
    public String reload(RequestDto request) throws Exception {
        reloadDroolsRules.reload(request);
        return "reload ok!";
    }

    @RequestMapping(value = "/testReload", method = RequestMethod.POST)
    public String testReload() throws Exception {
        reloadDroolsRules.testReload();
        return "test reload ok!";
    }

    public String checkDrlFile(String drlName) throws Exception {
        // todo 校验drl文件是否有语法错误
        return "check is ok!";
    }

    @RequestMapping("/updateDrlFile")
    public String updateDrlFile(MultipartFile file) throws Exception {
        // todo 更新drl后，再调用reload方法重载。即可热部署
        return "ok";
    }

}
