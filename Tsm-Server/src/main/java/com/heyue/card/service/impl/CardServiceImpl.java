package com.heyue.card.service.impl;

import cn.com.heyue.utils.FileUtils;
import cn.com.heyue.utils.SftpUtils;
import com.heyue.card.service.CardService;
import com.heyue.cityservice.service.impl.CityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private static Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

    @Override
    public void creatCardDataFile() {
    // 按照一定文件格式生成制卡数据申请文件 并ftp至城市平台指定目录
        // 登录sftp
        SftpUtils sftp = new SftpUtils("baotgj", "CVgcayEJ5S", "jiaotong.unionpay.com", 6022);
        sftp.login();

        List<String> accountList = new ArrayList<>();
        accountList.add("01"); // 版本号
        accountList.add("TRADELIST END,000000,000000000000"); // 交易头 记录总数 城市代码 请求类型 地区代码 卡种类型拼接
        try {
            FileUtils.writeFileContext(accountList,"D:\\和悦项目\\包头易码行\\test.txt");
            File f = new File("D:\\和悦项目\\包头易码行\\test.txt");
            InputStream is = new FileInputStream(f);
            sftp.upload("/C150220190200001/20190331", "test.txt",is);
            sftp.logout();
        } catch (Exception e) {
            logger.error("卡激活请求异常:{}", e);
        }

    }
}
