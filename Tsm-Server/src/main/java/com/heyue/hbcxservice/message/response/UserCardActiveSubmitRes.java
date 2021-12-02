package com.heyue.hbcxservice.message.response;

import lombok.Data;

@Data
public class UserCardActiveSubmitRes {
    private String transaction_datetime; // 交易日期时间
    private String transaction_num; // 终端编号
}
