package com.heyue.bean;

import cn.hutool.core.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TsmBaseReq<T> {
    private String version; // “1.0.0”,接口协议版本号
    private String city_code;// 城市平台编码，城市平台定义
    private String tsm_id;// 和能tsm平台标识，和能tsm分配
    private String action_id;// 操作流水号，格式：tsm_id+yyyymmddhhmmssms+4位随机数
    private T data;
    private String action_time;// 操作时间，时间格式：YYYYMMDDhhmmssms
    private String sign;// 网关签名

    /**
     * @param city_code 城市id
     * @param tsm_id    tsm_id
     * @param data      数据体
     * @param sign      签名
     */
    public TsmBaseReq(String city_code, String tsm_id, T data, String sign) {
        SimpleDateFormat df = new SimpleDateFormat("YYYYMMDDhhmmssms");
        String date_2 = df.format(new Date().getTime());
        // 4位随机数
        String random = RandomUtil.randomNumbers(4);
        this.version = "1.0.0";
        this.city_code = city_code;
        this.tsm_id = tsm_id;
        this.action_id = tsm_id + date_2 + random;
        this.data = data;
        this.action_time = date_2;
        this.sign = sign;
    }
}
