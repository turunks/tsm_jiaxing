package com.heyue.card.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 城市平台回馈文件数据体
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatCardDataRes {

    private String card_no; // 用户卡号

    private String Card_sign; // 发卡方标识

    private String requestType; // 应用类型标识

    private String card_app_version; // 发卡方应用版本

    private String app_serial_no; // 应用序列号

    private String app_start_date; // 应用启用日期

    private String app_valid_date; // 应用失效日期

    private String card_custom_fci; // 发卡方自定义 FCI 数据

    private String card_type_sign; // 卡类型标识

    private String internate_code; // 国际代码

    private String province_code; // 省际代码

    private String city_code; // 城市代码

    private String contact_card_type; // 互通卡种

    private List<Secretkey> secretkeyList; // 国际密钥包含多种密钥 单字段保存


}
