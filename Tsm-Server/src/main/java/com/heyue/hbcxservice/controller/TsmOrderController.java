package com.heyue.hbcxservice.controller;

import com.alibaba.fastjson.JSON;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.request.CmpayNotifyReq;
import com.heyue.hbcxservice.message.request.OrderApplyReq;
import com.heyue.hbcxservice.message.request.OrderQueryReq;
import com.heyue.hbcxservice.message.response.OrderApplyRes;
import com.heyue.hbcxservice.message.response.PayOrderRes;
import com.heyue.hbcxservice.service.TsmOrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/hb/order")
public class TsmOrderController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass()); // 日志类

    @Autowired
    private TsmOrderInfoService tsmOrderInfoService;

    /**
     * 下单支付
     *
     * @param orderApplyReq
     * @return
     */
    @RequestMapping("/orderApply")
    @ResponseBody
    public Result<OrderApplyRes> orderApply(@RequestBody @Valid OrderApplyReq orderApplyReq) {
        logger.info("【下单支付】请求参数{}", JSON.toJSONString(orderApplyReq));
        Result<OrderApplyRes> result = tsmOrderInfoService.orderApply(orderApplyReq);
        logger.info("【下单支付】返回参数{}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 支付后台通知
     *
     * @param notify
     * @return
     */
    @RequestMapping("/cmpayNotify")
    public String cmpayNotify(CmpayNotifyReq notify) {
        return tsmOrderInfoService.cmpayNotify(notify);
    }

    /**
     * 支付订单查询
     *
     * @param orderQueryReq
     * @return
     */
    @RequestMapping("/orderQuery")
    public Result<PayOrderRes> getPayOrder(@RequestBody OrderQueryReq orderQueryReq) {
        logger.info("【支付订单查询】请求参数{}", JSON.toJSONString(orderQueryReq));
        Result<PayOrderRes> result = tsmOrderInfoService.getPayOrder(orderQueryReq.getServiceOrderId());
        logger.info("【支付订单查询】返回参数{}", JSON.toJSONString(result));
        return result;
    }

}
