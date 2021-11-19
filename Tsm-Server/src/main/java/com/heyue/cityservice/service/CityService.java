package com.heyue.cityservice.service;


import com.heyue.bean.TsmBaseRes;
import com.heyue.cityservice.message.request.*;

/**
 * 城市服务
 */
public interface CityService {

    // 1.交易信息查询
    TsmBaseRes selTradeInfoR(TradeInfoReq tradeInfoReq);

    // 2.卡激活请求
    TsmBaseRes cardActive(CardActiveReq cardActiveReq);

    // 3.卡激活请求提交
    TsmBaseRes cardActiveSubmit(CardActiveSubmitReq cardActiveSubmitReq);

    // 4.卡圈存请求
    TsmBaseRes cardTrap(CardTrapReq cardTrapReq);

    // 5.卡圈存请求提交
    TsmBaseRes cardTrapSubmit(CardTrapSubmitReq cardTrapSubmitReq);

    // 6.卡账户信息查询
    TsmBaseRes cardAccountInfo(CardAccountInfoReq cardAccountInfoReq);

    // 7.卡消费记录查询
    TsmBaseRes cardConsumRecord(CardConsumRecordReq cardConsumRecordReq);
}
