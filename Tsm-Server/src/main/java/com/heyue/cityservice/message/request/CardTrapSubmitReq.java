package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 卡圈存请求提交
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardTrapSubmitReq {

    private String issue_inst; // 发卡机构代码

    private String terminal_code; // 终端编号

    private String transaction_num; // 终端交易序号

    private String card_no; // 卡应用序列号

    private String transaction_datetime; // 交易日期时间

    private String merchant_num; // 商户号

    private String ret_status; // 写卡状态

    private String tac; // 交易验证码

    private String order_no;// 全局唯一。确定唯一一笔交易，业务幂等性判断

}
