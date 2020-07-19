package com.xijue.drools.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: xijue987654
 * Date: Created in 2020/7/16 10:06
 * Version: 1.0
 */
@Table(name = "rule")
@Data
public class Rule implements Serializable {

    /**
     * 自增ID
     */
    @Column(name = "rid")
    private Integer rid;

    /**
     * 产品ID
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 域账号
     */
    @Column(name = "add_by")
    private String addBy;

    /**
     * 添加日期
     */
    @Column(name = "add_time")
    private Date addTime;

    /**
     * 域账号
     */
    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * 最后更新日期
     */
    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "is_deleted")
    private String isDeleted;

    @Column(name = "rule_name")
    private String ruleName;

    @Column(name = "rule_detail")
    private String ruleDetail;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "visible")
    private Integer visible;

}
