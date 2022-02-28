package com.heyue.hbcxservice.service.impl;

import cn.com.heyue.entity.*;
import cn.com.heyue.mapper.*;
import com.alibaba.fastjson.JSON;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.cmpay.CmpayService;
import com.heyue.hbcxservice.message.request.AppStatusNoticeReq;
import com.heyue.hbcxservice.service.TsmCardstatusNotifyService;
import com.heyue.hbcxservice.service.TsmOrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TsmCardstatusNotifyServiceImpl implements TsmCardstatusNotifyService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass()); // 日志类

    @Autowired
    private TsmCardstatusNotifyMapper tsmCardstatusNotifyMapper;

    @Autowired
    private TsmCardDetailMapper tsmCardDetailMapper;

    @Autowired
    private TsmTerminalMpper tsmTerminalMpper;

    @Autowired
    private TsmOpencardInfoMapper tsmOpencardInfoMapper;

    @Autowired
    private TsmRefundOrderMapper tsmRefundOrderMapper;

    @Autowired
    private TsmUserInfoMapper tsmUserInfoMapper;

    @Autowired
    private TsmRefundBillMapper tsmRefundBillMapper;

    @Autowired
    private TsmOrderInfoService tsmOrderInfoService;

    @Autowired
    private CmpayService cmpayService;


    @Override
    public Result<String> appStatusNotice(AppStatusNoticeReq req) {
        try {
            TsmOrderInfo orderInfo = tsmOrderInfoService.getOrder(req.getServiceOrderId());
            TsmCardstatusNotify cardstatusNotify = new TsmCardstatusNotify();
            BeanUtils.copyProperties(req, cardstatusNotify);
            BeanUtils.copyProperties(orderInfo, cardstatusNotify);
            cardstatusNotify.setCreatetime(new Date());
            // 开卡通知
            if (req.getOptType().equals("01")) {
                TsmCardDetail cardDetail = tsmCardDetailMapper.selectOneByServiceOrderId(req.getServiceOrderId());
                if (cardDetail == null) {
                    return Result.fail(null, "暂无开卡信息");
                }
                // 储存发卡信息
                TsmTerminal tsmTerminal = tsmTerminalMpper.selectByCityCode(cardDetail.getApplyCityCode());
                TsmOpencardInfo tsmOpencardInfo = new TsmOpencardInfo();
                tsmOpencardInfo.setMerchantNo(req.getMerchantNo());
                tsmOpencardInfo.setCityCode(cardDetail.getCityCode());
                tsmOpencardInfo.setAreaCode(cardDetail.getAreaCode());
                tsmOpencardInfo.setCardSpecies(cardDetail.getCardSpecies());
                tsmOpencardInfo.setUserId(orderInfo.getUserId());
                tsmOpencardInfo.setCardNo(cardDetail.getCardNo());
                tsmOpencardInfo.setOpencardTime(cardDetail.getOutDepositTime());
                tsmOpencardInfo.setIssOrgCode(cardDetail.getCardSign().substring(0, 8));
                tsmOpencardInfo.setTerminalNo(tsmTerminal.getTerminalNo());
                tsmOpencardInfo.setCreatetime(new Date());
                tsmOpencardInfoMapper.insert(tsmOpencardInfo);
                cardstatusNotify.setCardNo(cardDetail.getCardNo());
            } else if (req.getOptType().equals("02")) {
                // 退卡通知
                if (orderInfo == null || orderInfo.getOrderType() != 3) {
                    return Result.fail(null, "退卡订单不存在");
                }
                // 生成退款账单
                TsmRefundBill refundBill = new TsmRefundBill();
                refundBill.setServiceorderid(orderInfo.getServiceOrderId());
                refundBill.setCardNo(orderInfo.getCardNo());
                refundBill.setAppId(orderInfo.getAppId());
                refundBill.setMerchantNo(orderInfo.getMerchantNo());
                refundBill.setOrdertype("05");
                refundBill.setRefundamount(orderInfo.getAmount());
                refundBill.setRefundType("01");
                refundBill.setCreatetime(new Date());
                tsmRefundBillMapper.insertSelective(refundBill);
                cardstatusNotify.setCardNo(orderInfo.getCardNo());
                // 异步退款
                asyncRefund(orderInfo, "05");
            }
            tsmCardstatusNotifyMapper.insert(cardstatusNotify);
            return Result.ok();
        } catch (Exception e) {
            logger.error("应用通知失败 ，req={}, e", JSON.toJSONString(req), e);
            return Result.fail(null, "应用通知失败");
        }
    }

    /**
     * 异步退款
     */
    @Async
    void asyncRefund(TsmOrderInfo orderInfo, String ordertype) {
        logger.info("开始异步退款， orderInfo={}", JSON.toJSONString(orderInfo));
        // 退款
        List<TsmOrderInfo> orderInfos = tsmOrderInfoService.getRefund(orderInfo.getUserId(), orderInfo.getCardNo());
        //待退款金额
        Integer amountToBeRefund = orderInfo.getAmount();
        for (TsmOrderInfo order : orderInfos) {
            if (amountToBeRefund >0) {
                Integer refundAmount = 0;
                if (amountToBeRefund > order.getAmount()) {
                    refundAmount = order.getAmount();
                } else {
                    refundAmount = amountToBeRefund;
                }
                Map paraMap = new HashMap();
                paraMap.put("orderId",order.getServiceOrderId());
                paraMap.put("amount",refundAmount);
                paraMap.put("applyCityCode", order.getCityCode());
                Map retMap = cmpayService.refund(paraMap);
                String retCode = retMap.get("returnCode").toString();
                String retMsg = retMap.get("message").toString();
                logger.info("退款返回数据retMap={}", JSON.toJSONString(retMap));
                // 创建退款订单
                TsmUserInfo userInfo = tsmUserInfoMapper.selectByPrimaryKey(orderInfo.getUserId());
                TsmRefundOrder refundOrder = new TsmRefundOrder();
                refundOrder.setServiceOrderId(order.getServiceOrderId());
                refundOrder.setCityCode(orderInfo.getCityCode());
                refundOrder.setAreaCode(orderInfo.getAreaCode());
                refundOrder.setCardSpecies(orderInfo.getCardSpecies());
                refundOrder.setServicetype(ordertype);
                refundOrder.setMobile(userInfo.getMobile());
                refundOrder.setAppId(order.getAppId());
                refundOrder.setMerchantNo(order.getMerchantNo());
                refundOrder.setRefundamount(refundAmount);
                refundOrder.setCreatetime(new Date());
                refundOrder.setRefundret(retCode.equals("000000") ? "00" : "01");
                tsmRefundOrderMapper.insertSelective(refundOrder);
                amountToBeRefund = amountToBeRefund - order.getAmount();
            }
        }

    }
}
