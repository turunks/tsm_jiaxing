package com.heyue.cityservice.controller;


import cn.com.heyue.entity.TsmCardDetail;
import cn.com.heyue.mapper.TsmCardDetailMapper;
import com.heyue.bean.TsmBaseRes;
import com.heyue.cityservice.message.request.*;
import com.heyue.cityservice.message.response.CardActiveRes;
import com.heyue.cityservice.message.response.CardActiveSubmitRes;
import com.heyue.cityservice.message.response.CardTrapRes;
import com.heyue.cityservice.message.response.CardTrapSubmitRes;
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
public class CityController {

    @Autowired
    private TsmCardDetailMapper tsmCardDetailMapper;

    @Autowired
    private CityService cityService;


    @RequestMapping("TsmCardDetail/get")
    @ResponseBody
    public TsmCardDetail getTsmCardDetail() {
        TsmCardDetail tsmCardDetail = tsmCardDetailMapper.selectByPrimaryKey((long) 1);
        return tsmCardDetail;
    }


    // 向城市服务发送交易查询
    @PostMapping("selTradeInfoR/get")
    @ResponseBody
    public TsmBaseRes selTradeInfoR(@RequestBody TradeInfoReq tradeInfoReq) {
        TsmBaseRes tsmBaseRes = cityService.selTradeInfoR(tradeInfoReq);
        return tsmBaseRes;
    }

    // 向城市服务发送卡激活请求
    @PostMapping("cardActive/get")
    @ResponseBody
    public CardActiveRes cardActive(@RequestBody CardActiveReq cardActiveReq) {
        CardActiveRes cardActiveRes = cityService.cardActive(cardActiveReq);
        return cardActiveRes;
    }


    // 向城市服务发送卡激活请求提交
    @PostMapping("cardActiveSubmit/get")
    @ResponseBody
    public CardActiveSubmitRes cardActiveSubmit(@RequestBody CardActiveSubmitReq cardActiveSubmitReq) {
        CardActiveSubmitRes cardActiveSubmitRes = cityService.cardActiveSubmit(cardActiveSubmitReq);
        return cardActiveSubmitRes;
    }

    // 向城市服务发送卡圈存请求
    @PostMapping("cardTrap/get")
    @ResponseBody
    public CardTrapRes cardTrap(@RequestBody CardTrapReq cardTrapReq) {
        CardTrapRes cardTrapRes = cityService.cardTrap(cardTrapReq);
        return cardTrapRes;
    }

    // 卡圈存请求提交
    @PostMapping("cardTrapSubmit/get")
    @ResponseBody
    public CardTrapSubmitRes cardTrapSubmit(@RequestBody CardTrapSubmitReq cardTrapSubmitReq) {
        CardTrapSubmitRes cardTrapSubmitRes = cityService.cardTrapSubmit(cardTrapSubmitReq);
        return cardTrapSubmitRes;
    }

    // 卡消费记录查询
    @PostMapping("cardConsumRecord/get")
    @ResponseBody
    public TsmBaseRes cardConsumRecord(@RequestBody CardConsumRecordReq cardConsumRecordReq) {
        TsmBaseRes result = cityService.cardConsumRecord(cardConsumRecordReq);
        return result;
    }
}