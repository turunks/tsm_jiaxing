package cn.com.heyue.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tsm_refund_order
 * @author 
 */
@Data
public class TsmRefundOrder implements Serializable {
    /**
     * 自增长id
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
     * 业务订单类型：00：支付+开卡 01：支付+开卡+圈存 02：支付+圈存 03：应用迁移云备份 04：应用恢复 05：删卡+退款（好卡）06：维修退款（坏卡）
     */
    private String servicetype;

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
     * 退款金额（分）
     */
    private Integer refundamount;

    /**
     * 退款结果：00：退款成功 01：退款失败
     */
    private String refundret;

    /**
     * 创建时间
     */
    private Date createtime;

    private static final long serialVersionUID = 1L;
}