package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 卡账户信息请求
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardAccountInfoReq {
    private String issue_inst; // 发卡机构代码
    private String terminal_code; // 终端编号
    private String transaction_num; // 终端交易序号
    private String card_no; // 卡应用序列号
}
