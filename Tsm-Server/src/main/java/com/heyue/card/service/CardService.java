package com.heyue.card.service;

import com.heyue.card.message.request.CreatCardDataReq;

/**
 * 制卡文件服务
 */
public interface CardService {

    // 生成制卡数据申请文件
    void creatCardDataFile(CreatCardDataReq creatCardDataReq);

    // 读取ftp文件
    void readFile(String filename);

    // 上传ftp
    void toFTP(String filename);

    // ftp下载并解析
    void downFromFTP();
}
