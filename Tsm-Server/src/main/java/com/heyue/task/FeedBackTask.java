package com.heyue.task;

import cn.com.heyue.utils.FtpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

//@Component
public class FeedBackTask {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    // 解析城市平台反馈文件定时任务
//    @Scheduled(cron = "0/30 * * * * ? ")// 30秒
    public void FeedBackTask() {
        // 定时解析指定目录（城市平台ftp过来）获取到的反馈文件
        // 下载至ftp
        FtpUtils ftpUtils = new FtpUtils();
        ftpUtils.setServer("192.168.99.100");
        ftpUtils.setPort("21");
        ftpUtils.setUser("test");
        ftpUtils.setPassword("test");
        ftpUtils.setTimeout("30000");
        try {
            ftpUtils.connectServer("");
            System.out.println("登录成功。。。");
            //ftpUtils.getZdFile("/cmpay/20180410/","D:\\Users\\UserA\\Desktop\\bak");
            File f = new File("D:/bak", "test.txt");
            ftpUtils.download("/xuyang1/test.txt", f.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
