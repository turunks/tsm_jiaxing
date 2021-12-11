package cn.com.heyue.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tsm_user_info
 * @author 
 */
@Data
public class TsmUserInfo implements Serializable {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 商户号
     */
    private String merchantNo;

    /**
     * 第三方用户id
     */
    private String thirdUserId;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}