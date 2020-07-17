package com.xijue.drools.utils;

/**
 * Author: xijue987654
 * Date: Created in 2020/7/13 19:00
 * Version: 1.0
 */
public class CommonUtil {

    /**
     * 生成随机数
     * @param num
     * @return
     */
    public String generateRandom(int num) {
        String chars = "0123456789";
        StringBuffer number=new StringBuffer();
        for (int i = 0; i < num; i++) {
            int rand = (int) (Math.random() * 10);
            number=number.append(chars.charAt(rand));
        }
        return number.toString();
    }

}
