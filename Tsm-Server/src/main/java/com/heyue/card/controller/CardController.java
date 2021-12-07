package com.heyue.card.controller;


import cn.com.heyue.entity.TsmTerminalOrder;
import cn.com.heyue.mapper.TsmCardDetailMapper;
import cn.com.heyue.mapper.TsmCardMakefileMapper;
import cn.com.heyue.mapper.TsmTerminalOrderMapper;
import cn.com.heyue.utils.QRCodeUtil;
import cn.com.heyue.utils.RSAUtils;
import com.alibaba.fastjson.JSON;
import com.heyue.bean.TsmBaseReq;
import com.heyue.bean.TsmBaseRes;
import com.heyue.card.message.request.CreatCardDataReq;
import com.heyue.card.service.CardService;
import com.heyue.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("/card")
public class CardController {


    private static Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private TsmTerminalOrderMapper tsmTerminalOrderMapper;

    @Autowired
    private TsmCardMakefileMapper tsmCardMakefileMapper;

    @Autowired
    private TsmCardDetailMapper tsmCardDetailMapper;

    @Autowired
    private CardService cardService;


    @RequestMapping("TsmTerminalOrder/get")
    @ResponseBody
    public TsmTerminalOrder getTsmTerminalOrder() {
        TsmTerminalOrder tsmTerminalOrder = tsmTerminalOrderMapper.selectByPrimaryKey((long) 1);
        try {
            String signRet = RSAUtils.signWithRsa2(JSON.toJSONString(tsmTerminalOrder).getBytes(StandardCharsets.UTF_8), Constant.TSM_LOC_PRI_KEY).replaceAll(System.getProperty("line.separator"), "");
            TsmBaseReq<TsmTerminalOrder> tsmTerminalOrderTsmBaseReq = new TsmBaseReq<TsmTerminalOrder>(tsmTerminalOrder, signRet);
            System.out.println(tsmTerminalOrderTsmBaseReq);
        } catch (Exception e) {
            logger.info("系统异常:{}", e);
        }
        return tsmTerminalOrder;
    }

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