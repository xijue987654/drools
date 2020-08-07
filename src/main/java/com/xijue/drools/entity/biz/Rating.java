package com.xijue.drools.entity.biz;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Rating {
    // 要点id
    private String keypointId;

    // 要点code
    private String keypointCode;

    // 要点name
    private String keypointName;

    // 要点排序id
    private int keypointSortId;

    // 要点路径
    private String keypointPath;

    // 要点父级id
    private String keypointParentId;

    // 权重
    private BigDecimal weight;

    // 分数
    private BigDecimal score;

    // 是否叶子节点
    private Boolean isLeaf;

}
