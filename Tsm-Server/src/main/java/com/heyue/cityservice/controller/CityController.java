package com.heyue.cityservice.controller;


import cn.com.heyue.entity.TsmCardDetail;
import cn.com.heyue.mapper.TsmCardDetailMapper;
import com.heyue.bean.Result;
import com.heyue.cityservice.message.request.*;
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
    public Result selTradeInfoR(@RequestBody TradeInfoReq tradeInfoReq) {
        Result result = cityService.selTradeInfoR(tradeInfoReq);
        return result;
    }

    // 向城市服务发送卡激活请求
    @PostMapping("cardActive/get")
    @ResponseBody
    public Result cardActive(@RequestBody CardActiveReq cardActiveReq) {
        Result result = cityService.cardActive(cardActiveReq);
        return result;
    }


    // 向城市服务发送卡激活请求提交
    @PostMapping("cardActiveSubmit/get")
    @ResponseBody
    public Result cardActiveSubmit(@RequestBody CardActiveSubmitReq cardActiveSubmitReq) {
        Result result = cityService.cardActiveSubmit(cardActiveSubmitReq);
        return result;
    }

    // 向城市服务发送卡圈存请求
    @PostMapping("cardTrap/get")
    @ResponseBody
    public Result cardTrap(@RequestBody CardTrapReq cardTrapReq) {
        Result result = cityService.cardTrap(cardTrapReq);
        return result;
    }

    // 卡圈存请求提交
    @PostMapping("cardTrapSubmit/get")
    @ResponseBody
    public Result cardTrapSubmit(@RequestBody CardTrapSubmitReq cardTrapSubmitReq) {
        Result result = cityService.cardTrapSubmit(cardTrapSubmitReq);
        return result;
    }

    // 卡圈存请求提交
    @PostMapping("cardConsumRecord/get")
    @ResponseBody
    public Result cardConsumRecord(@RequestBody CardConsumRecordReq cardConsumRecordReq) {
        Result result = cityService.cardConsumRecord(cardConsumRecordReq);
        return result;
    }


}