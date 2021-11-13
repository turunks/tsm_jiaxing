package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 卡消费记录请求
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardConsumRecordReq {

    private String card_no; // 卡应用序列号

    private String terminal_code; // 终端编号

    private String Starttime; // 查询开始时间

    private String Endtime; // 查询结束时间

    private String algorithm_id; // 加密算法标识

}
