package com.heyue.hbcxservice.service.impl;

import cn.com.heyue.entity.*;
import cn.com.heyue.mapper.TsmOrderInfoMapper;
import cn.com.heyue.mapper.TsmPayOrderMapper;
import cn.com.heyue.mapper.TsmRefundOrderMapper;
import cn.com.heyue.utils.DateUtils;
import cn.com.heyue.utils.Md5Encrypt;
import cn.com.heyue.utils.OKHttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.cmpay.CmpayService;
import com.heyue.hbcxservice.message.request.CmpayNotifyReq;
import com.heyue.hbcxservice.message.request.OrderApplyReq;
import com.heyue.hbcxservice.message.request.OrderReFundReq;
import com.heyue.hbcxservice.message.response.OrderApplyRes;
import com.heyue.hbcxservice.message.response.PayOrderRes;
import com.heyue.hbcxservice.service.TsmOrderInfoService;
import com.heyue.hbcxservice.service.TsmUserInfoService;
import com.heyue.utils.GenerateIdUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TsmOrderInfoServiceImpl implements TsmOrderInfoService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass()); // 日志类

    @Autowired
    private TsmOrderInfoMapper tsmOrderInfoMapper;

    @Autowired
    private TsmPayOrderMapper tsmPayOrderMapper;

    @Autowired
    private CmpayService cmpayService;

    @Autowired
    private TsmUserInfoService tsmUserInfoService;


    @Autowired
    private TsmRefundOrderMapper tsmRefundOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderApplyRes> orderApply(OrderApplyReq orderApplyReq) {
        try {
            TsmOrderInfo tsmOrderInfo = new TsmOrderInfo();
            BeanUtils.copyProperties(orderApplyReq, tsmOrderInfo);

            String payChannel = orderApplyReq.getPayChannel(); // 支付方式
            Integer orderType = getOrderType(orderApplyReq.getServiceType()); // 订单类型
            Integer amount = Integer.valueOf(orderApplyReq.getAmount());// 交易金额
            int cardprice = orderApplyReq.getCardPrice() == null ? 0 : Integer.parseInt(orderApplyReq.getCardPrice());// 开卡费
            int cumAmount = orderApplyReq.getCumAmount() == null ? 0 : Integer.parseInt(orderApplyReq.getCumAmount());// 活动累计金额
            String orderId = GenerateIdUtils.getOrderId();
            tsmOrderInfo.setServiceOrderId(orderId);
            tsmOrderInfo.setCreateTime(new Date());
            tsmOrderInfo.setPayPlatNo(payChannel);
            tsmOrderInfo.setOrderType(orderType);
            tsmOrderInfo.setAmount(amount);
            tsmOrderInfo.setCardPrice(cardprice);
            tsmOrderInfo.setCumAmount(cumAmount);
            tsmOrderInfo.setMarketAmount(orderApplyReq.getMarketAmount() == null ? 0 :
                    Integer.parseInt(orderApplyReq.getMarketAmount()));
            tsmOrderInfo.setTopUpAmount(orderApplyReq.getTopUpAmount() == null ? 0 :
                    Integer.parseInt(orderApplyReq.getTopUpAmount()));
            if (tsmOrderInfo.getOrderType() == 0) {
                return Result.fail(null, "暂不支持的订单类型");
            }
            TsmUserInfo tsmUserInfo = tsmUserInfoService.getUserInfo(orderApplyReq.getUserId());
            if (tsmUserInfo == null) {
                return Result.fail(null, "用户不存在");
            }
            OrderApplyRes orderApplyRes = new OrderApplyRes();
            orderApplyRes.setServiceOrderId(orderId);
            orderApplyRes.setAmount(tsmOrderInfo.getAmount() + "");
            // 开卡圈存订单 需要支付 退卡订单不需要支付
            if (tsmOrderInfo.getOrderType() == 1 || tsmOrderInfo.getOrderType() == 2) {
                if (tsmOrderInfo.getTopUpAmount() != tsmOrderInfo.getAmount() + tsmOrderInfo.getMarketAmount()) {
                    logger.error("下单失败，充值金额不等于交易金额，orderApplyReq={}", JSON.toJSONString(orderApplyReq));
                    return Result.fail(null, "充值金额错误");
                }
                TsmPayOrder tsmPayOrder = new TsmPayOrder();
                // 全额权益金支付
                if (tsmOrderInfo.getAmount() == 0 && tsmOrderInfo.getMarketAmount() > 0) {
                    logger.info("全额权益金支付，不需要调用和包支付");
                    tsmPayOrder.setPayRet("03");
                } else {
                    // 需要现金支付，请求和包支付
                    Map<String, Object> cmPayMap = new HashMap<>();
                    cmPayMap.put("orderId", orderId);
                    cmPayMap.put("mobile", tsmUserInfo.getMobile());
                    cmPayMap.put("amount", tsmOrderInfo.getAmount());
                    cmPayMap.put("orderDate", DateUtils.format(new Date(), DateUtils.FORMAT_DATE));
                    cmPayMap.put("applyCityCode", tsmOrderInfo.getCityCode());
                    Map retMap = cmpayService.pay(cmPayMap);
                    logger.info("retMap={}", JSON.toJSONString(retMap));
                    String retCode = retMap.get("returnCode").toString();
                    String retMsg = retMap.get("message").toString();
                    if ("000000".equals(retCode)) {
                        tsmOrderInfo.setPayParm(retMap.get("payparm").toString());
                        orderApplyRes.setPayparm(retMap.get("payparm").toString());
                    } else {
                        logger.error("和包支付下单失败，retMap={}", JSON.toJSONString(retMap));
                        return Result.fail(null, retMsg);
                    }
                    tsmPayOrder.setPayRet("03");
                }
                BeanUtils.copyProperties(tsmOrderInfo, tsmPayOrder);
                tsmPayOrder.setOrderType(orderApplyReq.getServiceType());
                tsmPayOrder.setPayAmount(tsmOrderInfo.getAmount());
                tsmPayOrder.setMobile(tsmUserInfo.getMobile());
                tsmPayOrder.setCreateTime(new Date());
                int payCount = tsmPayOrderMapper.insertSelective(tsmPayOrder);
                if (payCount != 1) {
                    logger.error("下单失败，orderApplyReq={}", JSON.toJSONString(orderApplyReq));
                    return Result.fail(null, "下单失败");
                }
            }
            int count = tsmOrderInfoMapper.insert(tsmOrderInfo);
            if (count != 1) {
                logger.error("下单失败，orderApplyReq={}", JSON.toJSONString(orderApplyReq));
                return Result.fail(null, "下单失败");
            }
            return Result.ok(orderApplyRes);
        } catch (Exception e) {
            logger.error("下单失败，orderApplyReq={}, {}", JSON.toJSONString(orderApplyReq), e);
            return Result.fail(null, "下单失败");
        }

    }

    @Override
    public Result<String> orderRefund(OrderReFundReq req) {
        TsmOrderInfo orderInfo = getOrder(req.getServiceOrderId());
        Map paraMap = new HashMap();
        paraMap.put("orderId",req.getServiceOrderId());
        paraMap.put("amount",req.getAmount());
        paraMap.put("applyCityCode", orderInfo.getCityCode());
        Map retMap = cmpayService.refund(paraMap);
        String retCode = retMap.get("returnCode").toString();
        String retMsg = retMap.get("message").toString();
        logger.info("退款返回数据retMap={}", JSON.toJSONString(retMap));
        // 创建退款订单
        TsmUserInfo userInfo = tsmUserInfoService.getUserInfo(orderInfo.getUserId());
        TsmRefundOrder refundOrder = new TsmRefundOrder();
        refundOrder.setServiceOrderId(orderInfo.getServiceOrderId());
        refundOrder.setCityCode(orderInfo.getCityCode());
        refundOrder.setAreaCode(orderInfo.getAreaCode());
        refundOrder.setCardSpecies(orderInfo.getCardSpecies());
        refundOrder.setServicetype("05");
        refundOrder.setMobile(userInfo.getMobile());
        refundOrder.setAppId(orderInfo.getAppId());
        refundOrder.setMerchantNo(orderInfo.getMerchantNo());
        refundOrder.setRefundamount(req.getAmount());
        refundOrder.setCreatetime(new Date());
        refundOrder.setRefundret(retCode.equals("000000") ? "00" : "01");
        tsmRefundOrderMapper.insertSelective(refundOrder);
        if (retCode.equals("000000")) {
            return Result.ok();
        } else {
            return Result.fail(null, retMsg);
        }
    }

    @Override
    public TsmOrderInfo getOrder(String serviceOrderId) {
        return tsmOrderInfoMapper.selectByPrimaryKey(serviceOrderId);
    }

    @Override
    public List<TsmOrderInfo> getRefund(String userId, String cardNo) {
        return tsmOrderInfoMapper.getRefund(userId, cardNo);
    }

    @Override
    public Result<PayOrderRes> getPayOrder(String serviceOrderId) {
        PayOrderRes payOrder = new PayOrderRes();
        try {
            TsmOrderInfo tsmOrderInfo = tsmOrderInfoMapper.selectByPrimaryKey(serviceOrderId);
            TsmPayOrder tsmPayOrder = tsmPayOrderMapper.selectByServiceOrderId(serviceOrderId);
            if (tsmOrderInfo == null || tsmPayOrder == null) {
                return Result.fail(null, "订单号不存在");
            }
            // 如果未支付，去和包查支付状态
            if (tsmPayOrder.getPayRet().equals("03")) {
                Map<String, Object> paraMap = new HashMap<>();
                paraMap.put("orderId", serviceOrderId);
                paraMap.put("applyCityCode", tsmOrderInfo.getCityCode());
                Map retMap = cmpayService.query(paraMap);
                String retCode = retMap.get("returnCode").toString();
                String retMsg = retMap.get("message").toString();
                // 查到支付成功，更新支付状态
                if (StringUtils.equals(retCode, "000000") && retMap.get("status").toString().equals("SUCCESS")) {
                    tsmPayOrder.setPayRet("01");
                    tsmPayOrderMapper.updateByPrimaryKey(tsmPayOrder);
                    payOrder.setPayDate(DateUtils.format(new Date(), "yyyyMMddHHmmss"));
                }
            }
            payOrder.setServiceOrderId(serviceOrderId);
            payOrder.setCardNo(tsmOrderInfo.getCardNo());
            payOrder.setOrderType(tsmPayOrder.getOrderType());
            payOrder.setAmount(String.valueOf(tsmOrderInfo.getAmount()));
            payOrder.setPayRet(tsmPayOrder.getPayRet());
            payOrder.setCreatTime(DateUtils.dateToStr(tsmOrderInfo.getCreateTime(), DateUtils.FORMAT_FULL));
        } catch (Exception e) {
            logger.error("查询订单失败，serviceOrderId={}，{}", serviceOrderId, e);
            return Result.fail(null, "查询订单失败");
        }
        return Result.ok(payOrder);
    }

    @Override
    public int updateOrder(TsmOrderInfo tsmOrderInfo) {
        return tsmOrderInfoMapper.updateByPrimaryKeySelective(tsmOrderInfo);
    }

    @Override
    public int updatePayOrder(TsmPayOrder tsmPayOrder) {
        return tsmPayOrderMapper.updateByPrimaryKeySelective(tsmPayOrder);
    }

    @Override
    public String cmpayNotify(CmpayNotifyReq notify) {
        try {
            String sign = "【和包支付异步通知】";

            String merchantId = notify.getMerchantId();
            String payNo = notify.getPayNo();
            String returnCode = notify.getReturnCode();
            String message = notify.getMessage();
            String signType = notify.getSignType();
            String type = notify.getType();
            String version = notify.getVersion();
            String amount = notify.getAmount();
            String amtItem = notify.getAmtItem();
            String bankAbbr = notify.getBankAbbr();
            String mobile = notify.getMobile();
            String orderId = notify.getOrderId();
            String payDate = notify.getPayDate();
            String accountDate = notify.getAccountDate();
            String reserved1 = notify.getReserved1();
            String reserved2 = notify.getReserved2();
            String status = notify.getStatus();
            String orderDate = notify.getOrderDate();
            String fee = notify.getFee();
            String serverCert = notify.getServerCert();
            String hmac = notify.getHmac();

            logger.info("【和包支付异步通知】通知参数：hmac->" + hmac + ",merchantId->" + merchantId + ",payNo->" + payNo
                    + ",returnCode->" + returnCode + ",message->" + message + ",signType->" + signType + ",type->" + type
                    + ",version->" + version + ",amount->" + amount + ",amtItem->" + amtItem + ",bankAbbr->" + bankAbbr
                    + ",mobile->" + mobile + ",orderId->" + orderId + ",payDate->" + payDate + ",accountDate->"
                    + accountDate + ",reserved1->" + reserved1 + ",reserved2->" + reserved2 + ",status->" + status
                    + ",orderDate->" + orderDate + ",fee->" + fee + ",serverCert->" + serverCert + ",hmac->" + hmac);

            if (StringUtils.isBlank(hmac)) {
                logger.error("{}签名为空", sign);
                return "FAILED";
            }
            if (StringUtils.isBlank(orderId)) {
                logger.error("{}商户订单号为空", sign);
                return "FAILED";
            }
            if (StringUtils.isNotBlank(message)) {
                message = URLDecoder.decode(message, "utf-8");
            }

            TsmOrderInfo order = tsmOrderInfoMapper.selectByPrimaryKey(orderId);
            if (null == order) {
                logger.error("{}商户订单号不存在", sign);
                return "FAILED";
            }
            TsmPayOrder payOrder = tsmPayOrderMapper.selectByServiceOrderId(orderId);
            logger.info("payOrder={}", JSON.toJSONString(payOrder));
            if (payOrder.getPayRet().equals("01")) {
                logger.info("{}商户订单已支付成功", sign);
                return "SUCCESS";
            }
            // 拼接签名原始报文
            StringBuffer signBuf = new StringBuffer();
            signBuf.append(StringUtils.isNotBlank(merchantId) ? merchantId : "");
            signBuf.append(StringUtils.isNotBlank(payNo) ? payNo : "");
            signBuf.append(StringUtils.isNotBlank(returnCode) ? returnCode : "");
            signBuf.append(StringUtils.isNotBlank(message) ? message : "");
            signBuf.append(StringUtils.isNotBlank(signType) ? signType : "");
            signBuf.append(StringUtils.isNotBlank(type) ? type : "");
            signBuf.append(StringUtils.isNotBlank(version) ? version : "");
            signBuf.append(StringUtils.isNotBlank(amount) ? amount : "");
            signBuf.append(StringUtils.isNotBlank(amtItem) ? amtItem : "");
            signBuf.append(StringUtils.isNotBlank(bankAbbr) ? bankAbbr : "");
            signBuf.append(StringUtils.isNotBlank(mobile) ? mobile : "");
            signBuf.append(StringUtils.isNotBlank(orderId) ? orderId : "");
            signBuf.append(StringUtils.isNotBlank(payDate) ? payDate : "");
            signBuf.append(StringUtils.isNotBlank(accountDate) ? accountDate : "");
            signBuf.append(StringUtils.isNotBlank(reserved1) ? reserved1 : "");
            signBuf.append(StringUtils.isNotBlank(reserved2) ? reserved2 : "");
            signBuf.append(StringUtils.isNotBlank(status) ? status : "");
            signBuf.append(StringUtils.isNotBlank(orderDate) ? orderDate : "");
            signBuf.append(StringUtils.isNotBlank(fee) ? fee : "");
            logger.info("{}签名源串:{}", sign, signBuf.toString());

            TsmCmpayConfig tsmCmpayConfig = cmpayService.getPayConfig(order.getCityCode());
            String sign_succ = Md5Encrypt.md5_hisun(signBuf.toString(), tsmCmpayConfig.getCmpayMerKey());
            logger.info("生成的签名串：" + sign_succ);
            if (!org.apache.commons.lang.StringUtils.equals(sign_succ.toUpperCase(), hmac.toUpperCase())) {
                logger.error("{}验签失败", sign);
                return "FAILED";
            }

            if (StringUtils.equals(returnCode, "000000")) {
                if (StringUtils.equals(status, "SUCCESS")) {
                    payOrder.setPayRet("01");
                    payOrder.setTradeTime(new Date());

                } else if (StringUtils.equals(status, "FAILED")) {
                    payOrder.setPayRet("02");
                }
                payOrder.setPayPlatNo(payNo);
                payOrder.setPayNotifyTime(new Date());
                updatePayOrder(payOrder);
                // 通知和包出行
                OKHttpClientUtils.postJson(tsmCmpayConfig.getThirdNotifyUrl(), sign, JSON.toJSONString(notify));
            }
        } catch (Exception e) {
            logger.error("支付通知异常，notify={}, {}", JSON.toJSONString(notify), e);
        }
        return "FAILED";
    }

    private Integer getOrderType(String payChannel) {
        switch (payChannel) {
            case "01":
                return 1;
            case "02":
                return 2;
            case "05":
                return 3;
            case "06":
                return 4;
            default:
                return 0;
        }

    }
}
