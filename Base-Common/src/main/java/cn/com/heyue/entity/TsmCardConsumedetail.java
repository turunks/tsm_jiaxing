package cn.com.heyue.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * tsm_card_consumedetail
 * @author 
 */
@Data
public class TsmCardConsumedetail implements Serializable {
    private Long id;

    /**
     * 消费记录文件id（为消费记录文件表的id）
     */
    private Long consumecadfileId;

    /**
     * 本地流水号
     */
    private String localSerialNumber;

    /**
     * 企业运营系统下的营运单位代码
     */
    private String unitCode;

    /**
     * 城市代码（交易发生地）
     */
    private String cityCodeTransaction;

    /**
     * *终端机编码
     */
    private String terminalNo;

    /**
     * *卡内号
     */
    private String cardNo;

    /**
     * *卡消费计数器
     */
    private String cardConsumeCounter;

    /**
     * 消费前卡余额/余次
     */
    private String beforeconsumeCardbalance;

    /**
     * *交易金额/次数
     */
    private String transactionAmout;

    /**
     * *交易发生日期
     */
    private String transactionDate;

    /**
     * *交易发生时间
     */
    private String transactionTime;

    /**
     * #行业内公司编号
     */
    private String companyNo;

    private static final long serialVersionUID = 1L;
}