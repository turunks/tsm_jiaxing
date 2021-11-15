package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 终端交易查询参数
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeInfoReq {

    private String transaction_num; // 终端交易序号

    private String terminal_code; // 终端编号:和能tsm的终端编号

    private Integer algorithm_id; // 加密算法标识: 00国际/01国密


}