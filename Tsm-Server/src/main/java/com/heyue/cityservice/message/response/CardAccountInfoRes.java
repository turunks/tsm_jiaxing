package com.heyue.cityservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 卡账户信息查询响应
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardAccountInfoRes {
    private String transaction_datetime; // 交易日期时间
    private String transaction_num; // 同请求transaction_num
    private String card_balance; // 卡账户余额
    private String Last_trading_time; // 最后交易时间
    private String annual_inspection_date; // 可被年检日期

}
