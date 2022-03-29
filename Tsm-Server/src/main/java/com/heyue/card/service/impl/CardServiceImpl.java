package com.heyue.card.service.impl;

import cn.com.heyue.entity.*;
import cn.com.heyue.mapper.*;
import cn.com.heyue.utils.*;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.heyue.bean.TsmBaseRes;
import com.heyue.card.message.request.AccountConsumeReq;
import com.heyue.card.message.request.CreatCardDataReq;
import com.heyue.card.message.response.Secretkey;
import com.heyue.card.service.CardService;
import com.heyue.constant.Constant;
import com.heyue.utils.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private static Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    @Autowired
    private TsmCardMakefileMapper tsmCardMakefileMapper;

    @Autowired
    private TsmCardDetailMapper tsmCardDetailMapper;

    @Autowired
    private TsmOpencardInfoMapper tsmOpencardInfoMapper;

    @Autowired
    private TsmOpencardSyncfileMapper tsmOpencardSyncfileMapper;

    @Autowired
    private TsmCardConsumedetailMapper tsmCardConsumedetailMapper;

    @Autowired
    private TsmCardConsumefileMapper tsmCardConsumefileMapper;


    // 城市平台服务反馈文件数据体下标位置
    public static int card_no_index = 10 * 2;
    public static int retain_index = card_no_index + 10 * 2;
    public static int card_sign_index = retain_index + 10 * 2;
    public static int app_type_sign_index = card_sign_index + 1 * 2;
    public static int card_app_version_index = app_type_sign_index + 1 * 2;
    public static int app_serial_no_index = card_app_version_index + 10 * 2;
    public static int app_start_date_index = app_serial_no_index + 4 * 2;
    public static int app_valid_date_index = app_start_date_index + 4 * 2;
    public static int card_custom_fci_index = app_valid_date_index + 2 * 2;
    public static int card_type_sign_index = card_custom_fci_index + 1 * 2;
    public static int internate_code_index = card_type_sign_index + 4 * 2;
    public static int province_code_index = internate_code_index + 2 * 2;
    public static int city_code_index = province_code_index + 2 * 2;
    public static int contact_card_type_index = city_code_index + 2 * 2;
    public static int reserve_index = contact_card_type_index + 50 * 2;


    // 数据域2
    public static int userAppKey_index = reserve_index + 24 * 2; // 用户卡主控密钥
    public static int userCardMaintainKey_index = userAppKey_index + 24 * 2; // 用户卡维护密钥
    public static int userAppMaintainKey_index = userCardMaintainKey_index + 24 * 2; // 用户卡应用主控密钥
    public static int contactWalletSpareKey_index = userAppMaintainKey_index + 24 * 2;// 用户卡应用维护密钥
    public static int consumeKey_index = contactWalletSpareKey_index + 24 * 2;// 消费密钥
    public static int rechargeKey_index = consumeKey_index + 24 * 2;// 充值密钥
    public static int tacKey_index = rechargeKey_index + 24 * 2;// TAC 密钥
    public static int userLockAppMaintainKey_index = tacKey_index + 24 * 2;// 用户卡应用维护密钥
    public static int pinKey_index = userLockAppMaintainKey_index + 24 * 2;// PIN 密钥
    public static int contactEcashKey_index = pinKey_index + 24 * 2;// 互通记录保护密钥-电子现金
    public static int contactEwalletKey_index = contactEcashKey_index + 24 * 2;// 互通记录保护密钥（现金备用）
    public static int userUnlockAppMaintainKey_index = contactEwalletKey_index + 24 * 2;// 用户卡应用维护密钥（应用解锁
    public static int reserveKey1_index = userUnlockAppMaintainKey_index + 24 * 2;// 预留密钥 1
    public static int reserveKey2_index = reserveKey1_index + 24 * 2;// 预留密钥 2
    public static int key2_index = reserveKey2_index + 24 * 2;// 充值密钥 2（国际）
    public static int reserve2_index = key2_index + 48 * 2;// // 预留


    // 解析卡消费数据文件
    public static int industry_code_index = 24 * 2; // 行业代码
    public static int file_type = industry_code_index + 24 * 2; // 文件类型
    public static int record_num = file_type + 24 * 2; // 记录总数
    public static int record_size = record_num + 24 * 2; // 记录长度
    public static int consume_reserve_index = record_size + 24 * 2; // 保留域

    public static int local_serialno_index = 12; // 本地流水号
    public static int operating_unit_index = local_serialno_index + 8; // 企业运营系统下的营运单位代码
    public static int citycode_index = operating_unit_index + 4; // 城市代码（交易发生地）
    public static int terminal_code_index = citycode_index + 12; // 终端机编码
    public static int cardno_index = terminal_code_index + 20; // 卡内号
    public static int cardconsume_count_index = cardno_index + 6; // 卡消费计数器
    public static int balance_index = cardconsume_count_index + 8; // 消费前卡余额/余次
    public static int transaction_amount_index = balance_index + 8; // 交易金额/次数
    public static int transaction_date_index = transaction_amount_index + 8; // 交易发生日期
    public static int transaction_time_index = transaction_date_index + 6; // 交易发生时间
    public static int industry_company_no_index = transaction_time_index + 8; // 行业内公司编号


    // 制卡
    @Transactional
    @Override
    public TsmBaseRes creatCardDataFile(CreatCardDataReq creatCardDataReq) {
        // 按照一定文件格式生成制卡数据申请文件 并ftp至城市平台指定目
        try {
            logger.info("制卡请求报文:{}", creatCardDataReq);
            //1.制卡
            String filename = writeFile(creatCardDataReq);
            logger.info("制卡文件名:{}", filename);
            //2.上传
            toFTP(filename, Constant.CREATECARD_UPLOAD_CATALOG, Constant.CREATECARD_LOCAL_CATALOG);
            logger.info("上传制卡文件完成:{}", filename);
            return TsmBaseRes.ok();
        } catch (Exception e) {
            logger.error("制卡请求出错:{}", e);
            return TsmBaseRes.fail();
        }
    }

    //
    public String writeFile(CreatCardDataReq creatCardDataReq) throws Exception {
        String version = "01";// 版本号
        Integer recordNum = creatCardDataReq.getRecordNum();// 长度
        String applyCityCode = creatCardDataReq.getCity_code();// 卡申请城市代码
        String requestType = creatCardDataReq.getRequestType();// 请求类型
        String area_code = creatCardDataReq.getArea_code();// 地区
        String card_species = creatCardDataReq.getCard_species();// 卡种类型
        StringBuffer sb = new StringBuffer();
        String HexrecordNum = HexStringUtils.intToHexString(recordNum, 4);
        sb.append(HexrecordNum);
        sb.append(applyCityCode);
        sb.append(requestType);
        sb.append(area_code);
        sb.append(card_species);
        sb.append("FFFFFFFFFFFFFFFFFFFF");


        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        String date_2 = df.format(new Date().getTime());
        String serialno = IdUtil.get10Serialno();// 流水号
        String filename = "SQ" + date_2 + applyCityCode + serialno + ".dat";
        String path = Constant.CREATECARD_LOCAL_CATALOG + filename;
        List<String> accountList = new ArrayList<>();
        accountList.add(version);
        accountList.add(sb.toString());
        FileUtils.writeFileContext(accountList, path);

        // 写入制卡数据文件表
        TsmCardMakefile tsmCardMakefile = new TsmCardMakefile();
        tsmCardMakefile.setApplyCityCode(applyCityCode);
        tsmCardMakefile.setAreaCode(area_code);
        tsmCardMakefile.setCardSpecies(card_species);
        tsmCardMakefile.setMakefileName(filename);
        tsmCardMakefile.setMakefileFtppath(Constant.CREATECARD_UPLOAD_CATALOG + filename);
        tsmCardMakefile.setMakefileCreatetime(new Date());
        tsmCardMakefile.setMakefileSerialno(serialno);
        tsmCardMakefile.setCardnum(recordNum);
        tsmCardMakefile.setCardtype(requestType);
        tsmCardMakefileMapper.insertSelective(tsmCardMakefile);

        // 返回文件名
        return filename;
    }


    public boolean readFile(String filename) {
        try {
            String path = Constant.CREATECARD_LOCAL_CATALOG + filename;
            String fileName = new File(path.trim()).getName();// 获取文件名
            String serialno = fileName.substring(fileName.length() - 10, fileName.length());// 流水号
            List<String> list = FileUtils.getFile(path);
            String s = list.toString();
            // 1.版本号
            String version = list.get(0);
            // 2.交易头
            String head = list.get(1);
            String card_num = head.substring(0, 4); // 记录总数
            String applyCityCode = head.substring(4, 8); // 卡申请城市代码
            String card_type = head.substring(8, 10); // 卡类型
            String area_code = head.substring(10, 12);// 地区代码
            String card_species = head.substring(12, 16);// 卡种类型

            // 3.解析数据体
//            转换10进制
            int size = Integer.parseInt(card_num, 16);
            List<String> body = list.subList(2, 2 + size);

//            // 更新制卡数据文件表反馈相关参数
            TsmCardMakefile tsmCardMakefile = new TsmCardMakefile();
            tsmCardMakefile.setFeedbackFilename(fileName);//制卡文件名
            tsmCardMakefile.setFeedbackfileCreatetime(path);//
            tsmCardMakefile.setGettime(new Date());
            tsmCardMakefile.setFeedbackfileSerialno(serialno);
            tsmCardMakefile.setFeedbackfileCardnum(size);
            tsmCardMakefile.setMakefileSerialno(serialno);
            tsmCardMakefileMapper.updateBySerialno(tsmCardMakefile);

            // 解析用户卡号
            for (String data : body) {
                // 数据域1
                logger.info("读卡数据体:{}", data);
                String card_no = data.substring(0, card_no_index); // 用户卡号1
                // 105卡号去掉F左补0
                card_no = "0" + card_no.substring(0, card_no.length() - 1);
                String retain = data.substring(card_no_index, retain_index); // 保留域
                String card_sign = data.substring(retain_index, card_sign_index);//发卡方标识
                String app_type_sign = data.substring(card_sign_index, app_type_sign_index);//应用类型标识
                String card_app_version = data.substring(app_type_sign_index, card_app_version_index);//发卡方应用版本
                String app_serial_no = data.substring(card_app_version_index, app_serial_no_index);//应用序列号
                String app_start_date = data.substring(app_serial_no_index, app_start_date_index);//应用启用日期
                String app_valid_date = data.substring(app_start_date_index, app_valid_date_index);//应用失效日期
                String card_custom_fci = data.substring(app_valid_date_index, card_custom_fci_index);//发卡方自定义 FCI 数据
                String card_type_sign = data.substring(card_custom_fci_index, card_type_sign_index);//卡类型标识
                String internate_code = data.substring(card_type_sign_index, internate_code_index);//国际代码
                String province_code = data.substring(internate_code_index, province_code_index);//省际代码
                String city_code = data.substring(province_code_index, city_code_index);//城市代码
                String contact_card_type = data.substring(city_code_index, contact_card_type_index);//互通卡种
                String reserve = data.substring(contact_card_type_index, reserve_index);//预留


                // 解析数据域2
                String domesticKey = formatSecretkey(data);
                // 写入卡数据明细表
                // 据流水号查询制卡文件表
                TsmCardMakefile tsmCardMakefile1 = tsmCardMakefileMapper.selBySerialno(tsmCardMakefile);
                Long id = null;
                if (tsmCardMakefile1 != null) {
                    id = tsmCardMakefile1.getId();// 为制卡数据文件的id
                }
                TsmCardDetail tsmCardDetail = new TsmCardDetail();
                tsmCardDetail.setCadfileId(id);//卡文件id
                tsmCardDetail.setApplyCityCode(applyCityCode);// 卡申请城市代码
                tsmCardDetail.setAreaCode(area_code);//地区代码
                tsmCardDetail.setCardSpecies(card_species);//卡种类型
                tsmCardDetail.setCardNo(card_no);//卡片序列号
                // 实际入库卡标识去掉多余的空格
                card_sign = card_sign.replaceAll(" ", "");
                tsmCardDetail.setCardSign(card_sign);//发卡方标识
                tsmCardDetail.setCardAppVersion(card_app_version);//应用类型标识
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
                // 预留只取49*2=96
                reserve = reserve.substring(0, reserve.length() - 2);
                tsmCardDetail.setReserve(reserve);//预留
                tsmCardDetail.setInternationKey(domesticKey);//国际密钥 json
//                tsmCardDetail.setDomesticKey("");//国密密钥
                tsmCardDetail.setCardStatus("1");//卡状态 1入库2出库
                tsmCardDetail.setInDepositTime(new Date());//入库时间
//                tsmCardDetail.setOutDepositTime();//出库时间
                tsmCardDetail.setAppId(Constant.APP_ID);//应用id
                tsmCardDetail.setMerchantNo(Constant.MERCHANT_NO);//服务商号
                tsmCardDetail.setServiceOrderId("");
                tsmCardDetailMapper.insertSelective(tsmCardDetail);
            }
        } catch (Exception e) {
            logger.error("解析卡文件{}异常:{}", filename, e);
            return false;
        }
        return true;
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
        String reserve2 = data.substring(key2_index, reserve2_index); // 预留

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

    @Transactional
    public void downFromFTP() {
        // 下载至ftp并解析
        FtpUtils ftpUtils = new FtpUtils();
        ftpUtils.setServer(Constant.FTP_HOST);
        ftpUtils.setPort(Constant.FTP_PORT);
        ftpUtils.setUser(Constant.FTP_USER_NAME);
        ftpUtils.setPassword(Constant.FTP_PASSWORD);
        ftpUtils.setTimeout("30000");
        try {
            ftpUtils.connectServer(Constant.CREATECARD_DOWNLOAD_CATALOG);// test用户下的目录
            logger.info("登陆成功，开始下载文件:{}");
            // 获取指定ftp下目录的文件
            List<String> allFile = ftpUtils.getAllFile(Constant.CREATECARD_LOCAL_CATALOG);
            logger.info("获取文件:{}", allFile);
            if (CollectionUtil.isNotEmpty(allFile)) {
                for (String downloadfilename : allFile) {
                    // 解析下载的文件数据
                    boolean b = readFile(downloadfilename);
                    // 解析完毕将反馈文件移动至历史文件夹;
                    if (b) {
                        ftpUtils.moveFile(downloadfilename, Constant.CREATECARD_DOWNLOAD_HISTORY + downloadfilename);
                    } else {
                        // 卡文件读取异常移动至error文件夹
                        ftpUtils.moveFile(downloadfilename, Constant.CREATECARD_DOWNLOAD_ERROR_CATALOG + downloadfilename);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param filename   文件名
     * @param uploadPath 上传至FTP目录
     * @throws Exception
     */
    public void toFTP(String filename, String uploadPath, String localPath) throws Exception {
        // 上传至ftp
        FtpUtils ftpUtils = new FtpUtils();
        ftpUtils.setServer(Constant.CITY_FTP_HOST);
        ftpUtils.setPort(Constant.CITY_FTP_PORT);
        ftpUtils.setUser(Constant.CITY_FTP_USER_NAME);
        ftpUtils.setPassword(Constant.CITY_FTP_PASSWORD);
        ftpUtils.setTimeout("30000");
        ftpUtils.connectServer(uploadPath);
        logger.info("登陆成功，开始上传文件:{}", filename);
        ftpUtils.upload(localPath + filename, filename);// 本地路径,ftp路径
    }

    @Override
    public void cardInfoSynFile() {
        try {
            // 1.查询当日发卡信息
            List<TsmOpencardInfo> tsmOpencardInfos = tsmOpencardInfoMapper.selByToday();
            if (tsmOpencardInfos.isEmpty()) {
                return;
            }
            // 2.生成发卡信息同步文件
            logger.info("生成发卡信息同步文件开始，发卡信息数:{}", tsmOpencardInfos.size());
            String filename = createCardInfoSynFile(tsmOpencardInfos);
            // 3.上传ftp
            toFTP(filename, Constant.CARDINFO_SYNFILE_UPLOAD_CATALOG, Constant.CARDINFO_SYNFILE_LOCAL_CATALOG);
            logger.info("上传发卡同步文件完成:{}", filename);
            // 4.插入发卡信息同步表
            TsmOpencardSyncfile tsmOpencardSyncfile = new TsmOpencardSyncfile();
            tsmOpencardSyncfile.setCityCode(Constant.CITY_CODE);
            tsmOpencardSyncfile.setCardissuefile(filename);
            tsmOpencardSyncfile.setFtppath(Constant.CARDINFO_SYNFILE_UPLOAD_CATALOG + filename);
            tsmOpencardSyncfile.setCardissuenum(tsmOpencardInfos.size());
            tsmOpencardSyncfile.setStarttime(new Date());
            tsmOpencardSyncfile.setEndtime(new Date());
            tsmOpencardSyncfile.setCreatetime(new Date());
            tsmOpencardSyncfileMapper.insertSelective(tsmOpencardSyncfile);
        } catch (Exception e) {
            logger.error("生成发卡信息异常:{}", e);
        }
    }

    // 解析卡消费文件并入库
    @Override
    public void analysisCardConsumRecord() {
        // 1.下载
        downCardConsumFile();
    }

    // 下载卡消费文件
    private void downCardConsumFile() {
        // 下载至ftp并解析
        FtpUtils ftpUtils = new FtpUtils();
        ftpUtils.setServer(Constant.FTP_HOST);
        ftpUtils.setPort(Constant.FTP_PORT);
        ftpUtils.setUser(Constant.FTP_USER_NAME);
        ftpUtils.setPassword(Constant.FTP_PASSWORD);
        ftpUtils.setTimeout("30000");
        try {
            ftpUtils.connectServer(Constant.CONSUMEFILE_DOWNLOAD_CATALOG);// test用户下的目录
            logger.info("登陆成功，开始下载文件:{}");
            // 获取指定ftp下目录的文件
            List<String> allFile = ftpUtils.getAllFile(Constant.CONSUMEFILE_LOCAL_CATALOG);
            logger.info("获取文件:{}", allFile);
            if (CollectionUtil.isNotEmpty(allFile)) {
                for (String downloadfilename : allFile) {
                    // 解析下载的文件数据
                    boolean b = readconsumFile(downloadfilename);
                    // 解析完毕将反馈文件移动至历史文件夹;
                    if (b) {
                        ftpUtils.moveFile(downloadfilename, Constant.CONSUMEFILE_DOWNLOAD_HISTORY + downloadfilename);
                    } else {
                        // 卡文件读取异常移动至error文件夹
                        ftpUtils.moveFile(downloadfilename, Constant.CONSUMEFILE_DOWNLOAD_ERROR_CATALOG + downloadfilename);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean readconsumFile(String filename) {
        String path = Constant.CONSUMEFILE_LOCAL_CATALOG + filename;
        List<String> list = null;
        try {
            list = FileUtils.getFile(path);
            // 1.第一行文件说明区
            String fileremark = list.get(0);
            // 数据体
            List<String> body = list.subList(1, list.size());
            ArrayList<TsmCardConsumedetail> consumedetails = new ArrayList<>();
            List<AccountConsumeReq> accountConsumeReqs = new ArrayList<>();
            for (String data : body) {
                String local_serialno = data.substring(0, local_serialno_index);// 本地流水号
                String operating_unit = data.substring(local_serialno_index, operating_unit_index); // 企业运营系统下的营运单位代码
                String citycode = data.substring(operating_unit_index, citycode_index);//城市代码
                String terminal_code = data.substring(citycode_index, terminal_code_index);//终端机编码
                String cardno = data.substring(terminal_code_index, cardno_index);//卡内号
                String cardconsume_count = data.substring(cardno_index, cardconsume_count_index);//卡消费计数器
                String balance = data.substring(cardconsume_count_index, balance_index);//消费前卡余额/余次
                String transaction_amount = data.substring(balance_index, transaction_amount_index);//交易金额/次数
                String transaction_date = data.substring(transaction_amount_index, transaction_date_index);//交易发生日期
                String transaction_time = data.substring(transaction_date_index, transaction_time_index);//交易发生时间
                String industry_company_no = data.substring(transaction_time_index, industry_company_no_index);//行业内公司编号

                TsmCardConsumedetail tsmCardConsumedetail = new TsmCardConsumedetail();
                tsmCardConsumedetail.setLocalSerialNumber(local_serialno);
                tsmCardConsumedetail.setUnitCode(operating_unit);
                tsmCardConsumedetail.setCityCodeTransaction(citycode);
                tsmCardConsumedetail.setTerminalNo(terminal_code);
                tsmCardConsumedetail.setCardNo(cardno);
                tsmCardConsumedetail.setCardConsumeCounter(cardconsume_count);
                tsmCardConsumedetail.setBeforeconsumeCardbalance(balance);
                tsmCardConsumedetail.setTransactionAmout(transaction_amount);
                tsmCardConsumedetail.setTransactionDate(transaction_date);
                tsmCardConsumedetail.setTransactionTime(transaction_time);
                tsmCardConsumedetail.setCompanyNo(industry_company_no);
                consumedetails.add(tsmCardConsumedetail);


                AccountConsumeReq accountConsumeReq = new AccountConsumeReq();
                accountConsumeReq.setSendSeq(local_serialno);
//                BigInteger txnAmt = new BigInteger(transaction_amount, 16);
                accountConsumeReq.setTxnAmt(transaction_amount);
                accountConsumeReq.setTermId(terminal_code);
                accountConsumeReq.setTxnDate(transaction_date);
                accountConsumeReq.setTxnTime(transaction_time);
                accountConsumeReq.setCardNo(cardno);
                accountConsumeReq.setIssOrgCode(citycode);
                accountConsumeReq.setCityCode(citycode);
                accountConsumeReq.setCardDebitCnt(cardconsume_count);
                accountConsumeReq.setBefBal(balance);
                accountConsumeReq.setRefuseRsn("000000");
                accountConsumeReq.setCreatedDate(new Date());
                accountConsumeReqs.add(accountConsumeReq);
            }
            // 入库消费文件表
            TsmCardConsumefile tsmCardConsumefile = new TsmCardConsumefile();
            tsmCardConsumefile.setConsumefileName(filename);
            tsmCardConsumefile.setConsumefileFtppath(path);
            tsmCardConsumefile.setRecordNum(consumedetails.size());
            tsmCardConsumefile.setConsumefileCreatetime(new Date());
            tsmCardConsumefileMapper.insertSelective(tsmCardConsumefile);

            TsmCardConsumefile tsmCardConsumefile1 = tsmCardConsumefileMapper.selectByfilename(tsmCardConsumefile);
            consumedetails.forEach(tsmCardConsumedetail -> tsmCardConsumedetail.setConsumecadfileId(tsmCardConsumefile1.getId()));
            // 入库消费详情表
            tsmCardConsumedetailMapper.insertBatch(consumedetails);
            // 同步和包出行数据库消费数据表
            boolean b = synConsumeToTravel(accountConsumeReqs);

        } catch (Exception e) {
            logger.error("解析卡文件{}异常:{}", filename, e);
            return false;
        }
        return true;
    }

    // 同步和包出行数据库消费数据表
    private boolean synConsumeToTravel(List<AccountConsumeReq> list) {
        try {
            String req = JSON.toJSONString(list);
            logger.info("发送同步消费数据至出行平台请求报文:{}", req);
            String res = HttpRequestUtils.doPost(Constant.SYN_CONSUME_TOTRAVEL_URL, req);
            logger.info("返回同步消费数据至出行平台请求报文:{}", res);
            String code = JSON.parseObject(res).getString("code");
            if ("200".equals(code)) {
                return true;
            }
            logger.info("返回同步消费数据至出行平台请求失败:{}", res);
            return false;
        } catch (Exception e) {
            logger.info("同步消费数据至出行平台异常:{}", e);
            return false;
        }
    }

    private String createCardInfoSynFile(List<TsmOpencardInfo> tsmOpencardInfos) throws Exception {
        String cardInfoNum = String.valueOf(tsmOpencardInfos.size());
        String version = Constant.VERSION;// 版本号
        String recordNum = cardInfoNum;// 记录总数
        String city_code = Constant.CITY_CODE;
        String requestType = "01";// 用户卡
        String area_code = Constant.AREA_CODE;
        StringBuffer sb = new StringBuffer();
        sb.append(recordNum);
        sb.append(city_code);
        sb.append(requestType);


        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        String date_2 = df.format(new Date().getTime());
        String serialno = IdUtil.get10Serialno();// 流水号
        String filename = "QR" + date_2 + city_code + serialno + ".dat";
        String path = Constant.CARDINFO_SYNFILE_LOCAL_CATALOG + filename;
        List<String> accountList = new ArrayList<>();
        accountList.add(version);
        accountList.add(sb.toString());
        accountList.add("FFFFFFFFFFFFFFFFFFFF");
        // 数据体
        tsmOpencardInfos.forEach(tsmOpencardInfo -> {
            StringBuffer body = new StringBuffer();
            String cardNo = tsmOpencardInfo.getCardNo();
            String cardSpecies = tsmOpencardInfo.getCardSpecies();
            body.append(cardNo);
            body.append("01"); // 启用
            body.append(area_code);
            body.append(cardSpecies);
            body.append("0000");// 押金
            accountList.add(body.toString());
        });
        FileUtils.writeFileContext(accountList, path);
        return filename;
    }

}
