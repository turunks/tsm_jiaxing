package com.heyue.card.service;

import com.heyue.bean.TsmBaseRes;
import com.heyue.card.message.request.CreatCardDataReq;

/**
 * 制卡文件服务
 */
public interface CardService {

    // 生成制卡数据申请文件
    TsmBaseRes creatCardDataFile(CreatCardDataReq creatCardDataReq);

    // 读取ftp文件
    boolean readFile(String filename) throws Exception;

    // 上传ftp
    void toFTP(String filename,String uploadPath,String localPath) throws Exception;

    // ftp下载并解析
    void downFromFTP();

    // 每日生成发卡信息同步文件
    void cardInfoSynFile();
}
