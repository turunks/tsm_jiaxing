package com.heyue.card.controller;


import com.heyue.bean.Result;
import com.heyue.bean.TsmBaseRes;
import com.heyue.card.message.request.CardDetailReq;
import com.heyue.card.message.request.CreatCardDataReq;
import com.heyue.card.service.CardService;
import com.heyue.hbcxservice.message.response.CardNumRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/card")
public class CardController {


    private static Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private CardService cardService;


    @RequestMapping("downFromFTP")
    @ResponseBody
    public void downFromFTP() throws Exception {
        // 下载至ftp并解析
        cardService.downFromFTP();
    }

    @RequestMapping("cardInfoSynFile")
    @ResponseBody
    public void cardInfoSynFile() throws Exception {
        // 生成卡信息同步文件
        cardService.cardInfoSynFile();
    }

    @PostMapping("createCard")
    @ResponseBody
    public TsmBaseRes testCreateCard(@RequestBody CreatCardDataReq creatCardDataReq) {
        return cardService.creatCardDataFile(creatCardDataReq);
    }

    @RequestMapping("analysisCardConsumRecord")
    @ResponseBody
    public void analysisCardConsumRecord() {
        cardService.analysisCardConsumRecord();
    }

    @RequestMapping("/getCardStock")
    @ResponseBody
    public Result<CardNumRes> getCardStock(@RequestBody CardDetailReq cardDetailReq) {
        logger.info("【卡数据库存查询】请求参数{}", cardDetailReq);
        Result<CardNumRes> result =  cardService.qryCardCount(cardDetailReq);
        return result;
    }

}