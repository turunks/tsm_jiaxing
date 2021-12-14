package com.heyue.hbcxservice.message.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
public class AppStatusNoticeReq {

    /**
     * 应用AID
     */
    @NotBlank(message = "应用AID不能为空")
    private String appId;

    /**
     * 商户号
     */
    @NotBlank(message = "商户号不能为空")
    private String merchantNo;


    /**
     * 业务订单号
     */
    @NotBlank(message = "业务订单号不能为空")
    private String serviceOrderId;

    /**
     * 操作类型：01 支付+开卡  02 删卡+退款  03 维修退款
     */
    @NotBlank(message = "操作类型不能为空")
    private String optType;

    /**
     * 卡片状态
     * 00：卡片正常（好卡）
     * 01：卡片异常（坏卡）
     */
    @NotBlank(message = "卡片状态不能为空")
    private String cardstatus;


}
