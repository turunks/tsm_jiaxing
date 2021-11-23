package com.heyue.cityservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 卡激活
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardActiveSubmitRes {
    private String transaction_datetime; // 交易日期时间
    private String transaction_num; // 终端编号
}