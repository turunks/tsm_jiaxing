package cn.com.heyue.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tsm_card_makefile
 * @author 
 */
@Data
public class TsmCardMakefile implements Serializable {
    /**
     * 自增长id
     */
    private Long id;

    /**
     * 卡申请城市代码
     */
    private String applyCityCode;

    /**
     * 地区代码
     */
    private String areaCode;

    /**
     * 卡种类型
     */
    private String cardSpecies;

    /**
     * 制卡文件名称
     */
    private String makefileName;

    /**
     * 制卡文件ftp路径
     */
    private String makefileFtppath;

    /**
     * 制卡文件创建时间
     */
    private Date makefileCreatetime;

    /**
     * 制卡文件流水号
     */
    private String makefileSerialno;

    /**
     * 制卡总数
     */
    private Integer cardnum;

    /**
     * 制卡请求类型
     */
    private String cardtype;

    /**
     * 反馈文件名称
     */
    private String feedbackFilename;

    /**
     * 反馈文件时间
     */
    private String feedbackfileCreatetime;

    /**
     * 反馈文件获取时间
     */
    private Date gettime;

    /**
     * 反馈文件流水号
     */
    private String feedbackfileSerialno;

    /**
     * 反馈文件解析卡数据数
     */
    private Integer feedbackfileCardnum;

    private static final long serialVersionUID = 1L;
}