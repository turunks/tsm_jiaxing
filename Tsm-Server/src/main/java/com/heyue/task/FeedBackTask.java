package com.heyue.task;

import cn.com.heyue.utils.DateUtils;
import com.heyue.card.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FeedBackTask {
    @Autowired
    private CardService cardService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    // 解析城市平台反馈文件定时任务
    @Scheduled(cron = "0 0 23 * * ?")// 每天23:00执行
    public void FeedBackTask() {
        // 下载至ftp并解析
        logger.info("解析城市平台反馈文件定时任务开始:{}", DateUtils.format(new Date(),DateUtils.FullDatePattern));
        cardService.downFromFTP();
        logger.info("解析城市平台反馈文件定时任务结束:{}", DateUtils.format(new Date(),DateUtils.FullDatePattern));
    }

    // 解析城市平台消费文件定时任务
    @Scheduled(cron = "0 0 22 * * ?")// 每天22:00执行
    public void consumeFileTask() {
        // 下载至ftp并解析
        logger.info("解析城市平台消费文件定时任务开始:{}", DateUtils.format(new Date(),DateUtils.FullDatePattern));
        cardService.analysisCardConsumRecord();
        logger.info("解析城市平台消费文件定时任务结束:{}", DateUtils.format(new Date(),DateUtils.FullDatePattern));
    }


    // 生成发卡信息同步文件定时任务
//    @Scheduled(cron = "0/30 * * * * ? ")// 30秒
    public void cardInfoSynFileTask() {
        // 生成发卡信息同步文件并上传FTP
        cardService.cardInfoSynFile();
    }
}
