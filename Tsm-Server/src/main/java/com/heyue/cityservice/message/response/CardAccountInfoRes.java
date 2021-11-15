package com.heyue.cityservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 卡账户信息查询响应
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardAccountInfoRes {

    private String card_balance; // 卡账户余额

    private String Last_trading_time; // 最后交易时间

    private String Annual_inspection_date; // 可被年检日期

}