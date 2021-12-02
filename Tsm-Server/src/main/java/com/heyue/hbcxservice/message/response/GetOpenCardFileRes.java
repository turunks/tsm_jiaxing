package com.heyue.hbcxservice.message.response;

import lombok.Data;


@Data
public class GetOpenCardFileRes {

    /**
     * BASE64后的bin文件
     */
    private String cardOpeningData;

}
