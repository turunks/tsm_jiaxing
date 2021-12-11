package cn.com.heyue.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tsm_order_info
 * @author 
 */
@Data
public class TsmOrderInfo implements Serializable {
    /**
     * 业务订单号
     */
    private String serviceOrderId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 商户号
     */
    private String merchantNo;

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
     * 用户Id
     */
    private String userId;

    /**
     * 卡应用序列号
     */
    private String cardNo;

    /**
     * 订单类型：1.支付+开卡+圈存  2.支付+圈存  3.退卡+退款 4.坏卡退卡
     */
    private Integer orderType;

    /**
     * 支付平台编号
     */
    private String payPlatNo;

    /**
     * 交易金额（分）
     */
    private Integer amount;

    /**
     * 开卡费（分）
     */
    private Integer cardPrice;

    /**
     * 充值选择金额（分）
     */
    private Integer topUpAmount;

    /**
     * 单次活动金额（分）
     */
    private Integer marketAmount;

    /**
     * 活动编号
     */
    private String marketNo;

    /**
     * 优惠出资方：0.中交金卡 1.中移金科（和包app业务活动出资方）
     */
    private Byte marketOrg;

    /**
     * 活动累计金额（分）
     */
    private Integer cumAmount;

    /**
     * 支付参数
     */
    private String payParm;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}