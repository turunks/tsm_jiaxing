package cn.com.heyue.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tsm_card_consumefile
 * @author 
 */
@Data
public class TsmCardConsumefile implements Serializable {
    private Long id;

    /**
     * 消费记录文件名称
     */
    private String consumefileName;

    /**
     * 消费记录文件ftp路径
     */
    private String consumefileFtppath;

    /**
     * 消费记录总数
     */
    private Integer recordNum;

    /**
     * 消费记录文件获取时间
     */
    private Date consumefileCreatetime;

    private static final long serialVersionUID = 1L;
}