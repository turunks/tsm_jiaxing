package com.heyue.hbcxservice.service;

import cn.com.heyue.entity.TsmUserInfo;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.response.TsmUserInfoRes;

public interface TsmUserInfoService {

    /**
     * 新增用户
     * @param userInfo
     * @return
     */
    Result<TsmUserInfoRes> save(TsmUserInfo userInfo);

    /**
     * 查用户信息
     * @param userId
     * @return
     */
    TsmUserInfo getUserInfo(String userId);
}
