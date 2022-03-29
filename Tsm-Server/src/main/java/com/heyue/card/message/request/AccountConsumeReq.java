package com.heyue.card.message.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountConsumeReq implements Serializable {
    private Integer id;

    /**
     * 结算日期
     */
    private String settleDate;

    /**
     * 8451 - 普通消费
        8251 - 进站
        8455 - 补票
        8470 – 锁卡
     */
    private String txnType;

    /**
     * 订单号
     */
    private String centSeq;

    /**
     * 收单机构日期
     */
    private String sendDate;

    /**
     * 收单机构流水号
     */
    private String sendSeq;

    /**
     * 交易日期YYYYMMDD
     */
    private String txnDate;

    /**
     * 交易时间HHMMSS
     */
    private String txnTime;

    /**
     * 06：单次消费
09：复合消费

     */
    private String transType;

    /**
     * 商户类型
     */
    private String mcc;

    /**
     * 渠道类型
     */
    private String txnChannel;

    /**
     * 终端编号
     */
    private String termId;

    /**
     * 终端流水号
     */
    private String termSeq;

    /**
     * PSAM设备号
     */
    private String posId;

    /**
     * PSAM卡流水号
     */
    private String samSeq;

    /**
     * 收单机构代码
     */
    private String acqInst;

    /**
     * 交易商户简称
     */
    private String txnMchntName;

    /**
     * 发卡机构代码
     */
    private String issOrgCode;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 卡消费计数器
     */
    private String cardDebitCnt;

    /**
     * 交易前余额
     */
    private String befBal;

    /**
     * 交易金额
     */
    private String txnAmt;

    /**
     * 交易认证码
     */
    private String tac;

    /**
     * 算法标识01：3des
04：SM4

     */
    private String algType;

    /**
     * 测试标记
     */
    private String testFlag;

    /**
     * 可疑原因
     */
    private String refuseRsn;

    /**
     * 线路名称
     */
    private String lineName;

    private String cityCode;

    private String areaCode;

    /**
     * 消费数据（前端读卡数据）
     * EP联机火脱机交易序号（1-2字节）
     * 透支限额（3-5字节）
     * 交易金额（6-9字节）
     * 交易类型标识（10字节）
     * 终端机编号（11-16字节）
     * 交易日期（17-20字节）
     * 交易时间（21-23字节）
     */
    private String consumeStr;

    private Date createdDate;

    private String cityName;

    private static final long serialVersionUID = 1L;
}