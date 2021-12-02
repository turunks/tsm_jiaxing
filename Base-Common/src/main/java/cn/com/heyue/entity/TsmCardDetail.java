package cn.com.heyue.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tsm_card _detail
 * @author 
 */
@Data
public class TsmCardDetail implements Serializable {
    /**
     * 自增长id
     */
    private Long id;

    /**
     * 卡文件id
     */
    private Long cadfileId;

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
     * 卡片序列号
     */
    private String cardNo;

    /**
     * 发卡方标识
     */
    private String cardSign;

    /**
     * 发卡方应用版本
     */
    private String cardAppVersion;

    /**
     * 应用序列号
     */
    private String appSerialNo;

    /**
     * 应用启用日期
     */
    private String appStartDate;

    /**
     * 应用失效日期
     */
    private String appValidDate;

    /**
     * fci数据
     */
    private String cardCustomFci;

    /**
     * 卡类型标识
     */
    private String cardTypeSign;

    /**
     * 国际代码
     */
    private String internateCode;

    /**
     * 省际代码
     */
    private String provinceCode;

    /**
     * 互通卡种
     */
    private String contactCardType;

    /**
     * 卡类型
     */
    private String cardType;

    /**
     * 预留
     */
    private String reserve;

    /**
     * 国际密钥
     */
    private String internationKey;

    /**
     * 国密密钥
     */
    private String domesticKey;

    /**
     * 卡状态
     */
    private String cardStatus;

    /**
     * 入库时间
     */
    private Date inDepositTime;

    /**
     * 出库时间
     */
    private Date outDepositTime;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 服务商号
     */
    private String merchantNo;

    /**
     * 业务订单号
     */
    private String serviceOrderId;

    private static final long serialVersionUID = 1L;
}