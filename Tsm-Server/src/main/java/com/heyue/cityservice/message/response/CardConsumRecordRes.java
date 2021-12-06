package com.heyue.cityservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 卡消费记录请求
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardConsumRecordRes {

    private String transaction_datetime; // 交易日期时间

    private String transaction_num; // 同请求transaction_num

    private List<Consumeinfo> consume_list; // 消费记录数据集。

}
