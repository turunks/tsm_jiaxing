package cn.com.heyue.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tsm_pay_order
 * @author 
 */
@Data
public class TsmPayOrder implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 业务订单号
     */
    private String serviceOrderId;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 地区代码
     */
    private String areaCode;

    /**
     * 卡种类型
     */
    private String cardSpecies;

    /**
     * 订单类型: 00：支付+开卡  01：支付+开卡+圈存  02：支付+圈存
     */
    private String orderType;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 商户号
     */
    private String merchantNo;

    /**
     * 第三方支付平台编号
     */
    private String payPlatNo;

    /**
     * 支付金额（分）
     */
    private Integer payAmount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 第三方支付订单号
     */
    private String payOrderId;

    /**
     * 支付结果: 01：支付成功 02：支付失败  03：支付中
     */
    private String payRet;

    /**
     * 交易完成时间
     */
    private Date tradeTime;

    /**
     * 支付结果通知时间
     */
    private Date payNotifyTime;

    private static final long serialVersionUID = 1L;
}