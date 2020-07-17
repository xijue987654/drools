package com.xijue.drools.entity;

/**
 * Author: xijue987654
 * Date: Created in 2020/7/15 10:38
 * Version: 1.0
 */
public class RequestDto {

    private String drlName;

    private String packageName;

    private String ruleName;

    /********************** getter and setter **************************/

    public String getDrlName() {
        return drlName;
    }

    public void setDrlName(String drlName) {
        this.drlName = drlName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
