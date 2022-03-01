package com.heyue.cityservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 卡激活申请响应
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardReturnRes {

    private String transaction_datetime; // 交易日期时间

    private String transaction_num; // 终端交易序号
}
