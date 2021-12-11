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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
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
            tsmCardstatusNotifyMapper.insert(cardstatusNotify);
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
                return Result.ok();
            } else if (req.getOptType().equals("02")) {
                // 退卡通知
                TsmOrderInfo tsmOrderInfo = tsmOrderInfoService.getOrder(req.getServiceOrderId());
                if (tsmOrderInfo == null || tsmOrderInfo.getOrderType() != 3) {
                    return Result.fail(null, "退卡订单不存在");
                }
                // 创建退款订单
                TsmUserInfo userInfo = tsmUserInfoMapper.selectByPrimaryKey(orderInfo.getUserId());
                TsmRefundOrder refundOrder = new TsmRefundOrder();
                refundOrder.setServiceOrderId(req.getServiceOrderId());
                refundOrder.setCityCode(orderInfo.getCityCode());
                refundOrder.setAreaCode(orderInfo.getAreaCode());
                refundOrder.setCardSpecies(orderInfo.getCardSpecies());
                refundOrder.setServicetype("05");
                refundOrder.setMobile(userInfo.getMobile());
                refundOrder.setAppId(req.getAppId());
                refundOrder.setMerchantNo(req.getMerchantNo());
                refundOrder.setRefundamount(orderInfo.getAmount());
                refundOrder.setCreatetime(new Date());
                // 退款
                Map paraMap = new HashMap();
                paraMap.put("orderId",tsmOrderInfo.getServiceOrderId());
                paraMap.put("amount",tsmOrderInfo.getAmount());
                Map retMap = cmpayService.refund(paraMap);
                String retCode = retMap.get("returnCode").toString();
                String retMsg = retMap.get("message").toString();
                if("000000".equals(retCode)){
                    refundOrder.setRefundret("00");
                } else {
                    refundOrder.setRefundret("01");
                }
                tsmRefundOrderMapper.insert(refundOrder);
                return Result.ok();
            }
        } catch (Exception e) {
            logger.error("应用通知失败 ，req={}", JSON.toJSONString(req));
            return Result.fail(null, "应用通知失败");
        }
        return Result.ok();
    }
}
