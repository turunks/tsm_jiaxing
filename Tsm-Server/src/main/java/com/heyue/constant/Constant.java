package com.heyue.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource(value = "classpath:application.properties")
public class Constant {

    // 城市平台服务
    public static String SEL_TRADEINFO_URL;
    public static String CARD_ACTIVE_URL;
    public static String CARD_ACTIVE_SUBMIT_URL;
    public static String CARD_TRAP_URL;
    public static String CARD_TRAP_SUBMIT_URL;
    public static String CARD_ACCOUNT_INFO_URL;
    public static String CARD_CONSUM_RECORD_URL;

    // 私钥
    public static String TSM_LOC_PRI_KEY;



    // 地址
    // 本地制卡目录
    public static String LOCAL_CATALOG = "D:/bak/";
    // FTP
    public static String FTP_HOST="192.168.99.100";
    public static String FTP_PORT="21";
    public static String FTP_USER_NAME="test";
    public static String FTP_PASSWORD="test";
    public static String FTP_UPLOAD_CATALOG="/upload";
    public static String FTP_DOWNLOAD_CATALOG="/download";
    public static String FTP_DOWNLOAD_HISTORY="/downloadhistory";

    @Value("${SEL_TRADEINFO_URL}")
    public void setselTradeInfoUrl(String selTradeInfoUrl) {
        SEL_TRADEINFO_URL = selTradeInfoUrl;
    }

    @Value("${CARD_ACTIVE_URL}")
    public void setcardActiveUrl(String cardActiveUrl) {
        CARD_ACTIVE_URL = cardActiveUrl;
    }

    @Value("${CARD_ACTIVE_SUBMIT_URL}")
    public void setcardActiveSubmitUrl(String cardActiveSubmitUrl) {
        CARD_ACTIVE_SUBMIT_URL = cardActiveSubmitUrl;
    }

    @Value("${CARD_TRAP_URL}")
    public void setcardTrapUrl(String cardTrapUrl) {
        CARD_TRAP_URL = cardTrapUrl;
    }

    @Value("${CARD_TRAP_SUBMIT_URL}")
    public void setcardTrapSubmitUrl(String cardTrapSubmitUrl) {
        CARD_TRAP_SUBMIT_URL = cardTrapSubmitUrl;
    }

    @Value("${CARD_ACCOUNT_INFO_URL}")
    public void setcardAccountInfoUrl(String cardAccountInfoUrl) {
        CARD_ACCOUNT_INFO_URL = cardAccountInfoUrl;
    }

    @Value("${CARD_CONSUM_RECORD_URL}")
    public void setcardConsumRecordUrl(String cardConsumRecordUrl) {
        CARD_CONSUM_RECORD_URL = cardConsumRecordUrl;
    }

    // 发送请求私钥
    @Value("${TSM_LOC_PRI_KEY}")
    public void setTsmLocPriKey(String cardTsmLocPriKey) {
        TSM_LOC_PRI_KEY = cardTsmLocPriKey;
    }

    //

}
