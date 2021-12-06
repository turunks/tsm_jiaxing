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

    // 城市平台服务反馈文件数据体下标位置
    public static int card_no_index = 10;
    public static int remark_index = card_no_index + 10;
    public static int Card_sign_index = remark_index + 20;
    public static int requestType_index = Card_sign_index + 2;
    public static int area_code_index = requestType_index + 2;
    public static int app_serial_no_index = area_code_index + 30;
    public static int app_start_date_index = app_serial_no_index + 8;
    public static int app_valid_date_index = app_start_date_index + 8;
    public static int card_custom_fci_index = app_valid_date_index + 4;
    public static int card_type_sign_index = card_custom_fci_index + 2;
    public static int internate_code_index = card_type_sign_index + 8;
    public static int province_code_index = internate_code_index + 4;
    public static int city_code_index = province_code_index + 4;
    public static int contact_card_type_index = city_code_index + 4;


    // 数据域2
    public static int userAppKey_index = contact_card_type_index + 2; // 用户卡主控密钥
    public static int userCardMaintainKey_index = userAppKey_index + 2; // 用户卡维护密钥
    public static int userAppMaintainKey_index = userCardMaintainKey_index + 2; // 用户卡应用主控密钥
    public static int contactWalletSpareKey_index = userAppMaintainKey_index + 2;// 用户卡应用维护密钥
    public static int consumeKey_index = contactWalletSpareKey_index + 2;// 消费密钥
    public static int rechargeKey_index = consumeKey_index + 2;// 充值密钥
    public static int tacKey_index = rechargeKey_index + 2;// TAC 密钥
    public static int userLockAppMaintainKey_index = tacKey_index + 2;// 用户卡应用维护密钥
    public static int pinKey_index = userLockAppMaintainKey_index + 2;// PIN 密钥
    public static int contactEcashKey_index = pinKey_index + 2;// 互通记录保护密钥-电子现金
    public static int contactEwalletKey_index = contactEcashKey_index + 2;// 互通记录保护密钥（现金备用）
    public static int userUnlockAppMaintainKey_index = contactEwalletKey_index + 2;// 用户卡应用维护密钥（应用解锁
    public static int reserveKey1_index = userUnlockAppMaintainKey_index + 2;// 预留密钥 1
    public static int reserveKey2_index = reserveKey1_index + 2;// 预留密钥 2
    public static int key2_index = reserveKey2_index + 2;// 充值密钥 2（国际）
    public static int remark2_index = key2_index + 2;// // 预留

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