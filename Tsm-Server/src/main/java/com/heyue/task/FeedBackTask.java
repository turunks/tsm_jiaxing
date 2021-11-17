package com.heyue.task;

import com.heyue.card.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class FeedBackTask {
    @Autowired
    private CardService cardService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    // 解析城市平台反馈文件定时任务
//    @Scheduled(cron = "0/30 * * * * ? ")// 30秒
    public void FeedBackTask() {
        // 下载至ftp并解析
        cardService.downFromFTP();
    }
}
