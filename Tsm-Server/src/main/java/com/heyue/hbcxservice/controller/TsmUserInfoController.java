package com.heyue.hbcxservice.controller;

import cn.com.heyue.entity.TsmUserInfo;
import com.alibaba.fastjson.JSON;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.response.OrderApplyRes;
import com.heyue.hbcxservice.message.response.TsmUserInfoRes;
import com.heyue.hbcxservice.service.TsmUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hb/user")
public class TsmUserInfoController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass()); // 日志类

    @Autowired
    private TsmUserInfoService tsmUserInfoService;

    @RequestMapping("/userSyn")
    @ResponseBody
    public Result<TsmUserInfoRes> save(@RequestBody TsmUserInfo userInfo) {
        logger.info("【用户信息同步】请求参数{}", JSON.toJSONString(userInfo));
        Result<TsmUserInfoRes> result = tsmUserInfoService.save(userInfo);
        logger.info("【用户信息同步】返回参数{}", JSON.toJSONString(result));
        return result;
    }
}
