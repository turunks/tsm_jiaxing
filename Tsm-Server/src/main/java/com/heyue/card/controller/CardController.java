package com.heyue.card.controller;


import cn.com.heyue.utils.QRCodeUtil;
import com.heyue.bean.TsmBaseRes;
import com.heyue.card.message.request.CreatCardDataReq;
import com.heyue.card.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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

    @GetMapping("testQrcode")
    @ResponseBody
    public void testQrcode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 二维码的内容
        String content = "二维码存储信息";
        QRCodeUtil.encode(content, response.getOutputStream());
    }


}