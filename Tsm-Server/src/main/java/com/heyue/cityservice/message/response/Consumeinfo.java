package com.heyue.cityservice.message.response;

import lombok.Data;

@Data
public class Consumeinfo {
    private String index; // 索引

    private String Consumeamount; // 消费金额：单位分

    private String Consumetime; // 消费时间

    private String Consumeline; // 消费线路
}
