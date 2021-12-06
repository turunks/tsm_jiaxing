package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 卡消费记录请求
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardConsumRecordReq {

    private String issue_inst; // 发卡机构代码：读取用户卡15H
    private String terminal_code; // 终端编号
    private String transaction_num; // 终端交易序号
    private String card_no; // 卡应用序列号
    private String starttime; // 查询开始时间
    private String endtime; // 查询结束时间

}
