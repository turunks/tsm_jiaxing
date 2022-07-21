package com.heyue.cityservice.service;


import com.heyue.bean.TsmBaseRes;
import com.heyue.cityservice.message.request.*;
import com.heyue.cityservice.message.response.*;

/**
 * 城市服务
 */
public interface CityService {

    // 1.交易信息查询
    TsmBaseRes selTradeInfoR(TradeInfoReq tradeInfoReq);

    // 2.卡激活请求
    CardActiveRes cardActive(CardActiveReq cardActiveReq);

    // 3.卡激活请求提交
    CardActiveSubmitRes cardActiveSubmit(CardActiveSubmitReq cardActiveSubmitReq);

    // 4.卡圈存请求
    CardTrapRes cardTrap(CardTrapReq cardTrapReq);

    // 5.卡圈存请求提交
    CardTrapSubmitRes cardTrapSubmit(CardTrapSubmitReq cardTrapSubmitReq);

    // 6.卡账户信息查询
    TsmBaseRes cardAccountInfo(CardAccountInfoReq cardAccountInfoReq);

    // 7.卡消费记录查询
    TsmBaseRes cardConsumRecord(CardConsumRecordReq cardConsumRecordReq);

    // 8.退卡通知
    CardReturnNotifyRes cardReturnNotify(CardReturnNotifyReq cardReturnNotifyReq);

    // 9.敬老卡鉴权
    String validElderCard(ValidElderCardReq validElderCardReq);

}
