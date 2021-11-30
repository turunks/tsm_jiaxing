package cn.com.heyue.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tsm_opencard_syncfile
 * @author 
 */
@Data
public class TsmOpencardSyncfile implements Serializable {
    /**
     * 自增长id
     */
    private Long id;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 发卡信息同步文件名称
     */
    private String cardissuefile;

    /**
     * ftp地址
     */
    private String ftppath;

    /**
     * 发卡信息数
     */
    private Integer cardissuenum;

    /**
     * 发卡开始时间
     */
    private Date starttime;

    /**
     * 发卡截止时间
     */
    private Date endtime;

    /**
     * 创建时间
     */
    private Date createtime;

    private static final long serialVersionUID = 1L;
}