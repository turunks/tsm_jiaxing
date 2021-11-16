package com.heyue.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

//@Component
public class FeedBackTask {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
//    private ResourceGrantService resourceGrantService;


    // 解析城市平台反馈文件定时任务
    @Scheduled(cron = "0/30 * * * * ? ")// 30秒
    public void FeedBackTask() {
        // 定时解析指定目录（城市平台ftp过来）获取到的反馈文件
        System.out.println("nihao");
//        String downloadFielName = "211116110407001111.FS";
////        SftpUtils sftp = new SftpUtils(Constant.SFTP_USER_NAME,Constant.SFTP_HOST, Constant.SFTP_PORT,"/home/cx_sem/.ssh/id_rsa");
//        SftpUtils sftp = new SftpUtils("baotgj", "CVgcayEJ5S", "jiaotong.unionpay.com", 6022);
//        sftp.login();
//        try {
//            sftp.download("directory", "downloadFile", "saveFile");
//        } catch (Exception e) {
//            logger.error("下载文件异常:{}", e);
//        }
//        sftp.logout();
    }

}
