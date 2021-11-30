package cn.com.heyue.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tsm_opencard_info
 * @author 
 */
@Data
public class TsmOpencardInfo implements Serializable {
    /**
     * 自增长id
     */
    private Long id;

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
     * 用户id
     */
    private String userId;

    /**
     * 卡应用序列号
     */
    private String cardNo;

    /**
     * 开卡时间
     */
    private Date opencardTime;

    /**
     * 发卡机构代码
     */
    private String issOrgCode;

    /**
     * 终端机编号
     */
    private String terminalNo;

    /**
     * 创建时间
     */
    private Date createtime;

    private static final long serialVersionUID = 1L;
}