package cn.com.heyue.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tsm_refund_bill
 * @author 
 */
@Data
public class TsmRefundBill implements Serializable {
    /**
     * 自增长id
     */
    private Long billId;

    /**
     * 业务订单号
     */
    private String serviceorderid;

    /**
     * 卡操作状态通知id
     */
    private Long cardoperationstatusNotify;

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
     * 区域代码
     */
    private String areaCode;

    /**
     * 卡种类型
     */
    private String cardSpecies;

    /**
     * 卡片序列号
     */
    private String cardNo;

    /**
     * 业务订单类型： 05：删卡+退款（好卡）06：维修退款（坏卡）
     */
    private String ordertype;

    /**
     * 退款方式 00 实时退款 01 非实时退款
     */
    private String refundType;

    /**
     * 退款金额（fen）
     */
    private Integer refundamount;

    /**
     * 创建时间
     */
    private Date createtime;

    private static final long serialVersionUID = 1L;
}