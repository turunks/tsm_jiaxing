package cn.com.heyue.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * tsm_cmpay_config
 * @author 
 */
@Data
public class TsmCmpayConfig implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 城市代码
     */
    private String applyCityCode;

    /**
     * 支付地址
     */
    private String cmpayUrl;

    /**
     * 商户号
     */
    private String cmpayMerNo;

    /**
     * 秘钥
     */
    private String cmpayMerKey;

    /**
     * 支付结果通知地址
     */
    private String cmpayNotifyUrl;

    /**
     * 支付回调页面
     */
    private String cmpayCallbackUrl;

    /**
     * 通知第三方平台地址
     */
    private String thirdNotifyUrl;

    /**
     * 服务ip
     */
    private String serviceIp;

    private static final long serialVersionUID = 1L;
}