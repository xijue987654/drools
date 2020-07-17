package com.xijue.drools.controller;

import com.xijue.drools.entity.PointDomain;
import com.xijue.drools.service.PointRuleEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: xijue987654
 * Date: Created in 2020/7/10 17:02
 * Version: 1.0
 */
@RestController
public class DemoController {

    @Autowired
    private PointRuleEngineService ruleEngineService;

    @RequestMapping("/rule/param")
    public void param() {
        PointDomain pointDomain = new PointDomain();

        pointDomain.setUserName("hello kitty");
        pointDomain.setBackMondy(100d);
        pointDomain.setBuyMoney(500d);
        pointDomain.setBackNums(1);
        pointDomain.setBuyNums(5);
        pointDomain.setBillThisMonth(5);
        pointDomain.setBirthDay(true);
        pointDomain.setPoint(0l);

        ruleEngineService.executePointRule(pointDomain);

        System.out.println("执行完毕BillThisMonth：" + pointDomain.getBillThisMonth());
        System.out.println("执行完毕BuyMoney：" + pointDomain.getBuyMoney());
        System.out.println("执行完毕BuyNums：" + pointDomain.getBuyNums());
        System.out.println("执行完毕规则引擎总积分：" + pointDomain.getPoint());

        PointDomain pointDomain2 = new PointDomain();

        pointDomain2.setUserName("hello kitty");
        pointDomain2.setBackMondy(100d);
        pointDomain2.setBuyMoney(500d);
        pointDomain2.setBackNums(1);
        pointDomain2.setBuyNums(5);
        pointDomain2.setBillThisMonth(5);
        pointDomain2.setBirthDay(false);
        pointDomain2.setPoint(0l);

        ruleEngineService.executePointRule(pointDomain2);

        System.out.println("执行完毕BillThisMonth：" + pointDomain2.getBillThisMonth());
        System.out.println("执行完毕BuyMoney：" + pointDomain2.getBuyMoney());
        System.out.println("执行完毕BuyNums：" + pointDomain2.getBuyNums());
        System.out.println("执行完毕规则引擎总积分：" + pointDomain2.getPoint());

    }


}
