package com.heyue.cityservice.service;


import com.heyue.bean.Result;
import com.heyue.cityservice.message.request.*;

/**
 * 城市服务
 */
public interface CityService {

    // 1.交易信息查询
    Result selTradeInfoR(TradeInfoReq tradeInfoReq);

    // 2.卡激活请求
    Result cardActive(CardActiveReq cardActiveReq);

    // 3.卡激活请求提交
    Result cardActiveSubmit(CardActiveSubmitReq cardActiveSubmitReq);

    // 4.卡圈存请求
    Result cardTrap(CardTrapReq cardTrapReq);

    // 5.卡圈存请求提交
    Result cardTrapSubmit(CardTrapSubmitReq cardTrapSubmitReq);

    // 6.卡账户信息查询
    Result cardAccountInfo(CardAccountInfoReq cardAccountInfoReq);

    // 7.卡消费记录查询
    Result cardConsumRecord(CardConsumRecordReq cardConsumRecordReq);
}
