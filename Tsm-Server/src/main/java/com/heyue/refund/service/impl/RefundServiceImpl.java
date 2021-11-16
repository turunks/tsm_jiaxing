package com.heyue.refund.service.impl;

import cn.com.heyue.entity.TsmRefundBill;
import cn.com.heyue.mapper.TsmRefundBillMapper;
import com.heyue.refund.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundServiceImpl implements RefundService {
    @Autowired
    private TsmRefundBillMapper tsmRefundBillMapper;

    // 生成退款账单
    @Override
    public void createRefund(TsmRefundBill record) {
        tsmRefundBillMapper.insertSelective(record);
    }
}
