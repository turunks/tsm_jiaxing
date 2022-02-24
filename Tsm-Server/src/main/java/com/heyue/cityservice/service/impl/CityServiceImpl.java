package com.heyue.cityservice.service.impl;


import cn.com.heyue.entity.TsmCardapduApply;
import cn.com.heyue.entity.TsmTerminalOrder;
import cn.com.heyue.mapper.TsmCardapduApplyMapper;
import cn.com.heyue.mapper.TsmTerminalOrderMapper;
import cn.com.heyue.utils.HexStringUtils;
import cn.com.heyue.utils.HttpRequestUtils;
import cn.com.heyue.utils.RSAUtils;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.heyue.bean.TsmBaseReq;
import com.heyue.bean.TsmBaseRes;
import com.heyue.cityservice.message.request.*;
import com.heyue.cityservice.message.response.*;
import com.heyue.cityservice.service.CityService;
import com.heyue.constant.Constant;
import com.heyue.utils.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private static Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

    @Autowired
    TsmCardapduApplyMapper tsmCardapduApplyMapper; // 卡指令请求记录

    @Autowired
    TsmTerminalOrderMapper tsmTerminalOrderMapper; // 终端交易记录订单

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    // 1.交易信息查询
    @Override
    public TsmBaseRes selTradeInfoR(TradeInfoReq tradeInfoReq) {
        // 向城市服务发送交易查询
        try {
            String signRet = RSAUtils.signWithRsa2(JSON.toJSONString(tradeInfoReq).getBytes(StandardCharsets.UTF_8), Constant.TSM_LOC_PRI_KEY).replaceAll(System.getProperty("line.separator"), "");
            TsmBaseReq<TradeInfoReq> tsmBaseReq = new TsmBaseReq<>(tradeInfoReq, signRet);
            String req = JSON.toJSONString(tsmBaseReq);
            logger.info("发送城市服务发送交易查询报文:{}", req);
            String res = HttpRequestUtils.doPost(Constant.SEL_TRADEINFO_URL, req);
            logger.info("返回城市服务发送交易查询报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回城市服务发送交易查询为空");
                return TsmBaseRes.fail();
            }
            TsmBaseRes<TradeInfoRes> tsmBaseRes = JSON.parseObject(res, TsmBaseRes.class);
            return tsmBaseRes;
        } catch (Exception e) {
            logger.error("返回城市服务发送交易查询异常:{}", e);
            return TsmBaseRes.fail();
        }
    }

    // 2.卡激活请求
    @Override
    public CardActiveRes cardActive(CardActiveReq cardActiveReq) {
        // 向城市服务发送卡激活请求
        try {
            // 参数二次封装
            String card_no = cardActiveReq.getCard_no();
            String terminalCode = cardActiveReq.getTerminal_code();
            String transactionNum = IdUtil.getTransactionNum();
            // 卡指令表获取订单号,无则创建
            String cardOptype="01";
            String issubmit="00"; // 查询未提交的激活订单号
            String orderNo = selOrderNo(card_no,cardOptype,issubmit);
            if (StringUtils.isEmpty(orderNo)) {
                orderNo = getOrderNo(terminalCode, transactionNum);
            }
            cardActiveReq.setMerchant_num(Constant.MERCHANT_NO);
            cardActiveReq.setTransaction_num(transactionNum);
            cardActiveReq.setOrder_no(orderNo);
            String signRet = RSAUtils.signWithRsa2(JSON.toJSONString(cardActiveReq).getBytes(StandardCharsets.UTF_8), Constant.TSM_LOC_PRI_KEY).replaceAll(System.getProperty("line.separator"), "");
            TsmBaseReq<CardActiveReq> tsmBaseReq = new TsmBaseReq<>(cardActiveReq, signRet);
            String req = JSON.toJSONString(tsmBaseReq);
            logger.info("发送城市服务卡激活报文:{}", req);
            String res = HttpRequestUtils.doPost(Constant.CARD_ACTIVE_URL, req);
            logger.info("返回城市服务卡激活报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回城市服务卡激活信息为空");
                return null;
            }
//            TsmBaseRes tsmBaseRes = JSON.parseObject(res, TsmBaseRes.class);
            // 提交成功插入卡指令请求记录表，终端交易数据存入 终端交易订单表

            String cardSpecies = Constant.CARD_SPECIES;
            String cityCode = Constant.CITY_CODE;
            String regionCode = Constant.AREA_CODE;
            String tsmId = Constant.TSM_ID;
            String apdu = "";
            String transaction_datetime = "";

            CardActiveRes cardActiveRes = JSON.parseObject(JSON.parseObject(res).getString("data"), CardActiveRes.class);

            if (cardActiveRes != null) {
                transaction_datetime = cardActiveRes.getTransaction_datetime();
                apdu = cardActiveRes.getApdu();

                TsmCardapduApply record = new TsmCardapduApply();
                record.setCityCode(cityCode);
                record.setAreaCode(regionCode);
                record.setCardSpecies(cardSpecies);
                record.setTerminalNo(terminalCode);
                record.setTsmNo(tsmId);
                record.setCardNo(card_no);
                record.setCardOptype("01");// 卡激活
                record.setTransactionNum(transactionNum);
                record.setCityOrderNo(orderNo);
                record.setTransactionDatetime(new Date());
                record.setIssubmit("00");// 未提交
                record.setSubmittime(new Date());
                record.setApdu(apdu);
                tsmCardapduApplyMapper.insertSelective(record);

                TsmTerminalOrder record_one = new TsmTerminalOrder();
                record_one.setTransactionNum(transactionNum);
                record_one.setCityOrderNo(orderNo);
                record_one.setTransactionType("2");// 激活请求
                record_one.setCityCode(cityCode);
                record_one.setAreaCode(regionCode);
                record_one.setCardSpecies(cardSpecies);
                record_one.setTerminalNo(terminalCode);
                record_one.setTsmNo(tsmId);
                record_one.setCreatetime(new Date());
                tsmTerminalOrderMapper.insertSelective(record_one);
                return cardActiveRes;
            }
            return cardActiveRes;
        } catch (Exception e) {
            logger.error("卡激活请求异常:{}", e);
            return null;
        }

    }

    // 3.卡激活请求提交
    @Override
    public CardActiveSubmitRes cardActiveSubmit(CardActiveSubmitReq cardActiveSubmitReq) {
        // 向城市服务发送卡激活请求提交
        try {
            // 参数二次封装
            String card_no = cardActiveSubmitReq.getCard_no();
            String terminalCode = cardActiveSubmitReq.getTerminal_code();
            String transactionNum = IdUtil.getTransactionNum();
            String ret_status = cardActiveSubmitReq.getRet_status();


            // 获取订单号
            // 卡指令表获取订单号
            String cardOptype="01";
            String issubmit="00"; // 查询未提交的激活订单号
            String orderNo = selOrderNo(card_no,cardOptype,issubmit);
            String transaction_datetime = sdf.format(new Date());
            cardActiveSubmitReq.setTransaction_datetime(transaction_datetime);
            cardActiveSubmitReq.setTransaction_num(transactionNum);
            cardActiveSubmitReq.setOrder_no(orderNo);
            cardActiveSubmitReq.setMerchant_num(Constant.MERCHANT_NO);
            // 写卡状态转16进制
            ret_status = HexStringUtils.intToHexString(Integer.parseInt(ret_status), 2);
            cardActiveSubmitReq.setRet_status(ret_status);

            String signRet = RSAUtils.signWithRsa2(JSON.toJSONString(cardActiveSubmitReq).getBytes(StandardCharsets.UTF_8), Constant.TSM_LOC_PRI_KEY).replaceAll(System.getProperty("line.separator"), "");
            TsmBaseReq<CardActiveSubmitReq> tsmBaseReq = new TsmBaseReq<>(cardActiveSubmitReq, signRet);
            String req = JSON.toJSONString(tsmBaseReq);
            logger.info("发送城市服务卡激活请求提交报文:{}", req);
            String res = HttpRequestUtils.doPost(Constant.CARD_ACTIVE_SUBMIT_URL, req);
            logger.info("返回城市服务卡激活请求提交返回报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回卡激活请求提交为空");
                return null;
            }
//            TsmBaseRes<CardActiveSubmitRes> tsmBaseRes = JSON.parseObject(res, TsmBaseRes.class);
            CardActiveSubmitRes cardActiveSubmitRes = JSON.parseObject(JSON.parseObject(res).getString("data"), CardActiveSubmitRes.class);
            if (cardActiveSubmitRes != null) {
                // 提交成功后更新 卡指令请求记录表 的是否请求被提交 和 请求提交时间字段。
                TsmCardapduApply record = new TsmCardapduApply();
                record.setCardNo(card_no);
                record.setIssubmit("01");
                record.setSubmittime(new Date());
                tsmCardapduApplyMapper.updateByCardNo(record);
            }
            return cardActiveSubmitRes;
        } catch (Exception e) {
            logger.error("卡激活请求提交异常:{}", e);
            return null;
        }
    }

    // 4.卡圈存请求
    @Override
    public CardTrapRes cardTrap(CardTrapReq cardTrapReq) {
        // 向城市服务发送卡圈存请求
        try {
            // 参数二次封装
            String card_balance = cardTrapReq.getCard_balance();
            String money = cardTrapReq.getMoney();
            String card_transaction_num = cardTrapReq.getCard_transaction_num();
            card_balance = HexStringUtils.intToHexString(Integer.parseInt(card_balance), 8);
            money = HexStringUtils.intToHexString(Integer.parseInt(money), 8);
            card_transaction_num = HexStringUtils.intToHexString(Integer.parseInt(card_transaction_num), 4);
            cardTrapReq.setCard_balance(card_balance);
            cardTrapReq.setMoney(money);
            cardTrapReq.setCard_transaction_num(card_transaction_num);

            String regionCode = cardTrapReq.getRegion_code();
            String cardSpecies = cardTrapReq.getCard_species();
            String cardNo = cardTrapReq.getCard_no();
            String terminalCode = cardTrapReq.getTerminal_code();
            String transactionNum = IdUtil.getTransactionNum();

//            String cardOptype="02";
//            String orderNo = selOrderNo(cardNo,cardOptype);
//            if (StringUtils.isEmpty(orderNo)) {
            String orderNo = getOrderNo(terminalCode, transactionNum);
//            }
            cardTrapReq.setOrder_no(orderNo);
            cardTrapReq.setMerchant_num(Constant.MERCHANT_NO);
            cardTrapReq.setTransaction_num(transactionNum);
            String signRet = RSAUtils.signWithRsa2(JSON.toJSONString(cardTrapReq).getBytes(StandardCharsets.UTF_8), Constant.TSM_LOC_PRI_KEY).replaceAll(System.getProperty("line.separator"), "");
            TsmBaseReq<CardTrapReq> tsmBaseReq = new TsmBaseReq<>(cardTrapReq, signRet);
            String req = JSON.toJSONString(tsmBaseReq);
            logger.info("发送卡圈存请求报文:{}", req);
            String res = HttpRequestUtils.doPost(Constant.CARD_TRAP_URL, req);
            logger.info("返回卡圈存请求报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回卡圈存请求为空");
                return null;
            }
//            TsmBaseRes<CardTrapRes> tsmBaseRes = JSON.parseObject(res, TsmBaseRes.class);
            // 成功后将插入一条记录到 卡指令请求记录表，另外 会将终端交易数据存入 终端交易订单表
            CardTrapRes cardTrapRes = JSON.parseObject(JSON.parseObject(res).getString("data"), CardTrapRes.class);
            //

            String apdu = "";
            if (cardTrapRes != null) {
                apdu = cardTrapRes.getApdu();
                TsmCardapduApply record = new TsmCardapduApply();
                record.setCityCode(Constant.CITY_CODE);
                record.setAreaCode(regionCode);
                record.setCardSpecies(cardSpecies);
                record.setTerminalNo(terminalCode);
                record.setTsmNo(Constant.TSM_ID);
                record.setCardNo(cardNo);
                record.setCardOptype("02");
                record.setTransactionNum(transactionNum);
                record.setTransactionDatetime(new Date());
                record.setCityOrderNo(orderNo);
                record.setIssubmit("00");
                record.setSubmittime(new Date());
                record.setApdu(apdu);
                tsmCardapduApplyMapper.insertSelective(record);

                TsmTerminalOrder record_one = new TsmTerminalOrder();
                record_one.setTransactionNum(transactionNum);
                record_one.setTransactionType("3");
                record_one.setCityOrderNo(orderNo);
                record_one.setCityCode(Constant.CITY_CODE);
                record_one.setAreaCode(regionCode);
                record_one.setCardSpecies(cardSpecies);
                record_one.setTerminalNo(terminalCode);
                record_one.setCreatetime(new Date());
                tsmTerminalOrderMapper.insertSelective(record_one);
            }
            return cardTrapRes;
        } catch (Exception e) {
            logger.error("卡圈存请求异常:{}", e);
            return null;
        }
    }

    // 5.卡圈存请求提交
    //
    @Override
    public CardTrapSubmitRes cardTrapSubmit(CardTrapSubmitReq cardTrapSubmitReq) {
        // 向城市服务发送卡圈存请求提交
        try {
            String card_no = cardTrapSubmitReq.getCard_no();
            String terminalCode = cardTrapSubmitReq.getTerminal_code();
            // 写卡状态转16进制
            String ret_status = cardTrapSubmitReq.getRet_status();
            ret_status = HexStringUtils.intToHexString(Integer.parseInt(ret_status), 2);
            cardTrapSubmitReq.setRet_status(ret_status);
            String transactionNum = IdUtil.getTransactionNum();
            String transaction_datetime = sdf.format(new Date());
            cardTrapSubmitReq.setTransaction_datetime(transaction_datetime);
            String cardOptype="02";
            String issubmit="00"; // 查询未提交的圈存订单号
            String orderNo = selOrderNo(card_no,cardOptype,issubmit);
            cardTrapSubmitReq.setTransaction_num(transactionNum);
            cardTrapSubmitReq.setOrder_no(orderNo);
            cardTrapSubmitReq.setMerchant_num(Constant.MERCHANT_NO);
            String signRet = RSAUtils.signWithRsa2(JSON.toJSONString(cardTrapSubmitReq).getBytes(StandardCharsets.UTF_8), Constant.TSM_LOC_PRI_KEY).replaceAll(System.getProperty("line.separator"), "");
            TsmBaseReq<CardTrapSubmitReq> tsmBaseReq = new TsmBaseReq<>(cardTrapSubmitReq, signRet);
            String req = JSON.toJSONString(tsmBaseReq);
            logger.info("发送卡圈存请求提交报文:{}", req);
            String res = HttpRequestUtils.doPost(Constant.CARD_TRAP_SUBMIT_URL, req);
            logger.info("返回卡圈存请求提交报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回卡圈存请求提交为空");
                return null;
            }
//            TsmBaseRes<CardTrapSubmitRes> tsmBaseRes = JSON.parseObject(res, TsmBaseRes.class);
            CardTrapSubmitRes cardTrapSubmitRes = JSON.parseObject(JSON.parseObject(res).getString("data"), CardTrapSubmitRes.class);

            if (cardTrapSubmitRes != null) {
                // 如果提交成功后更新 卡指令请求记录表 的是否请求被提交和 请求提交时间字段。
                TsmCardapduApply record = new TsmCardapduApply();
                record.setCardNo(card_no);
                record.setIssubmit("01");
                record.setSubmittime(new Date());
                tsmCardapduApplyMapper.updateByCardNo(record);
            }
            return cardTrapSubmitRes;
        } catch (Exception e) {
            logger.error("卡圈存请求提交异常:{}", e);
            return null;
        }
    }

    // 6.卡账户信息查询
    @Override
    public TsmBaseRes cardAccountInfo(CardAccountInfoReq cardAccountInfoReq) {
        // 向城市服务发送卡账户信息查询
        try {
            String signRet = RSAUtils.signWithRsa2(JSON.toJSONString(cardAccountInfoReq).getBytes(StandardCharsets.UTF_8), Constant.TSM_LOC_PRI_KEY).replaceAll(System.getProperty("line.separator"), "");
            TsmBaseReq<CardAccountInfoReq> tsmBaseReq = new TsmBaseReq<>(cardAccountInfoReq, signRet);
            String req = JSON.toJSONString(tsmBaseReq);
            logger.info("发送卡账户信息查询报文:{}", req);
            String res = HttpRequestUtils.doPost(Constant.CARD_ACCOUNT_INFO_URL, req);
            logger.info("返回卡账户信息查询报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回卡账户信息为空");
                return TsmBaseRes.fail();
            }
            TsmBaseRes<CardAccountInfoRes> tsmBaseRes = JSON.parseObject(res, TsmBaseRes.class);
            return tsmBaseRes;
        } catch (Exception e) {
            logger.error("返回卡账户信息异常:{}", e);
            return TsmBaseRes.fail();
        }
    }

    // 7.卡消费记录查询
    @Override
    public TsmBaseRes cardConsumRecord(CardConsumRecordReq cardConsumRecordReq) {
        // 向城市服务发送卡消费记录查询
        try {
            String terminalCode = "6a787a640001";
            String transactionNum = IdUtil.getTransactionNum();
            cardConsumRecordReq.setIssue_inst("31048803");
            cardConsumRecordReq.setTerminal_code(terminalCode);
            cardConsumRecordReq.setTransaction_num(transactionNum);

            String signRet = RSAUtils.signWithRsa2(JSON.toJSONString(cardConsumRecordReq).getBytes(StandardCharsets.UTF_8), Constant.TSM_LOC_PRI_KEY).replaceAll(System.getProperty("line.separator"), "");
            TsmBaseReq<CardConsumRecordReq> tsmBaseReq = new TsmBaseReq<>(cardConsumRecordReq, signRet);
            String req = JSON.toJSONString(tsmBaseReq);
            logger.info("发送卡消费记录查询报文:{}", req);
            String res = HttpRequestUtils.doPost(Constant.CARD_CONSUM_RECORD_URL, req);
            logger.info("返回卡消费记录查询报文:{}", res);
            if (StringUtils.isBlank(res)) {
                logger.warn("{}返回卡消费记录为空");
                return TsmBaseRes.fail();
            }
            TsmBaseRes tsmBaseRes = JSON.parseObject(res, TsmBaseRes.class);
            CardConsumRecordRes cardConsumRecordRes = JSON.parseObject(JSON.parseObject(res).getString("data"), CardConsumRecordRes.class);
//            List<CardConsumRecordRes> cardConsumRecordResList = JSONArray.parseObject(res, List.class);
            return tsmBaseRes;
        } catch (Exception e) {
            logger.error("返回卡消费记录异常:{}", e);
            return TsmBaseRes.fail();
        }
    }

    // 生成唯一的order_no

    /**
     * @param terminalCode   终端编号
     * @param transactionNum 终端交易序号
     * @return
     */
    private String getOrderNo(String terminalCode, String transactionNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_2 = df.format(new Date().getTime());
        String orderNo = terminalCode + transactionNum + date_2;
        return orderNo;
    }

    private String selOrderNo(String cardNo,String cardOptype,String issubmit) {
        List<TsmCardapduApply> tsmCardapduApplies = tsmCardapduApplyMapper.selByCradNo(cardNo,cardOptype,issubmit);
        String orderNo = "";
        if (CollectionUtil.isNotEmpty(tsmCardapduApplies)) {
            TsmCardapduApply tsmCardapduApply = tsmCardapduApplies.get(0);
            orderNo = tsmCardapduApply.getCityOrderNo();
        }
        return orderNo;
    }


}
