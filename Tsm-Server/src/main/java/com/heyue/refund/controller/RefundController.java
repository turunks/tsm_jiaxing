package com.heyue.refund.controller;

import cn.com.heyue.entity.TsmRefundBill;
import cn.com.heyue.entity.TsmTerminalOrder;
import cn.com.heyue.mapper.TsmRefundBillMapper;
import cn.com.heyue.mapper.TsmTerminalOrderMapper;
import com.heyue.refund.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RefundController {

    @Autowired
    private RefundService refundService;


    // 退款服务
    @PostMapping("createRefund")
    @ResponseBody
    public void createRefund(@RequestBody TsmRefundBill record) {
        refundService.createRefund(record);
    }
}