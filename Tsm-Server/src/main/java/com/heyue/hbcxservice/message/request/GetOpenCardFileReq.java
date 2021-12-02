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
     * 城市代码
     */
    @NotBlank(message = "城市代码不能为空")
    private String cityCode;

    /**
     * 地区代码
     */
    @NotBlank(message = "地区代码不能为空")
    private String areaCode;

    /**
     * 卡种类型
     */
    @NotBlank(message = "卡种类型不能为空")
    private String cardSpecies;

    /**
     * tsm用户ID
     */
    @NotBlank(message = "tsm用户ID不能为空")
    private String userId;

}
