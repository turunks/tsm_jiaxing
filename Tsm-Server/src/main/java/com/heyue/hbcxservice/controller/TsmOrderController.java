package com.heyue.hbcxservice.controller;

import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.request.CmpayNotifyReq;
import com.heyue.hbcxservice.message.request.OrderApplyReq;
import com.heyue.hbcxservice.message.request.OrderQueryReq;
import com.heyue.hbcxservice.message.response.OrderApplyRes;
import com.heyue.hbcxservice.message.response.PayOrderRes;
import com.heyue.hbcxservice.service.TsmOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/hb/order")
public class TsmOrderController {

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
        return tsmOrderInfoService.orderApply(orderApplyReq);
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
        return tsmOrderInfoService.getPayOrder(orderQueryReq.getServiceOrderId());
    }

}
