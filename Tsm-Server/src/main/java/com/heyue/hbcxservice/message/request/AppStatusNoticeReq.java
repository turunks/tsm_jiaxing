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
     * 支付渠道、渠道编号：
     * 01：银联
     * 02：支付宝
     * 03：微信
     * 04：和包支付
     */
    @NotBlank(message = "支付渠道不能为空")
    private String payChannel;

    /**
     * 卡片状态
     * 00：卡片正常（好卡）
     * 01：卡片异常（坏卡）
     */
    @NotBlank(message = "卡片状态不能为空")
    private String cardstatus;

    /**
     * 用户真实姓名（当卡片是好卡且卡片删除，需要人工退余额时适用）
     */
    @NotBlank(message = "用户真实姓名不能为空")
    private String userName;

}
