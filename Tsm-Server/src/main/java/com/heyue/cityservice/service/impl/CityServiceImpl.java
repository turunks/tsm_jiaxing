package com.heyue.cityservice.service.impl;


import cn.com.heyue.entity.TsmCardapduApply;
import cn.com.heyue.entity.TsmTerminalOrder;
import cn.com.heyue.mapper.TsmCardapduApplyMapper;
import cn.com.heyue.mapper.TsmTerminalOrderMapper;
import cn.com.heyue.utils.HttpRequestUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.heyue.bean.Result;
import com.heyue.cityservice.message.request.*;
import com.heyue.cityservice.message.response.*;
import com.heyue.cityservice.service.CityService;
import com.heyue.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private static Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

    @Autowired
    TsmCardapduApplyMapper tsmCardapduApplyMapper; // 卡指令请求记录

    @Autowired
    TsmTerminalOrderMapper tsmTerminalOrderMapper; // 终端交易记录订单

    // 1.交易信息查询
    @Override
    public Result selTradeInfoR(TradeInfoReq tradeInfoReq) {
        // 向城市服务发送交易查询
        String req = JSON.toJSONString(tradeInfoReq);
        logger.info("发送城市服务发送交易查询报文:{}", req);
        try {
            String res = HttpRequestUtils.doPost(Constant.SEL_TRADEINFO_URL, req);
            logger.info("返回城市服务发送交易查询报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回城市服务发送交易查询为空");
                return Result.fail();
            }
            TradeInfoRes tradeInfoRes = JSON.parseObject(res, TradeInfoRes.class);
            return Result.ok(tradeInfoRes);
        } catch (Exception e) {
            logger.error("返回城市服务发送交易查询异常:{}", e);
            return Result.fail();
        }
    }

    // 2.卡激活请求
    @Override
    public Result cardActive(CardActiveReq cardActiveReq) {
        // 向城市服务发送卡激活请求
        String req = JSON.toJSONString(cardActiveReq);
        logger.info("发送城市服务卡激活报文:{}", req);
        try {
            String res = HttpRequestUtils.doPost(Constant.CARD_ACTIVE_URL, req);
            logger.info("返回城市服务卡激活报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回城市服务卡激活信息为空");
                return Result.fail();
            }
            CardActiveRes cardActiveRes = JSON.parseObject(res, CardActiveRes.class);
            // 提交成功插入卡指令请求记录表，终端交易数据存入 终端交易订单表

            //
            String cityCode = cardActiveReq.getCity_code();
            String regionCode = cardActiveReq.getRegion_code();
            String cardSpecies = cardActiveReq.getCard_species();
            String terminalCode = cardActiveReq.getTerminal_code();
            String cardNo = cardActiveReq.getCard_no();
            String transactionNum = cardActiveRes.getTransaction_num();
            String apdu = cardActiveRes.getApdu();

            TsmCardapduApply record =new TsmCardapduApply();
            record.setCityCode(cityCode);
            record.setAreaCode(regionCode);
            record.setCardSpecies(cardSpecies);
            record.setTerminalNo(terminalCode);
//            record.setTsmNo(cardActiveReq.getCity_code());
            record.setCardNo(cardNo);
            record.setCardOptype("01");
            record.setTransactionNum(transactionNum);
            record.setTransactionDatetime(new Date());
            record.setIssubmit("00");
            record.setSubmittime(new Date());
            record.setApdu(apdu);
            tsmCardapduApplyMapper.insertSelective(record);

//            tsmTerminalOrderMapper.insertSelective();
            TsmTerminalOrder record_one =new TsmTerminalOrder();
            record_one.setTransactionNum(transactionNum);
            record_one.setTransactionType("2");
            record_one.setCityCode(cityCode);
            record_one.setAreaCode(regionCode);
            record_one.setCardSpecies(cardSpecies);
            record_one.setTerminalNo(terminalCode);
//            record_one.setTsmNo();
            record_one.setCreatetime(new Date());
            tsmTerminalOrderMapper.insertSelective(record_one);

            return Result.ok(cardActiveRes);
        } catch (Exception e) {
            logger.error("卡激活请求异常:{}", e);
            return Result.fail();
        }

    }

    // 3.卡激活请求提交
    @Override
    public Result cardActiveSubmit(CardActiveSubmitReq cardActiveSubmitReq) {
        // 向城市服务发送卡激活请求提交
        String req = JSON.toJSONString(cardActiveSubmitReq);
        logger.info("发送城市服务卡激活请求提交报文:{}", req);
        try {
            String res = HttpRequestUtils.doPost(Constant.CARD_ACTIVE_SUBMIT_URL, req);
            logger.info("返回城市服务卡激活请求提交报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回卡激活请求提交为空");
                return Result.fail();
            }

            // 提交成功后更新 卡指令请求记录表 的是否请求被提交 和 请求提交时间字段。
            TsmCardapduApply record =new TsmCardapduApply();
            record.setCardNo(cardActiveSubmitReq.getCard_no());
            record.setIssubmit("01");
            record.setSubmittime(new Date());
            tsmCardapduApplyMapper.updateByCardNo(record);

            return Result.ok();
        } catch (Exception e) {
            logger.error("卡激活请求提交异常:{}", e);
            return Result.fail();
        }
    }

    // 4.卡圈存请求
    @Override
    public Result cardTrap(CardTrapReq cardTrapReq) {
        // 向城市服务发送卡圈存请求
        String req = JSON.toJSONString(cardTrapReq);
        logger.info("发送卡圈存请求报文:{}", req);
        try {
            String res = HttpRequestUtils.doPost(Constant.CARD_TRAP_URL, req);
            logger.info("返回卡圈存请求报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回卡圈存请求为空");
                return Result.fail();
            }
            CardTrapRes cardTrapRes = JSON.parseObject(res, CardTrapRes.class);
            // 成功后将插入一条记录到 卡指令请求记录表，另外 会将终端交易数据存入 终端交易订单表

            //
            String cityCode = cardTrapReq.getCity_code();
            String regionCode = cardTrapReq.getRegion_code();
            String cardSpecies = cardTrapReq.getCard_species();
            String terminalCode = cardTrapReq.getTerminal_code();
            String cardNo = cardTrapReq.getCard_no();
            String transactionNum = cardTrapRes.getTransaction_num();
//            String apdu = cardTrapRes.getApdu();

            TsmCardapduApply record =new TsmCardapduApply();
            record.setCityCode(cityCode);
            record.setAreaCode(regionCode);
            record.setCardSpecies(cardSpecies);
            record.setTerminalNo(terminalCode);
//            record.setTsmNo(cardActiveReq.getCity_code());
            record.setCardNo(cardNo);
            record.setCardOptype("02");
            record.setTransactionNum(transactionNum);
            record.setTransactionDatetime(new Date());
            record.setIssubmit("00");
            record.setSubmittime(new Date());
//            record.setApdu(apdu);
            tsmCardapduApplyMapper.insertSelective(record);

//            tsmTerminalOrderMapper.insertSelective();
            TsmTerminalOrder record_one =new TsmTerminalOrder();
            record_one.setTransactionNum(transactionNum);
            record_one.setTransactionType("3");
            record_one.setCityCode(cityCode);
            record_one.setAreaCode(regionCode);
            record_one.setCardSpecies(cardSpecies);
            record_one.setTerminalNo(terminalCode);
//            record_one.setTsmNo();
            record_one.setCreatetime(new Date());
            tsmTerminalOrderMapper.insertSelective(record_one);

            return Result.ok(cardTrapRes);
        } catch (Exception e) {
            logger.error("卡圈存请求异常:{}", e);
            return Result.fail();
        }
    }

    // 5.卡圈存请求提交
    //
    @Override
    public Result cardTrapSubmit(CardTrapSubmitReq cardTrapSubmitReq) {
        // 向城市服务发送卡圈存请求提交
        String req = JSON.toJSONString(cardTrapSubmitReq);
        logger.info("发送卡圈存请求提交报文:{}", req);
        try {
            String res = HttpRequestUtils.doPost(Constant.CARD_TRAP_SUBMIT_URL, req);
            logger.info("返回卡圈存请求提交报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回卡圈存请求提交为空");
                return Result.fail();
            }
            // 如果提交成功后更新 卡指令请求记录表 的是否请求被提交和 请求提交时间字段。
            TsmCardapduApply record =new TsmCardapduApply();

            record.setCardNo(cardTrapSubmitReq.getCard_no());
            record.setIssubmit("01");
            record.setSubmittime(new Date());
            tsmCardapduApplyMapper.updateByCardNo(record);

            return Result.ok();
        } catch (Exception e) {
            logger.error("卡圈存请求提交异常:{}", e);
            return Result.fail();
        }
    }

    // 6.卡账户信息查询
    @Override
    public Result cardAccountInfo(CardAccountInfoReq cardAccountInfoReq) {
        // 向城市服务发送卡账户信息查询
        String req = JSON.toJSONString(cardAccountInfoReq);
        logger.info("发送卡账户信息查询报文:{}", req);
        try {
            String res = HttpRequestUtils.doPost(Constant.CARD_ACCOUNT_INFO_URL, req);
            logger.info("返回卡账户信息查询报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回卡账户信息为空");
                return Result.fail();
            }
            CardAccountInfoRes cardAccountInfoRes = JSON.parseObject(res, CardAccountInfoRes.class);
            return Result.ok(cardAccountInfoRes);
        } catch (Exception e) {
            logger.error("返回卡账户信息异常:{}", e);
            return Result.fail();
        }
    }

    // 7.卡消费记录查询
    @Override
    public Result cardConsumRecord(CardConsumRecordReq cardConsumRecordReq) {
        // 向城市服务发送卡消费记录查询
        String req = JSON.toJSONString(cardConsumRecordReq);
        logger.info("发送卡消费记录查询报文:{}", req);
        try {
            String res = HttpRequestUtils.doPost(Constant.CARD_CONSUM_RECORD_URL, req);
            logger.info("返回卡消费记录查询报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回卡消费记录为空");
                return Result.fail();
            }
            List<CardConsumRecordRes> cardConsumRecordResList = JSONArray.parseObject(res, List.class);
            return Result.ok(cardConsumRecordResList);
        } catch (Exception e) {
            logger.error("返回卡消费记录异常:{}", e);
            return Result.fail();
        }
    }
}
