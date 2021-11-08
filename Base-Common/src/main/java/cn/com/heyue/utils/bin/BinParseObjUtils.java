package cn.com.heyue.utils.bin;

import cn.com.heyue.utils.HexStringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;
import sun.misc.BASE64Decoder;

/**
 * @author fuzuyuan
 * @Description
 * @create 2020-11-11 2:13 下午
 */
public class BinParseObjUtils {
    /**
     * 解析B001
     *
     * @param jsonObject 要返回的json
     * @param content    原始内容
     */
    private static void parseB001(JSONObject jsonObject, String content) {
        String cardIdentify = content.substring(0, 16); // 发卡方标识
        String appIdentify = content.substring(16, 18); // 应用类型标识
        String appVersion = content.substring(18, 20); // 发卡方应用版本
        String appSeq = content.substring(20, 40); // 应用序列号
        String appStartDate = content.substring(40, 48); // 应用启用日期
        String appEndDate = content.substring(48, 56); // 应用有效日期
        String fci = content.substring(56, 60); // FCI
        jsonObject.put("cardIdentify", cardIdentify);
        jsonObject.put("appIdentify", appIdentify);
        jsonObject.put("appVersion", appVersion);
        jsonObject.put("appSeq", appSeq);
        jsonObject.put("appStartDate", appStartDate);
        jsonObject.put("appEndDate", appEndDate);
        jsonObject.put("fci", fci);
    }

    /**
     * 解析B002
     *
     * @param jsonObject 要返回的json
     * @param content    原始内容
     */
    private static void parseB002(JSONObject jsonObject, String content) {
        String cardTypeIdentify = content.substring(0, 2); // 发类型标识
        String staff = content.substring(2, 4); // 本行职工标识
        String name = content.substring(4, 44); // 持卡人姓名
        String cardNo = content.substring(44, 108); // 持卡人证件号码
        String cardType = content.substring(108, 110); // 持卡人证件类型
        jsonObject.put("cardTypeIdentify", cardTypeIdentify);
        jsonObject.put("staff", staff);
        jsonObject.put("name", name);
        jsonObject.put("cardNo", cardNo);
        jsonObject.put("cardType", cardType);
    }

    /**
     * 解析B003
     *
     * @param jsonObject 要返回的json
     * @param content    原始内容
     */
    private static void parseB003(JSONObject jsonObject, String content) {
        String worldCode = content.substring(0, 8); // 国际代码
        String proviceCode = content.substring(8, 12); // 省级代码
        String cityCode = content.substring(12, 16); // 城市代码
        String uniCardType = content.substring(16, 20); // 互通卡种
        String cardType = content.substring(20, 22); // 卡种类型
        String reserver = content.substring(22, 120); // 预留
        jsonObject.put("worldCode", worldCode);
        jsonObject.put("proviceCode", proviceCode);
        jsonObject.put("cityCode", cityCode);
        jsonObject.put("uniCardType", uniCardType);
        jsonObject.put("cardType", cardType);
        jsonObject.put("reserver", reserver);
    }

    /**
     * 解析B0A0
     *
     * @param jsonObjectSrc 要返回的json
     * @param content       原始内容
     */
    private static void parseB0A0(JSONObject jsonObjectSrc, String content) {
        String cardPriKey = content.substring(0, 38); // 用户卡主控密钥
        String cardStickKey = content.substring(38, 76); // 用户卡维护密钥
        String cardAppPriKey = content.substring(76, 114); // 用户卡应用主控密钥
        String cardAppStickKey = content.substring(114, 152); // 用户卡应用维护密钥
        String cousumeKey = content.substring(152, 190); // 消费密钥
        String rechargeKey = content.substring(190, 228); // 充值密钥
        String tacKey = content.substring(228, 266); // TAC 密钥
        String cardAppStickLockKey = content.substring(266, 304); // 用户卡应用维护密钥（应用锁定）
        String pinKey = content.substring(304, 342); // PIN 密钥
        String cashKey = content.substring(342, 380); // 互通记录保护密钥-电子现金
        String walletKey = content.substring(380, 418); // 互通记录保护密钥-电子钱包
        String cashKey2 = content.substring(418, 456); // 互通记录保护密钥（现金备用）
        String appDelLockKey = content.substring(456, 494); // 用户卡应用维护密钥（应用解锁）
        String reserverKey1 = content.substring(494, 532); // 预留密钥
        String reserverKey2 = content.substring(532, 570); // 预留密钥
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cardPriKey", cardPriKey);
        jsonObject.put("cardStickKey", cardStickKey);
        jsonObject.put("cardAppPriKey", cardAppPriKey);
        jsonObject.put("cardAppStickKey", cardAppStickKey);
        jsonObject.put("cousumeKey", cousumeKey);
        jsonObject.put("rechargeKey", rechargeKey);
        jsonObject.put("tacKey", tacKey);
        jsonObject.put("cardAppStickLockKey", cardAppStickLockKey);
        jsonObject.put("pinKey", pinKey);
        jsonObject.put("cashKey", cashKey);
        jsonObject.put("walletKey", walletKey);
        jsonObject.put("cashKey2", cashKey2);
        jsonObject.put("appDelLockKey", appDelLockKey);
        jsonObject.put("reserverKey1", reserverKey1);
        jsonObject.put("reserverKey2", reserverKey2);
        jsonObjectSrc.put("keyData", jsonObject);
    }

    /**
     * 解析B005
     *
     * @param jsonObject 要返回的json
     * @param content    原始内容
     */
    private static void parseB0A5(JSONObject jsonObject, String content) {
        String rechargeKey2 = content.substring(0, 38); // 国际代码
        jsonObject.put("rechargeKey2", rechargeKey2);
    }

    /**
     * 要解析的内容
     *
     * @param content
     * @return
     */
    public static JSONObject parseBin(String content) {
        JSONObject jsonObject = new JSONObject();
        String INFO = content.substring(0, 24);  // 此信息未知，文档未注明，长度固定24
        String MIC = content.substring(24, 54);  // MIC 信息
        int LCCA = Integer.parseInt(content.substring(54, 70), 16);  // LCCA 长度
        int DGINum = Integer.parseInt(content.substring(70, 78), 16);  // DGINum 长度
        jsonObject.put("INFO", INFO);
        jsonObject.put("MIC", HexStringUtils.hexStringToString(MIC));
        jsonObject.put("LCCA", LCCA);
        jsonObject.put("DGINum", DGINum);
        // DGI数组，个数=DGINum
        JSONArray jsonArray = new JSONArray();
        int index = 78; // 目前索引
        // 取part2
        for (int i = 0; i < DGINum; i++) {
            int DGINameLen = Integer.parseInt(content.substring(index, index + 4), 16);  // DGINameLen 长度
            index = index + 4; // 下移
            String DGIName = HexStringUtils.hexStringToString(content.substring(index, index + DGINameLen * 2)); // 向后取 DGINameLen*2 长度
            index = index + DGINameLen * 2; // 下移

            JSONObject dgiJsonObject = new JSONObject();
            dgiJsonObject.put("DGIName", DGIName);

            jsonArray.add(dgiJsonObject);
        }

        String SeqNo = content.substring(index, index + 8);  // SeqNo 卡片序列号
        jsonObject.put("SeqNo", SeqNo);
        index = index + 8; // 下移
        String LDATA = content.substring(index, index + 4);  // LDATA 该张卡片后续所有数据的长度
        jsonObject.put("LDATA", LDATA);
        index = index + 4; // 下移
        for (int i = 0; i < DGINum; i++) {
            JSONObject dgiJsonObject = jsonArray.getJSONObject(i);

            String DGIStart = content.substring(index, index + 2);  // DGIStart 起始 86
            dgiJsonObject.put("DGIStart", DGIStart);
            index = index + 2; // 下移

            int DGI_len = Integer.parseInt(content.substring(index, index + 2), 16);  // DGI_len 检查长度是否大于0x80=128
            dgiJsonObject.put("DGI_len", DGI_len);
            index = index + 2; // 下移

            if (DGI_len < 128) {
                // 小于128，一个字节
                String DGI = content.substring(index, index + 4);  // DGI
                dgiJsonObject.put("DGI", DGI_len);
                index = index + 4; // 下移

                int DGILen = Integer.parseInt(content.substring(index, index + 2), 16);  // DGILen DGI数据长度
                dgiJsonObject.put("DGILen", DGILen);
                index = index + 2; // 下移

                String DGIContent = content.substring(index, index + DGILen * 2);  // DGILen DGI数据长度
                //设置dgi
                setDgi(dgiJsonObject, DGIContent, DGI);
                index = index + DGILen * 2; // 下移
            } else {
                // 大于128，二个字节
                //index = index + 2; // 需要下移四个
                //获取取值数
                String DGIl = content.substring(index - 2, index);
                Integer num = Integer.parseInt(DGIl, 16);
                Integer dl = 0;
                if(num >= 130) {
                    dl = Integer.parseInt(content.substring(index, index + 4), 16);
                    index = index + 4;
                } else {
                    dl = Integer.parseInt(content.substring(index, index + 2), 16);
                    index = index + 2;
                }
                //设置数据长度偏移
                int offset = 2;
                if((dl - 4) >= 256) {
                    offset = 4;
                }

                String DGI = content.substring(index, index + 4);  // DGI
                dgiJsonObject.put("DGI", DGI_len);
                index = index + 4; // 下移

                int DGILen = Integer.parseInt(content.substring(index, index + offset), 16);  // DGILen DGI数据长度
                dgiJsonObject.put("DGILen", DGILen);
                index = index + offset; // 下移

                String DGIContent = content.substring(index, index + DGILen * 2);  // DGILen DGI数据长度
                //设置dgi
                setDgi(dgiJsonObject, DGIContent, DGI);
                index = index + DGILen * 2; // 下移
            }
        }

        jsonObject.put("DGIArray", jsonArray);

        System.out.println(jsonObject.toJSONString());
        return jsonObject;
    }

    //设置dgi
    private static void setDgi(JSONObject dgiJsonObject, String DGIContent, String DGI) {
        if (StringUtils.endsWithIgnoreCase("B001", DGI)) {
            //公共应用信息文件
            parseB001(dgiJsonObject, DGIContent);
        } else if (StringUtils.endsWithIgnoreCase("B002", DGI)) {
            //持卡人基本信息文件
            parseB002(dgiJsonObject, DGIContent);
        } else if (StringUtils.endsWithIgnoreCase("B003", DGI)) {
            //管理信息文件
            parseB003(dgiJsonObject, DGIContent);
        } else if (StringUtils.endsWithIgnoreCase("B0A0", DGI)) {
            //密钥文件(国际)
            parseB0A0(dgiJsonObject, DGIContent);
        } else if (StringUtils.endsWithIgnoreCase("B0A5", DGI)) {
            //充值密钥2(国际)
            parseB0A5(dgiJsonObject, DGIContent);
        } else if (StringUtils.endsWithIgnoreCase("B1A0", DGI)) {
            //密钥文件(SM4)
            parseB0A0(dgiJsonObject, DGIContent);
        } else if (StringUtils.endsWithIgnoreCase("B1A5", DGI)) {
            //充值密钥2(SM4)
            parseB0A5(dgiJsonObject, DGIContent);
        }
    }

    /**
     * 根据base64字符串，转义并解析成卡片对象
     * @param base64
     * @return
     */
    public static JSONObject parseByBase64(String base64) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = decoder.decodeBuffer(base64);
        String result = HexStringUtils.bytesToHexString(bytes);
        return BinParseObjUtils.parseBin(result);
    }

    public static void main(String[] args) throws Exception {
        String s = "kdFTYZAadShWMDEtVFVuaW9uRFBHQ1RZVjAxAAAAAAAAFNQAAAA/AAdER0kwMTAxAAdER0kwMTAyAAdER0kwMzAxAAdER0kwMzAyAAdER0kwNDAxAAdER0kwNTAxAAdER0kwNzAxAAdER0kwNzAyAAdER0kwRDAxAAdER0kwRTAxAAdER0k5MTAyAAdER0k5MTAzAAdER0lBMDAxAAdER0k4MDEwAAdER0k5MDEwAAdER0kwMDk4AAdER0kwMDk5AAdER0kwMTAwAAdER0kwMjAxAAdER0kwMjAyAAdER0kwMjAzAAdER0kwMjA0AAdER0kwMjA1AAdER0kwNzAzAAdER0kwNzA0AAdER0kwNzA1AAdER0k5MTA0AAdER0k5MjAwAAdER0k5MjAzAAdER0k5MjA3AAdER0k4MDAwAAdER0k5MDAwAAdER0k3Nzc3AAdER0k4MjAxAAdER0k4MjAyAAdER0k4MjAzAAdER0k4MjA0AAdER0k4MjA1AAdER0k4MDIwAAdER0k5MDIwAAdER0kwODAxAAdER0kwODAyAAdER0kwODAzAAdER0kwOTAxAAdER0kwOTAyAAdER0k5MTA1AAdER0k5MjAxAAdER0k5MjA0AAdER0k5MjA4AAdER0k4NDAwAAdER0k5MDAzAAdER0k3NzcxAAdER0k4NDAyAAdER0k4MDIxAAdER0k5MDIxAAdER0k4MDExAAdER0lCMDAxAAdER0lCMDAyAAdER0lCMDAzAAdER0lCMEEwAAdER0lCMEE1AAdER0lCMUEwAAdER0lCMUE1AAAAARKThhoBARdwFVcTMQSJURIQAAAAXUASIgAAAHmQD4ZJAQJGcESfYSAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIJ9iAQBfIBoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIaBjwMBjHCBiVoKMQSJURIQAAAAX18kA0ASMV8lAyECAl80AQCfBwL/AI4MAAAAAAAAAAACAx8AXygCAVafDQXYYASoAJ8OBQAQmAAAnw8F2GgE+ACfSgGCjBufAgafAwafGgKVBV8qApoDnAGfNwSfIQOfThSNGooCnwIGnwMGnxoClQVfKgKaA58hA5wBnzcEhi4DAitwKZ9CAgFWXzACAiCfCAIAMJ9EAQKfCxMAAAAAAAAAAAAA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";
        JSONObject j = BinParseObjUtils.parseByBase64(s);
        System.out.println(j.toJSONString());
    }

//    public static void main(String[] args) {
//        String content = "91D15361901A75285630312D54556E696F6E44504743545956303100000000000003630000000700074447494230303100074447494230303200074447494230303300074447494230413000074447494230413500074447494231413000074447494231413500000001031A8621B0011E05212482FFFFFFFF02000310489519990000000220201020204012310000863AB0023700002020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202000863FB0033C00000156240024820001010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000086820121B0A0011D0452647C2B7A3D5EF4FF43E6EFFCF5C4CC0EE66968C4E76D2700D0501C1AB73956CDB7FA49237D82E526414E8E43D3799137E269C718E18B126EC2048442141071A5A346A4EC722E522B5C20F684D8FBFC51AF797BEB1CFA26247F719506B45FD898095770DB166957D2428A52794291F29B060B3A176FF6662CEE03A453E7DA5B15496B305BF611F71D375256A266036293A7957CED0F4D6D0A30E6F2869B2C5F03E1EFA34ADC5161B7F02872C85E0E6BAEF8BF210D160A9821B48E732E6352E9D19DCBAC6DE5040EE1E66AABDCB1DF52A0204A6C34780937D6AC2E367E788C14EA3CEE5BF611F71D375256A266036293A7957CED0F4D6EC2048442141071A5A346A4EC722E522B5C206EC2048442141071A5A346A4EC722E522B5C208616B0A513791FB5D12756331E00F0E0E2501AC90E09520F86820121B1A0011DB6441FBB6F54CB0681DCB2AFD3EEE6E5BB1203DF206F46A8F7A1CD88B7EAD6774017B91BD62DA2E92CAB25C4D1EF103B4A91ED4D2C0890F5066927A1226E4459BE7CA61C17F26B83734710F564163806ACAD3E22D1404C684F979E498317BF12DC0C6F7E9E848598C7E7CE7DA98FE1EB23EC225773C9DC05BBA85D7B578DA1B9F45F2798D4965C8434CDE9FB79236529738EF39920099334D9996063079DAE5D7314F01BA396BB1B2900452779884156D9B8B6855193EF8074996957EA5FD8CE8FDE6D01C6C34C800E13D220A22A856FBB5FF9C1E48948FEA9698793E5D4F743F1CE94B1965C8434CDE9FB79236529738EF399200993346927A1226E4459BE7CA61C17F26B83734710F56927A1226E4459BE7CA61C17F26B83734710F58616B1A513791FB5D12756331E00F0E0E2501AC90E09520F";
//        BinParseUtils.parseBin(content);
//    }

    public static void mainT(String[] args) {
        FileContentDO fileContentDO = new FileContentDO();
        InternationalKey internationalKey = new InternationalKey();
        DomesticKey domesticKey = new DomesticKey();
        String content = "91D15361901A75285630312D54556E696F6E44504743545956303100000000000003630000000700074447494230303100074447494230303200074447494230303300074447494230413000074447494230413500074447494231413000074447494231413500000001031A8621B0011E05212482FFFFFFFF02000310489519990000000220201020204012310000863AB0023700002020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202000863FB0033C00000156240024820001010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000086820121B0A0011D0452647C2B7A3D5EF4FF43E6EFFCF5C4CC0EE66968C4E76D2700D0501C1AB73956CDB7FA49237D82E526414E8E43D3799137E269C718E18B126EC2048442141071A5A346A4EC722E522B5C20F684D8FBFC51AF797BEB1CFA26247F719506B45FD898095770DB166957D2428A52794291F29B060B3A176FF6662CEE03A453E7DA5B15496B305BF611F71D375256A266036293A7957CED0F4D6D0A30E6F2869B2C5F03E1EFA34ADC5161B7F02872C85E0E6BAEF8BF210D160A9821B48E732E6352E9D19DCBAC6DE5040EE1E66AABDCB1DF52A0204A6C34780937D6AC2E367E788C14EA3CEE5BF611F71D375256A266036293A7957CED0F4D6EC2048442141071A5A346A4EC722E522B5C206EC2048442141071A5A346A4EC722E522B5C208616B0A513791FB5D12756331E00F0E0E2501AC90E09520F86820121B1A0011DB6441FBB6F54CB0681DCB2AFD3EEE6E5BB1203DF206F46A8F7A1CD88B7EAD6774017B91BD62DA2E92CAB25C4D1EF103B4A91ED4D2C0890F5066927A1226E4459BE7CA61C17F26B83734710F564163806ACAD3E22D1404C684F979E498317BF12DC0C6F7E9E848598C7E7CE7DA98FE1EB23EC225773C9DC05BBA85D7B578DA1B9F45F2798D4965C8434CDE9FB79236529738EF39920099334D9996063079DAE5D7314F01BA396BB1B2900452779884156D9B8B6855193EF8074996957EA5FD8CE8FDE6D01C6C34C800E13D220A22A856FBB5FF9C1E48948FEA9698793E5D4F743F1CE94B1965C8434CDE9FB79236529738EF399200993346927A1226E4459BE7CA61C17F26B83734710F56927A1226E4459BE7CA61C17F26B83734710F58616B1A513791FB5D12756331E00F0E0E2501AC90E09520F";
        JSONObject jsonObject = BinParseObjUtils.parseBin(content);
        jsonObject.get("SeqNo").toString();
        String dgiNum = jsonObject.get("DGINum").toString();
        int dgiLen = Integer.parseInt(dgiNum);
        int count =0;
        fileContentDO.setSeqNo(jsonObject.get("SeqNo").toString());
        JSONArray dgiArray = jsonObject.getJSONArray("DGIArray");
        if (count<dgiLen){
            JSONObject temp1 = (JSONObject) dgiArray.get(0);
            if (!ObjectUtils.isEmpty(temp1)){
                if (temp1.containsKey("cardIdentify")){
                    String cardIdentify = temp1.get("cardIdentify").toString();
                    fileContentDO.setCardSign(cardIdentify);
                }
                if (temp1.containsKey("appIdentify")){
                    String appIdentify = temp1.get("appIdentify").toString();
                    fileContentDO.setAppTypeSign(appIdentify);
                }
                if (temp1.containsKey("appVersion")){
                    String appVersion = temp1.get("appVersion").toString();
                    fileContentDO.setCardAppVersion(appVersion);
                }
                if (temp1.containsKey("appSeq")){
                    String appSeq = temp1.get("appSeq").toString();
                    fileContentDO.setAppSerialNo(appSeq);
                }
                if (temp1.containsKey("appStartDate")){
                    String appStartDate = temp1.get("appStartDate").toString();
                    fileContentDO.setAppStartDate(appStartDate);
                }
                if (temp1.containsKey("appEndDate")){
                    String appEndDate = temp1.get("appEndDate").toString();
                    fileContentDO.setAppValidDate(appEndDate);
                }
                if (temp1.containsKey("fci")){
                    String fci = temp1.get("fci").toString();
                    fileContentDO.setCardCustomFci(fci);
                }
            }
            count++;
        }
        if (count<dgiLen){
            JSONObject temp2 = (JSONObject) dgiArray.get(1);
            if (!ObjectUtils.isEmpty(temp2)){
                if (temp2.containsKey("cardTypeIdentify")){
                    String cardTypeIdentify = temp2.get("cardTypeIdentify").toString();
                    fileContentDO.setCardTypeSign(cardTypeIdentify);
                }
                if (temp2.containsKey("staff")){
                    String staff = temp2.get("staff").toString();
                    fileContentDO.setEmployeeSign(staff);
                }
                if (temp2.containsKey("name")){
                    String name = temp2.get("name").toString();
                    fileContentDO.setCarderName(name);
                }
                if (temp2.containsKey("cardNo")){
                    String cardNo = temp2.get("cardNo").toString();
                    fileContentDO.setCarderIdNo(cardNo);
                }
                if (temp2.containsKey("cardType")){
                    String cardType = temp2.get("cardType").toString();
                    fileContentDO.setCarderIdType(cardType);
                }
            }
            count++;
        }
        if (count<dgiLen){
            JSONObject temp3 = (JSONObject) dgiArray.get(2);
            if (!ObjectUtils.isEmpty(temp3)){
                if (temp3.containsKey("worldCode")){
                    String worldCode = temp3.get("worldCode").toString();
                    fileContentDO.setInternateCode(worldCode);
                }
                if (temp3.containsKey("proviceCode")){
                    String proviceCode = temp3.get("proviceCode").toString();
                    fileContentDO.setProvinceCode(proviceCode);
                }
                if (temp3.containsKey("cityCode")){
                    String cityCode = temp3.get("cityCode").toString();
                    fileContentDO.setCityCode(cityCode);
                }
                if (temp3.containsKey("uniCardType")){
                    String uniCardType = temp3.get("uniCardType").toString();
                    fileContentDO.setContactCardType(uniCardType);
                }
                if (temp3.containsKey("cardType")){
                    String cardType = temp3.get("cardType").toString();
                    fileContentDO.setCardType(cardType);
                }

            }
            count++;
        }

        if (count<dgiLen){
            JSONObject temp3 = (JSONObject) dgiArray.get(3);
            if (!ObjectUtils.isEmpty(temp3)){
                JSONObject keyDataTemp = temp3.getJSONObject("keyData");
//                JSONObject keyDataTemp = (JSONObject)keyData.get(0);
                if (!ObjectUtils.isEmpty(keyDataTemp)){
                    if (keyDataTemp.containsKey("cousumeKey")){
                        String cousumeKey = keyDataTemp.get("cousumeKey").toString();
                        internationalKey.setConsumeKey(cousumeKey);
                    }
                    if (keyDataTemp.containsKey("cardAppStickLockKey")){
                        String cardAppStickLockKey = keyDataTemp.get("cardAppStickLockKey").toString();
                        internationalKey.setUserLockAppMaintainKey(cardAppStickLockKey);
                    }
                    if (keyDataTemp.containsKey("cardAppStickKey")){
                        String cardAppStickKey = keyDataTemp.get("cardAppStickKey").toString();
                        internationalKey.setUserAppMaintainKey(cardAppStickKey);
                    }
                    if (keyDataTemp.containsKey("cardPriKey")){
                        String cardPriKey = keyDataTemp.get("cardPriKey").toString();
                        internationalKey.setUserCardKey(cardPriKey);
                    }
                    if (keyDataTemp.containsKey("cashKey2")){
                        String cashKey2 = keyDataTemp.get("cashKey2").toString();
                        internationalKey.setContactWalletSpareKey(cashKey2);
                    }
                    if (keyDataTemp.containsKey("cardStickKey")){
                        String cardStickKey = keyDataTemp.get("cardStickKey").toString();
                        internationalKey.setUserCardMaintainKey(cardStickKey);
                    }
                    if (keyDataTemp.containsKey("cardAppPriKey")){
                        String cardAppPriKey = keyDataTemp.get("cardAppPriKey").toString();
                        internationalKey.setUserAppKey(cardAppPriKey);
                    }
                    if (keyDataTemp.containsKey("pinKey")){
                        String pinKey = keyDataTemp.get("pinKey").toString();
                        internationalKey.setPinKey(pinKey);
                    }
                    if (keyDataTemp.containsKey("reserverKey1")){
                        String reserverKey1 = keyDataTemp.get("reserverKey1").toString();
                        internationalKey.setReserveKey1(reserverKey1);
                    }
                    if (keyDataTemp.containsKey("walletKey")){
                        String walletKey = keyDataTemp.get("walletKey").toString();
                        internationalKey.setContactEwalletKey(walletKey);
                    }
                    if (keyDataTemp.containsKey("reserverKey2")){
                        String reserverKey2 = keyDataTemp.get("reserverKey2").toString();
                        internationalKey.setReserveKey2(reserverKey2);
                    }
                    if (keyDataTemp.containsKey("rechargeKey")){
                        String rechargeKey = keyDataTemp.get("rechargeKey").toString();
                        internationalKey.setRechargeKey(rechargeKey);
                    }
                    if (keyDataTemp.containsKey("tacKey")){
                        String tacKey = keyDataTemp.get("tacKey").toString();
                        internationalKey.setTacKey(tacKey);
                    }
                    if (keyDataTemp.containsKey("cashKey")){
                        String cashKey = keyDataTemp.get("cashKey").toString();
                        internationalKey.setContactEcashKey(cashKey);
                    }
                    if (keyDataTemp.containsKey("appDelLockKey")){
                        String appDelLockKey = keyDataTemp.get("appDelLockKey").toString();
                        internationalKey.setUserUnlockAppMaintainKey(appDelLockKey);
                    }
                }
            }
            count++;
        }
        //DGI5不需要保存
        if (count<dgiLen){
            JSONObject temp4 = (JSONObject) dgiArray.get(4);
            if (!ObjectUtils.isEmpty(temp4)){
            }
            count++;
        }
        if (count<dgiLen){
            JSONObject temp5 = (JSONObject) dgiArray.get(5);
            if (!ObjectUtils.isEmpty(temp5)){
                JSONObject keyDataTemp = temp5.getJSONObject("keyData");
                if (!ObjectUtils.isEmpty(keyDataTemp)){
                    if (keyDataTemp.containsKey("cousumeKey")){
                        String cousumeKey = keyDataTemp.get("cousumeKey").toString();
                        domesticKey.setConsumeKey(cousumeKey);
                    }
                    if (keyDataTemp.containsKey("cardAppStickLockKey")){
                        String cardAppStickLockKey = keyDataTemp.get("cardAppStickLockKey").toString();
                        domesticKey.setUserLockAppMaintainKey(cardAppStickLockKey);
                    }
                    if (keyDataTemp.containsKey("cardAppStickKey")){
                        String cardAppStickKey = keyDataTemp.get("cardAppStickKey").toString();
                        domesticKey.setUserAppMaintainKey(cardAppStickKey);
                    }
                    if (keyDataTemp.containsKey("cardPriKey")){
                        String cardPriKey = keyDataTemp.get("cardPriKey").toString();
                        domesticKey.setUserCardKey(cardPriKey);
                    }
                    if (keyDataTemp.containsKey("cashKey2")){
                        String cashKey2 = keyDataTemp.get("cashKey2").toString();
                        domesticKey.setContactWalletSpareKey(cashKey2);
                    }
                    if (keyDataTemp.containsKey("cardStickKey")){
                        String cardStickKey = keyDataTemp.get("cardStickKey").toString();
                        domesticKey.setUserCardMaintainKey(cardStickKey);
                    }
                    if (keyDataTemp.containsKey("cardAppPriKey")){
                        String cardAppPriKey = keyDataTemp.get("cardAppPriKey").toString();
                        domesticKey.setUserAppKey(cardAppPriKey);
                    }
                    if (keyDataTemp.containsKey("pinKey")){
                        String pinKey = keyDataTemp.get("pinKey").toString();
                        domesticKey.setPinKey(pinKey);
                    }
                    if (keyDataTemp.containsKey("reserverKey1")){
                        String reserverKey1 = keyDataTemp.get("reserverKey1").toString();
                        domesticKey.setReserveKey1(reserverKey1);
                    }
                    if (keyDataTemp.containsKey("walletKey")){
                        String walletKey = keyDataTemp.get("walletKey").toString();
                        domesticKey.setContactEwalletKey(walletKey);
                    }
                    if (keyDataTemp.containsKey("reserverKey2")){
                        String reserverKey2 = keyDataTemp.get("reserverKey2").toString();
                        domesticKey.setReserveKey2(reserverKey2);
                    }
                    if (keyDataTemp.containsKey("rechargeKey")){
                        String rechargeKey = keyDataTemp.get("rechargeKey").toString();
                        domesticKey.setRechargeKey(rechargeKey);
                    }
                    if (keyDataTemp.containsKey("tacKey")){
                        String tacKey = keyDataTemp.get("tacKey").toString();
                        domesticKey.setTacKey(tacKey);
                    }
                    if (keyDataTemp.containsKey("cashKey")){
                        String cashKey = keyDataTemp.get("cashKey").toString();
                        domesticKey.setContactEcashKey(cashKey);
                    }
                    if (keyDataTemp.containsKey("appDelLockKey")){
                        String appDelLockKey = keyDataTemp.get("appDelLockKey").toString();
                        domesticKey.setUserUnlockAppMaintainKey(appDelLockKey);
                    }

                }
            }
            count++;
        }
        //DGI7
        if (count<dgiLen){
            JSONObject temp5 = (JSONObject) dgiArray.get(6);
            if (!ObjectUtils.isEmpty(temp5)){
                if (temp5.containsKey("rechargeKey2")){
                    String rechargeKey2 = temp5.get("rechargeKey2").toString();
                    domesticKey.setRechargeKeyBak(rechargeKey2);
                }
                String international = JSONObject.toJSONString(internationalKey);
                String domestic = JSONObject.toJSONString(domesticKey);
                fileContentDO.setInternationalKey(international);
                fileContentDO.setDomesticKey(domestic);
            }
            count++;
        }
        System.out.println(fileContentDO);
    }
}
