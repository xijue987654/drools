package com.xijue.drools.config;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Author: xijue987654
 * Date: Created in 2020/7/20 10:02
 * Version: 1.0
 */
public interface AbstractMapper<T> extends Mapper<T>, MySqlMapper<T>, ConditionMapper<T> {

}
