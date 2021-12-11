package com.heyue.hbcxservice.message.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
public class GetOpenCardFileReq {

    /**
     * 业务订单号
     */
    @NotBlank(message = "业务订单号不能为空")
    private String serviceOrderId;

    /**
     * 商户号
     */
    @NotBlank(message = "商户号不能为空")
    private String merchantNo;

    /**
     * tsm用户ID
     */
    @NotBlank(message = "tsm用户ID不能为空")
    private String userId;

}
