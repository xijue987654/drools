package com.xijue.drools.controller;

import com.xijue.drools.entity.TestRequest;
import com.xijue.drools.entity.TestResponse;
import com.xijue.drools.entity.biz.RatingRequest;
import com.xijue.drools.entity.biz.RatingResponse;
import com.xijue.drools.service.BusinessService;
import com.xijue.drools.utils.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description： 业务控制器
 * Author: xijue987654
 * Date: Created in 2020/7/31 16:45
 * Version: 1.0
 */
@RestController
@RequestMapping(value = "/business", produces = MediaType.APPLICATION_JSON_VALUE)
public class BusinessController {

    @Autowired
    BusinessService businessService;

    @Timer
    @RequestMapping(value = "/calSumScore", method = RequestMethod.POST)
    public TestResponse calSumScore(@RequestBody TestRequest request) throws Exception {
        TestResponse result = new TestResponse();
        result = businessService.calSumScore(request);
        return result;
    }

    @Timer
    @RequestMapping(value = "/calRatingSumScore", method = RequestMethod.POST)
    public RatingResponse calRatingSumScore(@RequestBody RatingRequest request) throws Exception {
        if (request == null || request.getLists().isEmpty()) {
            throw new Exception("请求参数不能为空!");
        }

        return businessService.calRatingSumScore(request);
    }

    @Timer
    @RequestMapping(value = "/calRatingSumScore2", method = RequestMethod.POST)
    public RatingResponse calRatingSumScore2(@RequestBody RatingRequest request) throws Exception {
        if (request == null || request.getLists().isEmpty()) {
            throw new Exception("请求参数不能为空!");
        }

        return businessService.calRatingSumScore2(request);
    }


}
