package com.heyue.hbcxservice.controller;


import com.alibaba.fastjson.JSON;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.request.*;
import com.heyue.hbcxservice.message.response.*;
import com.heyue.hbcxservice.service.TsmCardDetailService;
import com.heyue.hbcxservice.service.TsmCardstatusNotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/hb/card")
public class TsmCardController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass()); // 日志类

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
        logger.info("【获取开卡文件】请求参数{}", JSON.toJSONString(req));
        Result<GetOpenCardFileRes> result = tsmCardDetailService.getOpenCardFile(req);
        logger.info("【获取开卡文件】返回参数{}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 应用状态通知
     *
     * @param req
     * @return
     */
    @RequestMapping("/appStatusNotice")
    public Result<String> appStatusNotice(@RequestBody @Valid AppStatusNoticeReq req) {
        logger.info("【应用状态通知】请求参数{}", JSON.toJSONString(req));
        Result<String> result = tsmCardstatusNotifyService.appStatusNotice(req);
        logger.info("【应用状态通知】返回参数{}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 圈存申请
     *
     * @param req
     * @return
     */
    @RequestMapping("/transferApply")
    Result<TransferApplyRes> transferApply(@RequestBody @Valid TransferApplyReq req) {
        req.setCard_no("0" + req.getCard_no());
        logger.info("【圈存申请】请求参数{}", JSON.toJSONString(req));
        Result<TransferApplyRes> result = tsmCardDetailService.transferApply(req);
        logger.info("【圈存申请】返回参数{}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 圈存提交
     *
     * @param req
     * @return
     */
    @RequestMapping("/transferSubmit")
    Result<TransferSubmitRes> transferSubmit(@RequestBody @Valid TransferSubmitReq req) {
        req.setCard_no("0" + req.getCard_no());
        logger.info("【圈存提交】请求参数{}", JSON.toJSONString(req));
        Result<TransferSubmitRes> result = tsmCardDetailService.transferSubmit(req);
        logger.info("【圈存提交】返回参数{}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 激活申请
     *
     * @param req
     * @return
     */
    @RequestMapping("/userCardActive")
    Result<UserCardActiveRes> userCardActive(@RequestBody @Valid UserCardActiveReq req) {
        req.setCard_no("0" + req.getCard_no());
        logger.info("【激活申请】请求参数{}", JSON.toJSONString(req));
        Result<UserCardActiveRes> result = tsmCardDetailService.userCardActive(req);
        logger.info("【激活申请】返回参数{}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 激活提交
     *
     * @param req
     * @return
     */
    @RequestMapping("/userCardActiveSubmit")
    Result<UserCardActiveSubmitRes> userCardActiveSubmit(@RequestBody @Valid UserCardActiveSubmitReq req) {
        req.setCard_no("0" + req.getCard_no());
        logger.info("【激活提交】请求参数{}", JSON.toJSONString(req));
        Result<UserCardActiveSubmitRes> result = tsmCardDetailService.userCardActiveSubmit(req);
        logger.info("【激活提交】返回参数{}", JSON.toJSONString(result));
        return result;
    }
}
