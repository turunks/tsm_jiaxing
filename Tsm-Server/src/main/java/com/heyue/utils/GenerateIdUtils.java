package com.heyue.utils;

import cn.com.heyue.utils.DateUtils;

import java.util.Date;
import java.util.UUID;

public class GenerateIdUtils {

    /**
     * 机器ID，最大支持1-9个集群机器部署
     */
    public static int machineId = 1;

    /**
     * 生成用户Id
     *
     * @return
     */
    public static String getUserId() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        return machineId + String.format("%016d", hashCodeV);
    }

    /**
     * 生成用户Id
     *
     * @return
     */
    public static String getOrderId() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        return DateUtils.format(new Date(),"yyyyMMddHHmmss") + String.format("%010d", hashCodeV);
    }

    public static void main(String[] args) {
        System.out.println(getUserId());
    }
}
