package com.xijue.drools.config;

import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

/**
 * 自定义执行的规则过滤, 通过package区分
 * Created by xijue987654 on 2020/7/31 14:06
 */
public class CustomAgendaFilter implements AgendaFilter {

    private final String packageName; // 传入的package name

    public CustomAgendaFilter(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean accept(Match match) {
        return match.getRule().getPackageName().equals(packageName);
    }

}