package com.heyue.hbcxservice.service.impl;

import cn.com.heyue.entity.TsmUserInfo;
import cn.com.heyue.mapper.TsmUserInfoMapper;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.response.TsmUserInfoRes;
import com.heyue.hbcxservice.service.TsmUserInfoService;
import com.heyue.utils.GenerateIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TsmUserInfoServiceImpl implements TsmUserInfoService {

    @Autowired
    private TsmUserInfoMapper tsmUserInfoMapper;

    @Override
    public Result<TsmUserInfoRes> save(TsmUserInfo userInfo) {
        String userId = GenerateIdUtils.getUserId();
        userInfo.setUserId(userId);
        userInfo.setCreateTime(new Date());
        int count = tsmUserInfoMapper.insert(userInfo);
        if (count == 0) {
            return Result.fail();
        }
        return Result.ok(TsmUserInfoRes.builder().userId(userId).thirdUserIid(userInfo.getThirdUserId()).build());
    }

    @Override
    public TsmUserInfo getUserInfo(String userId) {
        return tsmUserInfoMapper.selectByPrimaryKey(userId);
    }
}
