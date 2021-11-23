package com.heyue.utils;

import cn.hutool.core.util.RandomUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


public class IdUtil {

    public static SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmssms");

    /**
     * 获得10个长度的流水号（时间+4位随机数）
     *
     * @return UUID
     */
    public static String get10Serialno() {
        SimpleDateFormat df = new SimpleDateFormat("YYMMdd");
        String date_2 = df.format(new Date().getTime());
        String random = RandomUtil.randomNumbers(4);
        return date_2 + random;
    }
}
