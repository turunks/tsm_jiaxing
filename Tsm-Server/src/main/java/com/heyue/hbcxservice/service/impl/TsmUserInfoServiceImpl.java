package com.heyue.hbcxservice.service.impl;

import cn.com.heyue.entity.TsmUserInfo;
import cn.com.heyue.mapper.TsmUserInfoMapper;
import com.alibaba.fastjson.JSON;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.response.TsmUserInfoRes;
import com.heyue.hbcxservice.service.TsmUserInfoService;
import com.heyue.utils.GenerateIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TsmUserInfoServiceImpl implements TsmUserInfoService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass()); // 日志类

    @Autowired
    private TsmUserInfoMapper tsmUserInfoMapper;

    @Override
    public Result<TsmUserInfoRes> save(TsmUserInfo userInfo) {
        String userId = null;
        int count = 0;
        try {
            TsmUserInfo tsmUserInfo = tsmUserInfoMapper.selectByThirdUserId(userInfo.getThirdUserId());
            if (tsmUserInfo != null) {
                return Result.ok(TsmUserInfoRes.builder()
                        .userId(tsmUserInfo.getUserId())
                        .thirdUserId(tsmUserInfo.getThirdUserId()).build());
            }
            userId = GenerateIdUtils.getUserId();
            userInfo.setUserId(userId);
            userInfo.setCreateTime(new Date());
            count = tsmUserInfoMapper.insert(userInfo);
        } catch (Exception e) {
            logger.error("同步用户信息失败，{}", e);
            return Result.fail(null, "同步用户信息失败");
        }
        logger.info("同步用户信息成功{}条, userInfo={}", count, JSON.toJSONString(userId));
        return Result.ok(TsmUserInfoRes.builder().userId(userId).thirdUserId(userInfo.getThirdUserId()).build());
    }

    @Override
    public TsmUserInfo getUserInfo(String userId) {
        return tsmUserInfoMapper.selectByPrimaryKey(userId);
    }
}
