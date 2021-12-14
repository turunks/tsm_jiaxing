package com.heyue.hbcxservice.message.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TsmUserInfoRes {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 第三方用户id
     */
    private String thirdUserId;
}
