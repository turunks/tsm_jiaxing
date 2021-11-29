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
    public static String LOCAL_CATALOG;
    // FTP
    public static String FTP_HOST;
    public static String FTP_PORT;
    public static String FTP_USER_NAME;
    public static String FTP_PASSWORD;
    public static String FTP_UPLOAD_CATALOG;
    public static String FTP_DOWNLOAD_CATALOG;
    public static String FTP_DOWNLOAD_HISTORY;

    // 系统参数
    // TSM_ID
    public static String TSM_ID = "TSM_ID";
    public static String CITY_CODE = "CITY_CODE";
    public static String VERSION = "1.0.0";


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


    //FTP
    @Value("${FTP_HOST}")
    public void setftpHost(String ftpHost) {
        FTP_HOST = ftpHost;
    }

    @Value("${FTP_PORT}")
    public void setftpPort(String ftpPort) {
        FTP_PORT = ftpPort;
    }

    @Value("${FTP_USER_NAME}")
    public void setftpUserName(String ftpUserName) {
        FTP_USER_NAME = ftpUserName;
    }

    @Value("${FTP_PASSWORD}")
    public void setftpPassWord(String ftpPassWord) {
        FTP_PASSWORD = ftpPassWord;
    }

    @Value("${LOCAL_CATALOG}")
    public void setlocalCatalog(String localCatalog) {
        LOCAL_CATALOG = localCatalog;
    }

    @Value("${FTP_UPLOAD_CATALOG}")
    public void setftpUploadCatalog(String ftpUploadCatalog) {
        FTP_UPLOAD_CATALOG = ftpUploadCatalog;
    }

    @Value("${FTP_DOWNLOAD_CATALOG}")
    public void setftpDownloadCatalog(String ftpDownloadCatalog) {
        FTP_DOWNLOAD_CATALOG = ftpDownloadCatalog;
    }

    @Value("${FTP_DOWNLOAD_HISTORY}")
    public void setftpDownloadHistory(String ftpDownloadHistory) {
        FTP_DOWNLOAD_HISTORY = ftpDownloadHistory;
    }

}
