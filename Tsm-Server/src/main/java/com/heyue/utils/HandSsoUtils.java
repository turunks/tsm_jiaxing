package cn.com.heyue.utils;

import cn.com.heyue.message.response.UserHandRes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.uam.ngca.util.XMLEncode;
import com.uam.ngca.util.XmlJson;
import com.uam.ngca.util.caService;
import com.uam.ngca.util.sign.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：dengjie
 * @date ：2021/1/20
 * @description ：掌厅登陆
 */
public class HandSsoUtils {

    private final static Logger log = LoggerFactory.getLogger(HandSsoUtils.class);

    /**
     * 渠道id
     */
    private static final String CHANNEL_ID = Constant.HAND_SSO_CHANNEL_ID;

    /**
     * 票据查询webservice地址
     */
    private static final String ARTIFACT_URL = Constant.HAND_SSO_USER_QUERY_ARTIFACT_URL;
    /**
     * 票据查询方法名称
     */
    private static final String ARTIFACT_METHOD = "getAssertInfoByArtifact";
    /**
     * 接口超时时长
     */
    private static final Integer TIMEOUT = Constant.HAND_SSO_TIMEOUT;
    /**
     * 成功代码
     */
    private static final String SUCCESS_CODE = "0000";

    /**
     * 根据票据查询用户信息
     *
     * @param artifact String
     * @return UserHandRes
     */
    public static UserHandRes getAssertInfoByArtifact(String artifact, String sign) {
        Map<String, String> xmlAssertionMap = new HashMap<>(2);
        xmlAssertionMap.put("Artifact", artifact);
        xmlAssertionMap.put("channelID", CHANNEL_ID);

        try {
            // 拼装请求报文
            String requestAssertionXml = XMLEncode.setAssertionXML(xmlAssertionMap);

            // 调用断言查询接口
            // 测试环境 testUrl 生产环境 trueUrl
            String rspAssertionXml = caService.getService(ARTIFACT_URL, ARTIFACT_METHOD, requestAssertionXml, TIMEOUT);

            // 返回报文解密
            rspAssertionXml = Security.getDecryptString(rspAssertionXml);
            log.info("{}报文查询结果->{}", sign, rspAssertionXml);

            // 解析登录返回报文 json数组
            JSONObject jsonAssertion = XmlJson.xml2Json(rspAssertionXml);

            // 处理登录返回结果 RspCode
            String rspAssertionCode = getRspCode(jsonAssertion);
            log.info("{}登录返回结果->{}", sign, rspAssertionCode);

            if (SUCCESS_CODE.equals(rspAssertionCode)) {
                // assertionInfo 中包含用户相关信息：手机号码、UID、省份、品牌、identcodecode 等
                JSONObject assertionInfo = jsonAssertion.getJSONObject("SessionBody");
                JSONObject qryRspInfo = assertionInfo.getJSONObject("AssertionQryRsp");
                JSONObject userInfo = qryRspInfo.getJSONObject("UserInfo");
                UserHandRes user = JSON.toJavaObject(userInfo, UserHandRes.class);
                log.info("{}查询到的用户信息->{}", sign, user.toString());
                return user;
            } else {
                // 失败返回结果描述
                String rspAssertionDesc = jsonAssertion.getJSONObject("SessionHeader").getJSONObject("Response").getString("RspDesc");
                log.error("{}查询失败rspAssertionCode->{},{}", sign, rspAssertionCode, rspAssertionDesc);
                return null;
            }
        } catch (Exception e) {
            log.error("{}查询用户信息异常,artifact_id->{},错误信息->{}", sign, artifact, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取认证结果
     *
     * @param json JSONObject
     * @return String
     */
    public static String getRspCode(JSONObject json) {
        JSONObject smsRsp = json.getJSONObject("SessionHeader").getJSONObject("Response");
        return smsRsp.getString("RspCode");
    }

}

 