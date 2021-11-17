package com.heyue.card.service.impl;

import cn.com.heyue.entity.TsmCardDetail;
import cn.com.heyue.entity.TsmCardMakefile;
import cn.com.heyue.mapper.TsmCardDetailMapper;
import cn.com.heyue.mapper.TsmCardMakefileMapper;
import cn.com.heyue.utils.FileUtils;
import cn.com.heyue.utils.FtpUtils;
import com.alibaba.fastjson.JSONObject;
import com.heyue.card.message.request.CreatCardDataReq;
import com.heyue.card.message.response.Secretkey;
import com.heyue.card.service.CardService;
import com.heyue.cityservice.service.impl.CityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private static Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

    @Autowired
    private TsmCardMakefileMapper tsmCardMakefileMapper;

    @Autowired
    private TsmCardDetailMapper tsmCardDetailMapper;

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

    //
    public static final String local_catalog = "D:/bak/";

    // 制卡
    @Override
    public void creatCardDataFile(CreatCardDataReq creatCardDataReq) {
        // 按照一定文件格式生成制卡数据申请文件 并ftp至城市平台指定目
        //1.制卡
        String filename = writeFile(creatCardDataReq);
        //2.上传
        toFTP(filename);
    }

    //
    public String writeFile(CreatCardDataReq creatCardDataReq) {
        String version = "01";// 版本号
        String recordNum = creatCardDataReq.getRecordNum();// 2 长度
        String city_code = creatCardDataReq.getCity_code();// 2
        String requestType = creatCardDataReq.getRequestType();// 1
        String area_code = creatCardDataReq.getArea_code();// 1
        String card_species = creatCardDataReq.getCard_species();// 2
        StringBuffer sb = new StringBuffer();
        sb.append(recordNum);
        sb.append(city_code);
        sb.append(requestType);
        sb.append(area_code);
        sb.append(card_species);


        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        String date_2 = df.format(new Date().getTime());
        city_code = "00";
        String serialno = "1111";// 流水号
        String filename = date_2 + city_code + serialno + ".SQ";
//        String path = "D:\\" + filename;
        String path = local_catalog + filename;
        List<String> accountList = new ArrayList<>();
        accountList.add(version);
        accountList.add("");
        accountList.add(sb.toString());
        try {
            FileUtils.writeFileContext(accountList, path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 写入制卡数据文件表
        TsmCardMakefile tsmCardMakefile = new TsmCardMakefile();
        tsmCardMakefile.setCityCode(city_code);
        tsmCardMakefile.setAreaCode(area_code);
        tsmCardMakefile.setCardSpecies(card_species);
        tsmCardMakefile.setMakefileName(filename);
        tsmCardMakefile.setMakefileFtppath("d://ftppath");
        tsmCardMakefile.setMakefileCreatetime(new Date());
        tsmCardMakefile.setMakefileSerialno(serialno);
        tsmCardMakefile.setCardnum(Integer.valueOf(recordNum));
        tsmCardMakefile.setCardtype(requestType);
        tsmCardMakefileMapper.insertSelective(tsmCardMakefile);

        // 返回文件名
        return filename;
    }


    public void readFile(String filename) {
        try {
            String path = local_catalog + filename;
//            String path = "D:\\" + "211116110407001111.FS";
            String fileName = new File(path.trim()).getName();// 获取文件名
            String serialno = fileName.substring(14, 18);

            List<String> list = FileUtils.getFile(path);
            String s = list.toString();
            // 1.解析交易头
            List<String> tradeHead = list.subList(2, 3);
            String head = tradeHead.get(0);
            String card_num = head.substring(0, 2);
//            String city_code = head.substring(2, 4);
            String card_type = head.substring(2, 4);
            // 2.解析mac
            int lastIndexOf = list.lastIndexOf("");
            List<String> mac = list.subList(lastIndexOf, list.size());
            // 获取数据体
            List<String> body = list.subList(4, lastIndexOf);
            // 解析数据体
            body.removeIf(s1 -> s1.isEmpty());

            // 更新制卡数据文件表反馈相关参数
            TsmCardMakefile tsmCardMakefile = new TsmCardMakefile();
            tsmCardMakefile.setFeedbackFilename(fileName);//制卡文件名
            tsmCardMakefile.setFeedbackfileCreatetime(path);//
            tsmCardMakefile.setGettime(new Date());
            tsmCardMakefile.setFeedbackfileSerialno(serialno);
            tsmCardMakefile.setFeedbackfileCardnum(Integer.valueOf(card_num));
            tsmCardMakefile.setMakefileSerialno(serialno);
            tsmCardMakefileMapper.updateBySerialno(tsmCardMakefile);

            // 解析用户卡号
            for (String data : body) {
                // 数据域1
                String card_no = data.substring(0, card_no_index); // 用户卡号1
                String retain = data.substring(card_no_index, remark_index); // 保留域
                String Card_sign = data.substring(remark_index, Card_sign_index);//发卡方标识
                String requestType = data.substring(Card_sign_index, requestType_index);//应用类型标识
                String card_app_version = data.substring(requestType_index, area_code_index);//发卡方应用版本
                String app_serial_no = data.substring(area_code_index, app_serial_no_index);//应用序列号
                String app_start_date = data.substring(app_serial_no_index, app_start_date_index);//应用启用日期
                String app_valid_date = data.substring(app_start_date_index, app_valid_date_index);//应用失效日期
                String card_custom_fci = data.substring(app_valid_date_index, card_custom_fci_index);//发卡方自定义 FCI 数据
                String card_type_sign = data.substring(card_custom_fci_index, card_type_sign_index);//卡类型标识
                String internate_code = data.substring(card_type_sign_index, internate_code_index);//国际代码
                String province_code = data.substring(internate_code_index, province_code_index);//省际代码
                String city_code = data.substring(province_code_index, city_code_index);//城市代码
                String contact_card_type = data.substring(city_code_index, contact_card_type_index);//互通卡种
                String remark1 = data.substring(city_code_index, contact_card_type_index);//预留
                System.out.println(data);

                // 解析数据域2
                String domesticKey = formatSecretkey(data);

                // 写入卡数据明细表
                // 据流水号查询制卡文件表
                TsmCardMakefile tsmCardMakefile1 = tsmCardMakefileMapper.selBySerialno(tsmCardMakefile);
                Long id = tsmCardMakefile1.getId();// 为制卡数据文件的id
                String areaCode = tsmCardMakefile1.getAreaCode();// 地区代码
                String cardSpecies = tsmCardMakefile1.getCardSpecies();// 卡种类型

                TsmCardDetail tsmCardDetail = new TsmCardDetail();
                tsmCardDetail.setCadfileId(id);//卡文件id
                tsmCardDetail.setCityCode(city_code);//城市代码
                tsmCardDetail.setAreaCode(areaCode);//地区代码
                tsmCardDetail.setCardSpecies(cardSpecies);//卡种类型
                tsmCardDetail.setCardNo(card_no);//卡片序列号
                tsmCardDetail.setCardSign(Card_sign);//发卡方标识
                tsmCardDetail.setCardAppVersion(requestType);//应用类型标识
                tsmCardDetail.setAppSerialNo(app_serial_no);//应用序列号
                tsmCardDetail.setAppStartDate(app_start_date);//应用启用日期
                tsmCardDetail.setAppValidDate(app_valid_date);//应用失效日期
                tsmCardDetail.setCardCustomFci(card_custom_fci);//fci数据
                tsmCardDetail.setCardTypeSign(card_type_sign);//卡类型标识
                tsmCardDetail.setInternateCode(internate_code);//国际代码
                tsmCardDetail.setProvinceCode(province_code);//省际代码
                tsmCardDetail.setCityCode(city_code);//城市代码
                tsmCardDetail.setContactCardType(contact_card_type);//互通卡种
                tsmCardDetail.setCardType(card_type);//卡类型
                tsmCardDetail.setReserve(remark1);//预留
                tsmCardDetail.setInternationKey(domesticKey);//国际密钥 json
//                tsmCardDetail.setDomesticKey("");//国密密钥
                tsmCardDetail.setCardStatus("1");//卡状态 1入库2出库
                tsmCardDetail.setInDepositTime(new Date());//入库时间
//                tsmCardDetail.setOutDepositTime();//出库时间
                tsmCardDetail.setAppId("");//应用id
                tsmCardDetail.setMerchantNo("");//服务商号
                tsmCardDetailMapper.insertSelective(tsmCardDetail);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String formatSecretkey(String data) {
        // 解析数据域2
        //数据域2(对称密钥)
        String userAppKey = data.substring(contact_card_type_index, userAppKey_index); // 用户卡主控密钥
        String userCardMaintainKey = data.substring(userAppKey_index, userCardMaintainKey_index); // 用户卡维护密钥
        String userAppMaintainKey = data.substring(userCardMaintainKey_index, userAppMaintainKey_index); // 用户卡应用主控密钥
        String contactWalletSpareKey = data.substring(userAppMaintainKey_index, contactWalletSpareKey_index); // 用户卡应用维护密钥
        String consumeKey = data.substring(contactWalletSpareKey_index, consumeKey_index); // 消费密钥
        String rechargeKey = data.substring(consumeKey_index, rechargeKey_index); // 充值密钥
        String tacKey = data.substring(rechargeKey_index, tacKey_index); // TAC 密钥
        String userLockAppMaintainKey = data.substring(tacKey_index, userLockAppMaintainKey_index); // 用户卡应用维护密钥（应用锁定）
        String pinKey = data.substring(userLockAppMaintainKey_index, pinKey_index); // PIN 密钥
        String contactEcashKey = data.substring(pinKey_index, contactEcashKey_index); // 互通记录保护密钥-电子现金
        String contactEwalletKey = data.substring(contactEcashKey_index, contactEwalletKey_index); // 互通记录保护密钥（现金备用）
        String userUnlockAppMaintainKey = data.substring(contactEwalletKey_index, userUnlockAppMaintainKey_index); // 用户卡应用维护密钥（应用解锁）
        String reserveKey1 = data.substring(userUnlockAppMaintainKey_index, reserveKey1_index); // 预留密钥 1
        String reserveKey2 = data.substring(reserveKey1_index, reserveKey2_index); // 预留密钥 2
        String key2 = data.substring(reserveKey2_index, key2_index); // 充值密钥 2（国际）
        String remark2 = data.substring(key2_index, remark2_index); // 预留

        // 国际密钥json
        Secretkey secretkey = new Secretkey();
        secretkey.setConsumeKey(consumeKey);
        secretkey.setContactEcashKey(contactEcashKey);
        secretkey.setContactEwalletKey(contactEwalletKey);
        secretkey.setContactWalletSpareKey(contactWalletSpareKey);
        secretkey.setPinKey(pinKey);
        secretkey.setRechargeKey(rechargeKey);
        secretkey.setReserveKey1(reserveKey1);
        secretkey.setReserveKey2(reserveKey2);
        secretkey.setTacKey(tacKey);
        secretkey.setUserAppKey(userAppKey);
        secretkey.setUserAppMaintainKey(userAppMaintainKey);
        secretkey.setUserCardKey("");
        secretkey.setUserCardMaintainKey(userCardMaintainKey);
        secretkey.setUserLockAppMaintainKey(userLockAppMaintainKey);
        secretkey.setUserUnlockAppMaintainKey(userUnlockAppMaintainKey);
        String domesticKey = JSONObject.toJSONString(secretkey);
        return domesticKey;
    }


    public void toFTP(String filename) {
        // 上传至ftp
        FtpUtils ftpUtils = new FtpUtils();
        ftpUtils.setServer("192.168.99.100");
        ftpUtils.setPort("21");
        ftpUtils.setUser("test");
        ftpUtils.setPassword("test");
        ftpUtils.setTimeout("30000");
        try {
            ftpUtils.connectServer("upload");
            System.out.println("登录成功。。。");
//            String resource = this.getClass().getResource("/").toString();// class路径
//            System.out.println(resource);
//            String uploadRelativePath = "upload/";
//            String path = resource + uploadRelativePath;
            ftpUtils.upload(local_catalog + filename, filename);// 本地路径,ftp路径
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void downFromFTP() {
        // 下载至ftp
        FtpUtils ftpUtils = new FtpUtils();
        ftpUtils.setServer("192.168.99.100");
        ftpUtils.setPort("21");
        ftpUtils.setUser("test");
        ftpUtils.setPassword("test");
        ftpUtils.setTimeout("30000");
        try {
            ftpUtils.connectServer("download");// test用户下的目录
            System.out.println("登录成功。。。");
            // 获取指定ftp下目录的文件
            List<String> allFile = ftpUtils.getAllFile(local_catalog);
            // 下载完毕将反馈文件移动至历史文件夹;
            for (String downloadfilename : allFile) {
                ftpUtils.moveFile(downloadfilename, "/downloadhistory/" + downloadfilename);
                // 解析下载的文件数据
                readFile(downloadfilename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
