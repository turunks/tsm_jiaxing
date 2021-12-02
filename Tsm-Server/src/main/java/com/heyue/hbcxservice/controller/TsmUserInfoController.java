package com.heyue.hbcxservice.controller;

import cn.com.heyue.entity.TsmUserInfo;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.response.TsmUserInfoRes;
import com.heyue.hbcxservice.service.TsmUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hb/user")
public class TsmUserInfoController {

    @Autowired
    private TsmUserInfoService tsmUserInfoService;

    @RequestMapping("/userSyn")
    @ResponseBody
    public Result<TsmUserInfoRes> save(@RequestBody TsmUserInfo userInfo) {
        return tsmUserInfoService.save(userInfo);
    }
}
