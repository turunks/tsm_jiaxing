package com.heyue.refund.service;

import cn.com.heyue.entity.TsmRefundBill;

/**
 * 退款服务
 */
public interface RefundService {

    // 生成退款账单
    void createRefund(TsmRefundBill record);
}
