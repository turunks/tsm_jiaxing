package com.heyue.card.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 卡激活申请
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatCardDataReq {

    private String recordNum; // 记录总数

    private String city_code; // 城市代码

    private String requestType; // 请求类型

    private String area_code; // 地区代码

    private String card_species; // 卡种类型

}
