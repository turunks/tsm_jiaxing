package cn.com.heyue.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tsm_terminal
 * @author 
 */
@Data
public class TsmTerminal implements Serializable {
    private Long id;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用名称
     */
    private String appname;

    /**
     * 终端编号
     */
    private String terminalNo;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 终端描述
     */
    private String terminalDesc;

    private Date createtime;

    private Date updatetime;

    private static final long serialVersionUID = 1L;
}