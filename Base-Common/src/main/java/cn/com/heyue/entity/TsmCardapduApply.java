package cn.com.heyue.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tsm_cardapdu_apply
 * @author 
 */
@Data
public class TsmCardapduApply implements Serializable {
    /**
     * 自增长id
     */
    private Long id;

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
     * 终端编号
     */
    private String terminalNo;

    /**
     * Tsm终端号
     */
    private String tsmNo;

    /**
     * 卡应用序列号
     */
    private String cardNo;

    /**
     * 卡操作类型：
01 卡激活
02 卡圈存
     */
    private String cardOptype;

    /**
     * 终端交易号
     */
    private String transactionNum;

    /**
     * 交易日期时间
     */
    private Date transactionDatetime;

    /**
     * 是否请求被提交
00 未提交
01 已提交
     */
    private String issubmit;

    /**
     * 请求提交时间
     */
    private Date submittime;

    /**
     * 指令数据
     */
    private String apdu;

    private static final long serialVersionUID = 1L;
}