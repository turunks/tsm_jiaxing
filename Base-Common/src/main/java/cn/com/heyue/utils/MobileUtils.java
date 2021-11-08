package cn.com.heyue.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmft.api.utils.APIUtils;
import com.cmft.api.utils.CoderException;
import com.cmft.api.utils.dto.CmftApiEncryptDTO;
import com.cmft.api.utils.helper.Signature;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * 手机号相关类，如：查询归属地等
 *
 * @author fuzuyuan
 */
public class MobileUtils {
    private static final Logger logger = LoggerFactory.getLogger(MobileUtils.class); // 日志类

    /**
     * 查询用户在和包的归属地信息
     *
     * @param sign 签名头
     * @param url  查询地址
     * @return
     */
    public static String queryHisunUserCity(String sign, String url) {
        String result = OKHttpClientUtils.getJsonSSL(url, sign, "");
        if (result != null) {
            JSONObject json = JSON.parseObject(result);
            String cityName = json.getString("cityName");
            return StringUtils.isNotBlank(cityName) ? cityName : null;
        }
        return null;
    }

    /**
     * 查询用户在和包的归属地省信息
     *
     * @param sign 签名头
     * @param url  查询地址
     * @return
     */
    public static String queryHisunUserProvinceName(String sign, String url) {
        String result = OKHttpClientUtils.getJsonSSL(url, sign, "");
        if (result != null) {
            JSONObject json = JSON.parseObject(result);
            String provinceName = json.getString("provinceName");
            return StringUtils.isNotBlank(provinceName) ? provinceName : null;
        }
        return null;
    }

    /**
     * 接入方请求参数加密
     *
     * @param sign               日志头
     * @param url                接口地址
     * @param mobile             手机号
     * @param externalPublicKey  接入方公钥
     * @param externalPrivateKey 接入方私钥
     * @param cmftPublicKey      金科平台公钥
     * @param cmftPrivateKey     金科平台私钥
     * @return
     * @throws CoderException
     */
    public static String queryHisunUserProvinceName(String sign, String url, String mobile, String externalPublicKey, String externalPrivateKey, String cmftPublicKey, String cmftPrivateKey, String appId) throws CoderException {
        //加密数据
        Map<String, String> map = new HashMap<>();
        map.put("mobileNo", mobile);
        CmftApiEncryptDTO encryptDTO = APIUtils.encrypt(map, "1.0.0", cmftPublicKey, externalPublicKey, externalPrivateKey);
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(encryptDTO));
        jsonObject.put("appId", appId);
        String result = OKHttpClientUtils.postJsonSSL(url, sign, jsonObject.toJSONString());
        if (result != null) {
            JSONObject resultJson = JSON.parseObject(result);
            BigInteger r = resultJson.getJSONObject("signature").getBigInteger("r");
            BigInteger s = resultJson.getJSONObject("signature").getBigInteger("s");
            Signature signature = new Signature(r,s);
            Map<String, String> resultDecrypt = APIUtils.decrypt(resultJson.getString("data"), resultJson.getString("tm"), "1.0.0", cmftPublicKey, externalPrivateKey, signature);
            logger.info("{}查询到结果：{}", sign, JSON.toJSONString(resultDecrypt));
            if (resultDecrypt.get("msgCd") != null && StringUtils.equals(resultDecrypt.get("msgCd"), "CMM00000")) {
                return resultDecrypt.get("provinceName");
            } else {
                logger.warn("{}查询归属地失败：{}", sign, resultDecrypt.get("msgInfo"));
                return null;
            }
        }
        return null;
    }

    public static String queryHisunUserCityName(String sign, String url, String mobile, String externalPublicKey, String externalPrivateKey, String cmftPublicKey, String cmftPrivateKey, String appId) throws CoderException {
        //加密数据
        Map<String, String> map = new HashMap<>();
        map.put("mobileNo", mobile);
        CmftApiEncryptDTO encryptDTO = APIUtils.encrypt(map, "1.0.0", cmftPublicKey, externalPublicKey, externalPrivateKey);
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(encryptDTO));
        jsonObject.put("appId", appId);
        String result = OKHttpClientUtils.postJsonSSL(url, sign, jsonObject.toJSONString());
        if (result != null) {
            JSONObject resultJson = JSON.parseObject(result);
            BigInteger r = resultJson.getJSONObject("signature").getBigInteger("r");
            BigInteger s = resultJson.getJSONObject("signature").getBigInteger("s");
            Signature signature = new Signature(r,s);
            Map<String, String> resultDecrypt = APIUtils.decrypt(resultJson.getString("data"), resultJson.getString("tm"), "1.0.0", cmftPublicKey, externalPrivateKey, signature);
            logger.info("{}查询到结果：{}", sign, JSON.toJSONString(resultDecrypt));
            if (resultDecrypt.get("msgCd") != null && StringUtils.equals(resultDecrypt.get("msgCd"), "CMM00000")) {
                return resultDecrypt.get("cityName");
            } else {
                logger.warn("{}查询归属地失败：{}", sign, resultDecrypt.get("msgInfo"));
                return null;
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
//        String url = "http://211.138.236.210:9201/api/v1/query/numberSegment?mobileNo=";
//        System.out.println(MobileUtils.queryHisunUserCity("【测试】", url + "15750315747"));


//        String url = "https://36.158.218.168:20914/api/v1/query/numberSegment?mobileNo=";
//        System.out.println(MobileUtils.queryHisunUserCity("【测试】", url + "15750315747"));

//        String result = "{\"appId\":\"1\",\"data\":\"JmUcHWpvYyfE3/47feKNa0xslOaz9Q83Q7lKCee3Xl1/xvcpMXlS9hckiVyE2Zsi+z9O2U16cl9rlIHOyJMip7bFg3pX1b+Ue0CfEj1PJAB0NO1XDRSkY0IxwnmVDxtO0ZYHlHaRi0ndhM8eIDhcyrNvFLagftDesPyrltt3Z0pYUJiBHZo2mQpXl3LQdPbuB9lQ+o5bUWu3J3tIoZs6GA==\",\"signature\":{\"r\":50085575932078850064462995441690492021281149407523767669374062009679401506572,\"s\":82652397424377853988570587140565867454501112736977292949589588809480865049279},\"tm\":\"040A71053D1B0D07CA4B684E0C565B5D8F88DB1DDA4AC50EF6E6E464D2FF6BBA55E5067883F5AFDA654A9D7F484EA987D6647BB4B79C0AA9F653EBBE23D72D209F0EE9C7DA9E2ED5020C97130033BDB62F7B016C85045F2AEBCC539B79F1182CA4525952332505B4173BEDD6A641D45292466105171B062B81F90F737239AB2073\",\"version\":\"1.0.0\"}";
//        JSONObject resultJson = JSON.parseObject(result);
//        BigInteger r = resultJson.getJSONObject("signature").getBigInteger("r");
//        BigInteger s = resultJson.getJSONObject("signature").getBigInteger("s");
//
//        Signature signature = new Signature(r,s);

        String url = "https://36.158.218.168:20914/api/v1/number/queryNumberSegment";
        String mobile = "13476257639";
        String extPubKey = "044ff9dcc6e4ca94bae46be16c77bc6fa683213713ddeaedd836924dea36e4ab02bad1b5adc00fb81b18bf0c5445e0b84015a444b0618b30d94e8fdbb84a1106b9";
        String extPriKey = "00bffaba1e3e638ca3a68667ee6b43a703d22b81df44c47b82e59289196a974ba5";
        String pubKey = "0479beb0aff4cc78b7fe33c1ff6b6d41d17267397e6873df6ed174af2defe5416d505e84bdd67d473ece0dd975b44b177a7ce2ddf258d9e84dc3759d36acee478b";
        String priKey = "";
        MobileUtils.queryHisunUserProvinceName("测试", url, mobile, extPubKey, extPriKey, pubKey, priKey, "1");
    }
}
