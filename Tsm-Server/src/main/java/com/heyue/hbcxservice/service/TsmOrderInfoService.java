package com.heyue.hbcxservice.service;

import cn.com.heyue.entity.TsmOrderInfo;
import cn.com.heyue.entity.TsmPayOrder;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.request.CmpayNotifyReq;
import com.heyue.hbcxservice.message.request.OrderApplyReq;
import com.heyue.hbcxservice.message.request.OrderReFundReq;
import com.heyue.hbcxservice.message.response.OrderApplyRes;
import com.heyue.hbcxservice.message.response.PayOrderRes;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TsmOrderInfoService {

    /**
     * 下单
     *
     * @param orderApplyReq
     * @return
     */
    Result<OrderApplyRes> orderApply(OrderApplyReq orderApplyReq);

    /**
     * 订单退款
     * @param orderReFundReq
     * @return
     */
    Result<String> orderRefund(OrderReFundReq orderReFundReq);

    /**
     * 查询订单
     * @param serviceOrderId
     * @return
     */
    TsmOrderInfo getOrder(String serviceOrderId);

    /**
     * 查可退款订单列表
     * @param userId
     * @param cardNo
     * @return
     */
    List<TsmOrderInfo> getRefund(String userId, String cardNo);

    /**
     * 查支付订单
     * @param serviceOrderId
     * @return
     */
    Result<PayOrderRes> getPayOrder(String serviceOrderId);

    /**
     * 更新订单
     * @param tsmOrderInfo
     * @return
     */
    int updateOrder(TsmOrderInfo tsmOrderInfo);

    /**
     * 更新支付订单
     * @param tsmPayOrder
     * @return
     */
    int updatePayOrder(TsmPayOrder tsmPayOrder);

    /**
     * 和包支付
     * @param notify
     * @return
     */
    String cmpayNotify(CmpayNotifyReq notify);
}
