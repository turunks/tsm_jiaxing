package com.heyue.hbcxservice.service.impl;

import cn.com.heyue.entity.TsmOrderInfo;
import cn.com.heyue.entity.TsmPayOrder;
import cn.com.heyue.entity.TsmUserInfo;
import cn.com.heyue.mapper.TsmOrderInfoMapper;
import cn.com.heyue.mapper.TsmPayOrderMapper;
import com.heyue.hbcxservice.cmpay.CmpayService;
import cn.com.heyue.utils.DateUtils;
import cn.com.heyue.utils.Md5Encrypt;
import cn.com.heyue.utils.OKHttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.request.CmpayNotifyReq;
import com.heyue.hbcxservice.message.request.OrderApplyReq;
import com.heyue.hbcxservice.message.response.OrderApplyRes;
import com.heyue.hbcxservice.message.response.PayOrderRes;
import com.heyue.hbcxservice.service.TsmOrderInfoService;
import com.heyue.hbcxservice.service.TsmUserInfoService;
import com.heyue.utils.GenerateIdUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TsmOrderInfoServiceImpl implements TsmOrderInfoService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass()); // 日志类

    /**
     * 支付商户秘钥
     */
    @Value("${CMPAY_MER_KEY}")
    private static String CMPAY_MER_KEY;

    /**
     * 和包出行支付回调地址
     */
    @Value("${HBCX_PAY_NOTIFY_URL}")
    private static String HBCX_PAY_NOTIFY_URL;

    @Autowired
    private TsmOrderInfoMapper tsmOrderInfoMapper;

    @Autowired
    private TsmPayOrderMapper tsmPayOrderMapper;

    @Autowired
    private CmpayService cmpayService;

    @Autowired
    private TsmUserInfoService tsmUserInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderApplyRes> orderApply( OrderApplyReq orderApplyReq) {
        try {
            TsmOrderInfo tsmOrderInfo = new TsmOrderInfo();
            BeanUtils.copyProperties(orderApplyReq, tsmOrderInfo);
            String orderId = GenerateIdUtils.getOrderId();
            tsmOrderInfo.setServiceOrderId(orderId);
            tsmOrderInfo.setCreateTime(new Date());
            tsmOrderInfo.setPayPlatNo(orderApplyReq.getPayChannel());
            tsmOrderInfo.setOrderType(getOrderType(orderApplyReq.getServiceType()));
            if (tsmOrderInfo.getOrderType() == 0) {
                return Result.fail(null, "暂不支持的订单类型");
            }
            TsmUserInfo tsmUserInfo = tsmUserInfoService.getUserInfo(orderApplyReq.getUserId());
            if (tsmUserInfo == null) {
                return Result.fail(null, "用户不存在");
            }
            Map<String, Object> cmPayMap = new HashMap<>();
            cmPayMap.put("orderId", orderId);
            cmPayMap.put("mobile", tsmUserInfo.getMobile());
            cmPayMap.put("amount", tsmOrderInfo.getAmount());
            cmPayMap.put("clientIp", orderId);

            cmPayMap.put("orderDate", DateUtils.format(new Date(),DateUtils.FORMAT_DATE));
            Map retMap = cmpayService.pay(cmPayMap);
            String retCode = retMap.get("returnCode").toString();
            String retMsg = retMap.get("message").toString();
            OrderApplyRes orderApplyRes = new OrderApplyRes();
            orderApplyRes.setServiceOrderId(orderId);
            orderApplyRes.setAmount(tsmOrderInfo.getAmount() + "");
            if("000000".equals(retCode)){
                orderApplyRes.setPayparm(retMap.get("payUrl").toString());
                int count = tsmOrderInfoMapper.insertSelective(tsmOrderInfo);
                TsmPayOrder tsmPayOrder = new TsmPayOrder();
                BeanUtils.copyProperties(tsmOrderInfo, tsmPayOrder);
                tsmPayOrder.setOrderType(orderApplyReq.getServiceType());
                tsmPayOrder.setPayAmount(tsmOrderInfo.getAmount());
                tsmPayOrder.setPayRet("03");
                tsmPayOrder.setMobile(tsmUserInfo.getMobile());
                tsmPayOrder.setCreateTime(new Date());
                int payCount = tsmPayOrderMapper.insertSelective(tsmPayOrder);
                if (count == 1 && payCount == 1) {
                    return Result.ok(orderApplyRes);
                } else {
                    logger.error("下单失败，orderApplyReq={}", JSON.toJSONString(orderApplyReq));
                    return Result.fail(null, "下单失败");
                }
            } else {
                logger.error(retMsg + "retMap={}", JSON.toJSONString(retMap));
                return Result.fail(null, retMsg);
            }
        } catch (Exception e) {
            logger.error("下单失败，orderApplyReq={}", JSON.toJSONString(orderApplyReq));
            return Result.fail(null, "下单失败");
        }

    }

    @Override
    public TsmOrderInfo getOrder(String serviceOrderId) {
        return tsmOrderInfoMapper.selectByPrimaryKey(serviceOrderId);
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
                Map retMap = CmpayService.query(paraMap);
                String retCode = retMap.get("returnCode").toString();
                String retMsg = retMap.get("message").toString();
                // 查到支付成功，更新支付状态
                if (StringUtils.equals(retCode, "000000") && retMap.get("status").toString().equals("SUCCESS")) {
                    tsmPayOrder.setPayRet("01");
                    tsmPayOrderMapper.updateByPrimaryKey(tsmPayOrder);
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

            String sign_succ = Md5Encrypt.md5_hisun(signBuf.toString(), CMPAY_MER_KEY);
            logger.info("生成的签名串：" + sign_succ);
            if (!org.apache.commons.lang.StringUtils.equals(sign_succ.toUpperCase(), hmac.toUpperCase())) {
                logger.error("{}验签失败", sign);
                return "FAILED";
            }

            if (StringUtils.equals(returnCode, "000000")) {
                if (StringUtils.equals(status, "SUCCESS")) {
                    payOrder.setOrderType("01");
                    payOrder.setTradeTime(new Date());

                } else if (StringUtils.equals(status, "FAILED")) {
                    payOrder.setPayOrderId("02");
                }
                payOrder.setPayPlatNo(payNo);
                payOrder.setPayNotifyTime(new Date());
                updatePayOrder(payOrder);
                // 通知和包出行
                OKHttpClientUtils.postJson(HBCX_PAY_NOTIFY_URL, sign, JSON.toJSONString(notify));
            }
        } catch (Exception e) {
            logger.error("支付通知异常，notify={}", JSON.toJSONString(notify));
        }
        return "FAILED";
    }

    private Integer getOrderType(String payChannel) {
        switch (payChannel) {
            case "01" :
                return 1;
            case "02" :
                return 2;
            case "05" :
                return 3;
            case "06" :
                return 4;
            default:
                return 0;
        }

    }
}