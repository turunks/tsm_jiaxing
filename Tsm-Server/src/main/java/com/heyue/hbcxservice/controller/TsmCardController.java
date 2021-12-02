package com.heyue.hbcxservice.controller;


import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.request.*;
import com.heyue.hbcxservice.message.response.*;
import com.heyue.hbcxservice.service.TsmCardstatusNotifyService;
import com.heyue.hbcxservice.service.TsmCardDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/hb/card")
public class TsmCardController {

    @Autowired
    private TsmCardDetailService tsmCardDetailService;

    @Autowired
    private TsmCardstatusNotifyService tsmCardstatusNotifyService;

    /**
     * 获取开卡文件
     *
     * @param req
     * @return
     */
    @RequestMapping("/getOpenCardFile")
    public Result<GetOpenCardFileRes> getOpenCardFile(@RequestBody @Valid GetOpenCardFileReq req) {
        return tsmCardDetailService.getOpenCardFile(req);
    }

    /**
     * 应用状态通知
     *
     * @param req
     * @return
     */
    @RequestMapping("/appStatusNotice")
    public Result<String> appStatusNotice(@RequestBody @Valid AppStatusNoticeReq req) {
        return tsmCardstatusNotifyService.appStatusNotice(req);
    }

    /**
     * 圈存申请
     * @param req
     * @return
     */
    @RequestMapping("/transferApply")
    Result<TransferApplyRes> transferApply(@RequestBody @Valid TransferApplyReq req) {
        return tsmCardDetailService.transferApply(req);
    }

    /**
     * 圈存提交
     * @param req
     * @return
     */
    @RequestMapping("/transferSubmit")
    Result<TransferSubmitRes> transferSubmit(@RequestBody @Valid TransferSubmitReq req) {
        return tsmCardDetailService.transferSubmit(req);
    }

    /**
     * 激活申请
     * @param req
     * @return
     */
    @RequestMapping("/userCardActive")
    Result<UserCardActiveRes> userCardActive(@RequestBody @Valid UserCardActiveReq req) {
        return tsmCardDetailService.userCardActive(req);
    }

    /**
     * 激活提交
     * @param req
     * @return
     */
    @RequestMapping("/userCardActiveSubmit")
    Result<UserCardActiveSubmitRes> userCardActiveSubmit(@RequestBody @Valid UserCardActiveSubmitReq req) {
        return tsmCardDetailService.userCardActiveSubmit(req);
    }
}
