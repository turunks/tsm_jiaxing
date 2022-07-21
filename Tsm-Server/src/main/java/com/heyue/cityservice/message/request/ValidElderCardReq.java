package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 敬老卡鉴权请求
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidElderCardReq {
    private String idcardAES; // 身份IDcard AES编码
}
