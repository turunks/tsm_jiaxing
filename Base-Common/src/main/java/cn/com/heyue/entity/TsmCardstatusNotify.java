package cn.com.heyue.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tsm_cardstatus_notify
 * @author 
 */
@Data
public class TsmCardstatusNotify implements Serializable {
    /**
     * 自增长id
     */
    private Long id;

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
     * 卡应用序号
     */
    private String cardNo;

    /**
     * 操作类型：01 支付+开卡 02 删卡+退款 03 维修退款
     */
    private String opttype;

    /**
     * 创建时间
     */
    private Date createtime;

    private static final long serialVersionUID = 1L;
}