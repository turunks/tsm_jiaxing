package cn.com.heyue.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tsm_terminal_order
 * @author 
 */
@Data
public class TsmTerminalOrder implements Serializable {
    /**
     * 自增长id
     */
    private Long id;

    /**
     * 终端交易号
     */
    private String transactionNum;

    /**
     * 交易类型: 
1-获取开卡文件
2-激活请求
3-充值请求
4-退卡请求
5-退资请求
6-修改卡种类型
7-修改年检日期
     */
    private String transactionType;

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
     * 和能tsm终端号
     */
    private String tsmNo;

    /**
     * 创建时间
     */
    private Date createtime;

    private static final long serialVersionUID = 1L;
}