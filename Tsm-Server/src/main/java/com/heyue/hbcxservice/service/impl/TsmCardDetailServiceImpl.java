package com.heyue.hbcxservice.service.impl;

import cn.com.heyue.entity.*;
import cn.com.heyue.mapper.*;
import cn.com.heyue.utils.Base64Utils;
import cn.com.heyue.utils.DateUtils;
import com.alibaba.fastjson.JSON;
import com.heyue.bean.Result;
import com.heyue.cityservice.message.request.CardActiveReq;
import com.heyue.cityservice.message.request.CardActiveSubmitReq;
import com.heyue.cityservice.message.request.CardTrapReq;
import com.heyue.cityservice.message.request.CardTrapSubmitReq;
import com.heyue.cityservice.message.response.CardActiveRes;
import com.heyue.cityservice.message.response.CardActiveSubmitRes;
import com.heyue.cityservice.message.response.CardTrapRes;
import com.heyue.cityservice.message.response.CardTrapSubmitRes;
import com.heyue.cityservice.service.CityService;
import com.heyue.hbcxservice.message.request.*;
import com.heyue.hbcxservice.message.response.*;
import com.heyue.hbcxservice.service.TsmCardDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TsmCardDetailServiceImpl implements TsmCardDetailService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass()); // 日志类

    @Autowired
    private TsmCardDetailMapper tsmCardDetailMapper;

    @Autowired
    private TsmPayOrderMapper tsmPayOrderMapper;

    @Autowired
    private TsmOrderInfoMapper tsmOrderInfoMapper;

    @Autowired
    private TsmCardstatusNotifyMapper tsmCardstatusNotifyMapper;

    @Autowired
    private TsmTerminalMpper tsmTerminalMpper;

    @Autowired
    private CityService cityService;

    @Override
    public Result<GetOpenCardFileRes> getOpenCardFile(GetOpenCardFileReq req) {
        GetOpenCardFileRes openCardFileRes = new GetOpenCardFileRes();
        try {
            TsmPayOrder tsmPayOrder = tsmPayOrderMapper.selectByServiceOrderId(req.getServiceOrderId());
            if (tsmPayOrder == null || !tsmPayOrder.getPayRet().equals("01")) {
                return Result.fail(null, "订单不存在或未完成支付");
            }
            TsmOrderInfo orderInfo = tsmOrderInfoMapper.selectByPrimaryKey(req.getServiceOrderId());
            if (orderInfo.getOrderType() != 1) {
                return Result.fail(null, "该订单不是开卡订单");
            }
            TsmCardDetail cardDetail = tsmCardDetailMapper.selectOneByServiceOrderId(req.getServiceOrderId());
            if (cardDetail == null) {
                cardDetail = tsmCardDetailMapper.selectOne(orderInfo.getCityCode(), orderInfo.getAreaCode(),
                        orderInfo.getCardSpecies());
                if (cardDetail == null) {
                    return Result.fail(null, "无可用卡数据");
                }
                cardDetail.setServiceOrderId(req.getServiceOrderId());
                cardDetail.setCardStatus("2");
                cardDetail.setOutDepositTime(new Date());
                tsmCardDetailMapper.updateByPrimaryKeySelective(cardDetail);
            }
            openCardFileRes.setCardOpeningData(Base64Utils.getBase64(JSON.toJSONString(cardDetail)));
        } catch (Exception e) {
            logger.error("获取开卡文件失败 ，req={}，{}", JSON.toJSONString(req), e);
            return Result.fail(null, "获取开卡文件失败");
        }
        return Result.ok(openCardFileRes);
    }

    @Override
    public Result<TransferApplyRes> transferApply(TransferApplyReq req) {
        TransferApplyRes transferApplyRes = new TransferApplyRes();
        try {
            TsmPayOrder payOrder = tsmPayOrderMapper.selectByServiceOrderId(req.getServiceOrderId());
            if (payOrder == null || !payOrder.getPayRet().equals("01")) {
                return Result.fail(null, "订单不存在或未完成支付");
            }
            TsmOrderInfo orderInfo = tsmOrderInfoMapper.selectByPrimaryKey(req.getServiceOrderId());
            if (orderInfo == null) {
                return Result.fail(null, "订单不存在");
            }
            if (orderInfo.getOrderType() != 1 && orderInfo.getOrderType() != 2) {
                return Result.fail(null, "订单类型错误");
            }
            TsmCardstatusNotify cardstatusNotify = tsmCardstatusNotifyMapper.selectByCardNo(req.getCard_no());
            if (cardstatusNotify == null) {
                return Result.fail(null, "未开卡成功");
            }
            TsmTerminal tsmTerminal = tsmTerminalMpper.selectByCityCode(payOrder.getCityCode());
            if (tsmTerminal == null) {
                return Result.fail(null, "终端机编号不存在");
            }
            CardTrapReq cardTrapReq = new CardTrapReq();
//            cardTrapReq.setOrder_no(req.getServiceOrderId());
            cardTrapReq.setCard_no(req.getCard_no());
            cardTrapReq.setIssue_inst(req.getIssue_inst());
            cardTrapReq.setTerminal_code(tsmTerminal.getTerminalNo());
            cardTrapReq.setCard_balance(req.getCard_balance());
            cardTrapReq.setCard_species(cardstatusNotify.getCardSpecies());
            cardTrapReq.setRegion_code(cardstatusNotify.getAreaCode());
            cardTrapReq.setCard_type(req.getCard_type());
            cardTrapReq.setAlgorithm_id(req.getAlgorithm_id());
            cardTrapReq.setMac(req.getMac());
            cardTrapReq.setMoney(orderInfo.getTopUpAmount() + "");
            cardTrapReq.setCard_transaction_num(req.getCard_transaction_num());
            cardTrapReq.setRandom(req.getRandom());
//            cardTrapReq.setMerchant_num(payOrder.getMerchantNo());
            cardTrapReq.setTransaction_datetime(DateUtils.format(new Date(),DateUtils.FORMAT_TIME));
            CardTrapRes cardTrapRes = cityService.cardTrap(cardTrapReq);
            if (cardTrapRes == null) {
                logger.error("调用城市圈存申请失败 ，cardTrapReq={}", JSON.toJSONString(cardTrapReq));
                return Result.fail(null, "调用城市圈存申请失败");
            }
            BeanUtils.copyProperties(cardTrapRes, transferApplyRes);
        } catch (Exception e) {
            logger.error("圈存申请失败 ，req={}，{}", JSON.toJSONString(req), e);
            return Result.fail(null, "圈存申请失败");
        }
        return Result.ok(transferApplyRes);
    }

    @Override
    public Result<TransferSubmitRes> transferSubmit(TransferSubmitReq req) {
        TransferSubmitRes transferSubmitRes = new TransferSubmitRes();
        try {
            TsmPayOrder payOrder = tsmPayOrderMapper.selectByServiceOrderId(req.getServiceOrderId());
            if (payOrder == null || !payOrder.getPayRet().equals("01")) {
                return Result.fail(null, "订单不存在或未完成支付");
            }
            TsmOrderInfo orderInfo = tsmOrderInfoMapper.selectByPrimaryKey(req.getServiceOrderId());
            if (orderInfo == null) {
                return Result.fail(null, "订单不存在");
            }
            if (orderInfo.getOrderType() != 1 && orderInfo.getOrderType() != 2) {
                return Result.fail(null, "订单类型错误");
            }
            TsmCardstatusNotify cardstatusNotify = tsmCardstatusNotifyMapper.selectByCardNo(req.getCard_no());
            if (cardstatusNotify == null) {
                return Result.fail(null, "未开卡成功");
            }
            TsmTerminal tsmTerminal = tsmTerminalMpper.selectByCityCode(payOrder.getCityCode());
            if (tsmTerminal == null) {
                return Result.fail(null, "终端机编号不存在");
            }
            CardTrapSubmitReq cardTrapSubmitReq = new CardTrapSubmitReq();
//            cardTrapSubmitReq.setOrder_no(req.getServiceOrderId());
            cardTrapSubmitReq.setCard_no(req.getCard_no());
//            cardTrapSubmitReq.setMerchant_num(payOrder.getMerchantNo());
            cardTrapSubmitReq.setIssue_inst(req.getIssue_inst());
            cardTrapSubmitReq.setTerminal_code(tsmTerminal.getTerminalNo());
            cardTrapSubmitReq.setRet_status(req.getRet_status());
            cardTrapSubmitReq.setTAC(req.getTAC());
            cardTrapSubmitReq.setTransaction_datetime(DateUtils.format(new Date(),DateUtils.FORMAT_TIME));
            CardTrapSubmitRes cardTrapSubmitRes = cityService.cardTrapSubmit(cardTrapSubmitReq);
            if (cardTrapSubmitRes == null) {
                logger.error("调用城市圈存申请提交失败 ，cardTrapSubmitReq={}", JSON.toJSONString(cardTrapSubmitReq));
                return Result.fail(null, "调用城市圈存申请提交失败");
            }
            BeanUtils.copyProperties(cardTrapSubmitRes, transferSubmitRes);
        } catch (Exception e) {
            logger.error("圈存申请提交失败 ，req={}，{}", JSON.toJSONString(req), e);
            return Result.fail(null, "圈存申请提交失败");
        }
        return Result.ok(transferSubmitRes);
    }

    @Override
    public Result<UserCardActiveRes> userCardActive(UserCardActiveReq req) {
        UserCardActiveRes userCardActiveRes = new UserCardActiveRes();
        try {
            TsmPayOrder payOrder = tsmPayOrderMapper.selectByServiceOrderId(req.getServiceOrderId());
            if (payOrder == null || !payOrder.getPayRet().equals("01")) {
                return Result.fail(null, "订单不存在或未完成支付");
            }
            TsmOrderInfo orderInfo = tsmOrderInfoMapper.selectByPrimaryKey(req.getServiceOrderId());
            if (orderInfo == null) {
                return Result.fail(null, "订单不存在");
            }
            if (orderInfo.getOrderType() != 1 && orderInfo.getOrderType() != 2) {
                return Result.fail(null, "订单类型错误");
            }
            TsmCardstatusNotify cardstatusNotify = tsmCardstatusNotifyMapper.selectByCardNo(req.getCard_no());
            if (cardstatusNotify == null) {
                return Result.fail(null, "未开卡成功");
            }
            TsmTerminal tsmTerminal = tsmTerminalMpper.selectByCityCode(payOrder.getCityCode());
            if (tsmTerminal == null) {
                return Result.fail(null, "终端机编号不存在");
            }
            CardActiveReq cardActiveReq = new CardActiveReq();
//            cardActiveReq.setOrder_no(req.getServiceOrderId());
            cardActiveReq.setCard_no(req.getCard_no());
            cardActiveReq.setIssue_inst(req.getIssue_inst());
//            cardActiveReq.setMerchant_num(payOrder.getMerchantNo());
            cardActiveReq.setTerminal_code(tsmTerminal.getTerminalNo());
            cardActiveReq.setCard_species(cardstatusNotify.getCardSpecies());
            cardActiveReq.setRegion_code(cardstatusNotify.getAreaCode());
            cardActiveReq.setCard_type(req.getCard_type());
            cardActiveReq.setAlgorithm_id(req.getAlgorithm_id());
            cardActiveReq.setRandom(req.getRandom());
            CardActiveRes cardActiveRes = cityService.cardActive(cardActiveReq);
            if (cardActiveRes == null) {
                logger.error("调用城市激活申请失败 ，cardActiveReq={}", JSON.toJSONString(cardActiveReq));
                return Result.fail(null, "调用城市激活申请失败");
            }
            BeanUtils.copyProperties(cardActiveRes, userCardActiveRes);
        } catch (Exception e) {
            logger.error("激活申请失败 ，req={}，{}", JSON.toJSONString(req), e);
            return Result.fail(null, "激活申请失败");
        }
        return Result.ok(userCardActiveRes);
    }

    @Override
    public Result<UserCardActiveSubmitRes> userCardActiveSubmit(UserCardActiveSubmitReq req) {
        UserCardActiveSubmitRes userCardActiveSubmitRes = new UserCardActiveSubmitRes();
        try {
            TsmPayOrder payOrder = tsmPayOrderMapper.selectByServiceOrderId(req.getServiceOrderId());
            if (payOrder == null || !payOrder.getPayRet().equals("01")) {
                return Result.fail(null, "订单不存在或未完成支付");
            }
            TsmOrderInfo orderInfo = tsmOrderInfoMapper.selectByPrimaryKey(req.getServiceOrderId());
            if (orderInfo == null) {
                return Result.fail(null, "订单不存在");
            }
            if (orderInfo.getOrderType() != 1 && orderInfo.getOrderType() != 2) {
                return Result.fail(null, "订单类型错误");
            }
            TsmCardstatusNotify cardstatusNotify = tsmCardstatusNotifyMapper.selectByCardNo(req.getCard_no());
            if (cardstatusNotify == null) {
                return Result.fail(null, "未开卡成功");
            }
            TsmTerminal tsmTerminal = tsmTerminalMpper.selectByCityCode(payOrder.getCityCode());
            if (tsmTerminal == null) {
                return Result.fail(null, "终端机编号不存在");
            }
            CardActiveSubmitReq cardActiveSubmitReq = new CardActiveSubmitReq();
//            cardActiveSubmitReq.setOrder_no(req.getServiceOrderId());
            cardActiveSubmitReq.setCard_no(req.getCard_no());
            cardActiveSubmitReq.setIssue_inst(req.getIssue_inst());
//            cardActiveSubmitReq.setMerchant_num(payOrder.getMerchantNo());
            cardActiveSubmitReq.setTerminal_code(tsmTerminal.getTerminalNo());
            cardActiveSubmitReq.setRet_status(req.getRet_status());
            cardActiveSubmitReq.setTransaction_datetime(DateUtils.format(new Date(),DateUtils.FORMAT_TIME));
            CardActiveSubmitRes cardActiveSubmitRes = cityService.cardActiveSubmit(cardActiveSubmitReq);
            if (cardActiveSubmitRes == null) {
                logger.error("调用城市激活申请提交失败 ，cardActiveSubmitReq={}", JSON.toJSONString(cardActiveSubmitReq));
                return Result.fail(null, "调用城市激活申请提交失败");
            }
            BeanUtils.copyProperties(cardActiveSubmitRes, userCardActiveSubmitRes);
        } catch (Exception e) {
            logger.error("激活申请提交失败 ，req={}，{}", JSON.toJSONString(req), e);
            return Result.fail(null, "激活申请提交失败");
        }
        return Result.ok(userCardActiveSubmitRes);
    }
}
