package com.heyue.cityservice.controller;


import com.heyue.bean.TsmBaseRes;
import com.heyue.cityservice.message.request.*;
import com.heyue.cityservice.message.response.*;
import com.heyue.cityservice.service.CityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author lyc
 * @date 2020-07-01 17:19:03
 */
@Api(value = "", tags = {"操作接口"})
//@EnableEurekaClient
@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;


    // 向城市服务发送交易查询
    @PostMapping("selTradeInfoR")
    @ResponseBody
    public TsmBaseRes selTradeInfoR(@RequestBody TradeInfoReq tradeInfoReq) {
        TsmBaseRes tsmBaseRes = cityService.selTradeInfoR(tradeInfoReq);
        return tsmBaseRes;
    }

    // 向城市服务发送卡激活请求
    @PostMapping("cardActive")
    @ResponseBody
    public CardActiveRes cardActive(@RequestBody CardActiveReq cardActiveReq) {
        CardActiveRes cardActiveRes = cityService.cardActive(cardActiveReq);
        return cardActiveRes;
    }


    // 向城市服务发送卡激活请求提交
    @PostMapping("cardActiveSubmit")
    @ResponseBody
    public CardActiveSubmitRes cardActiveSubmit(@RequestBody CardActiveSubmitReq cardActiveSubmitReq) {
        CardActiveSubmitRes cardActiveSubmitRes = cityService.cardActiveSubmit(cardActiveSubmitReq);
        return cardActiveSubmitRes;
    }

    // 向城市服务发送卡圈存请求
    @PostMapping("cardTrap")
    @ResponseBody
    public CardTrapRes cardTrap(@RequestBody CardTrapReq cardTrapReq) {
        CardTrapRes cardTrapRes = cityService.cardTrap(cardTrapReq);
        return cardTrapRes;
    }

    // 卡圈存请求提交
    @PostMapping("cardTrapSubmit")
    @ResponseBody
    public CardTrapSubmitRes cardTrapSubmit(@RequestBody CardTrapSubmitReq cardTrapSubmitReq) {
        CardTrapSubmitRes cardTrapSubmitRes = cityService.cardTrapSubmit(cardTrapSubmitReq);
        return cardTrapSubmitRes;
    }

    // 卡账户信息查询
    @PostMapping("cardAccountInfo")
    @ResponseBody
    public TsmBaseRes cardAccountInfo(@RequestBody CardAccountInfoReq cardAccountInfoReq) {
        TsmBaseRes result = cityService.cardAccountInfo(cardAccountInfoReq);
        return result;
    }

    // 卡消费记录查询
    @PostMapping("cardConsumRecord")
    @ResponseBody
    public TsmBaseRes cardConsumRecord(@RequestBody CardConsumRecordReq cardConsumRecordReq) {
        TsmBaseRes result = cityService.cardConsumRecord(cardConsumRecordReq);
        return result;
    }

    // 退卡通知
    @PostMapping("cardReturnNotify")
    @ResponseBody
    public CardReturnNotifyRes cardReturnNotify(@RequestBody CardReturnNotifyReq cardReturnNotifyReq) {
        CardReturnNotifyRes cardReturnNotifyRes = cityService.cardReturnNotify(cardReturnNotifyReq);
        return cardReturnNotifyRes;
    }

    // 敬老卡鉴权
    @PostMapping("validElderCard")
    @ResponseBody
    public String validElderCard(@RequestBody ValidElderCardReq validElderCardReq) {
        return cityService.validElderCard(validElderCardReq);

    }
}