package cn.com.heyue.utils;

import cn.com.heyue.model.UserInfoDO;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 和包单点登录
 * @author vipsw
 *
 */
public class SSOUtils
{
    protected static Logger logger = LoggerFactory.getLogger("SSOUtils"); // 日志类
    
    /**
     * 接入者秘钥
     */
    private static final String SECRET_KEY = Constant.SSO_DEV_SECRET_KEY;
    
    /**
     * 开发者ID
     */
    private static final String DEV_ID = Constant.SSO_DEV_ID;
    
    /**
     * 单点登录请求地址
     */
    private static final String URL = Constant.SSO_URL;
    
    /**
     * 发送单点登录请求
     *
     * @param reqData 请求报文
     * @return
     */
    public static String sendRequest(String reqData)
    {
        logger.info("发送单点登录请求,请求地址：{}",URL);
        StringWriter writer = new StringWriter();
        OutputStreamWriter osw = null;
        try
        {
            URL reqURL = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection)reqURL.openConnection();
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("User-Agent", "stargate");
            conn.setRequestProperty("Content-Type", "application/json");
            osw = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            osw.write(reqData);
            osw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            char[] chars = new char[256];
            int count = 0;
            while ((count = br.read(chars)) > 0)
            {
                writer.write(chars, 0, count);
            }
        }
        catch (Exception e)
        {
            logger.error("请求单点登录接口出现错误", e);
            return "";
        }
        finally
        {
            try
            {
                if(osw != null){
                    osw.close();
                }
            }
            catch (IOException e)
            {
                logger.error("关闭IO出现错误", e);
            }
        }
        String result = writer.toString();
        logger.info("单点登录调用结果：{}", result);
        return result;
    }
    
    /**
     * 组装 XML 报文*
     *
     * @param credtential 待验签数据
     * @param signData 验签密文
     * @return
     */
    public static String buildConfirmXmlMessage(String credtential, String signData)
    {
        logger.info("拼装单点登录请求XML报文");
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("ROOT");
        Element head = root.addElement("HEAD");
        Element body = root.addElement("BODY");
        // HEAD 部分
        head.addElement("TXNCD").setText("2208000");
        head.addElement("MBLNO").setText("");
        head.addElement("SESSIONID").setText("");
        head.addElement("PLAT").setText("99");
        head.addElement("UA").setText("default");
        head.addElement("VERSION").setText("default");
        head.addElement("PLUGINVER").setText("");
        head.addElement("NETTYPE").setText("");
        head.addElement("MCID").setText("default");
        head.addElement("MCA").setText("default");
        head.addElement("IMEI").setText("default");
        head.addElement("IMSI").setText("default");
        head.addElement("SOURCE").setText("default");
        head.addElement("DEVID").setText(DEV_ID);
        Date currentTime = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HHmmss");
        String serlno = df.format(currentTime);
        head.addElement("SERLNO").setText(serlno);
        // BODY 部分
        body.addElement("CREDTENTIAL").setText(credtential);
        body.addElement("SIGN_DATA").setText(signData);
        body.addElement("SIGN_TYPE").setText("MD5");
        // 获取没有头声明的 xml
        String reqDate = root.asXML();
        // 去掉 reqDate 中的 root 标签来进行加密签名
        reqDate = reqDate.substring(0, reqDate.length() - 7);
        reqDate = reqDate.substring(6, reqDate.length());
        String signature = signature(reqDate);
        // 把签名出来的结果组装到 xml 中
        root.addElement("SIGNATURE").setText(signature);
        String xml = null;
        XMLWriter writer = null;
        ByteArrayOutputStream baos = null;
        try
        {
            try
            {
                OutputFormat format = OutputFormat.createCompactFormat();
                format.setIndent(false);
                format.setNewlines(false);
                format.setLineSeparator("");
                baos = new ByteArrayOutputStream();
                writer = new XMLWriter(baos, format);
                writer.write(document);
                xml = baos.toString("utf-8");
                logger.info("单点登录请求XML报文拼装完成 {}", xml);
                return xml;
            }
            finally
            {
                writer.close();
                baos.close();
            }
        }
        catch (Exception e)
        {
            logger.error("生成XML报文出现错误", e);
            return "";
        }
    }

    //<?xml version="1.0" encoding="UTF-8"?>
    //<ROOT><HEAD><RSPCD>000000</RSPCD><LOGID>CCLIMCA4MCAMCA_REGION1320052508281907046</LOGID><SESSIONID/></HEAD>
    // <BODY><MBL_NO>13816928750</MBL_NO><USR_NO>400032724740</USR_NO><USR_ID>50357DC01CCC0024F09D69F303FFAC04</USR_ID></BODY>
    // </ROOT>
    public static UserInfoDO getHebaoUser(String result)
    {
        UserInfoDO user = null;
        if (StringUtils.isBlank(result))
        {
            logger.error("单点登录请求返回报文为空，直接返回");
            return user;
        }
        String mblNo = "";
        String userNo = "";
        try
        {
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            Element el = root.element("BODY");
            if (null == el || null == el.element("MBL_NO"))
            {
                logger.error("【返回的xml不包含手机号码】{}",result);
                return user;
            }
            mblNo = el.element("MBL_NO").getText();
            //userNo = el.element("USR_NO").getText();
            user = new UserInfoDO();
            user.setMobile(mblNo);
            //user.setUserNo(userNo);
            return user;
        }
        catch (Exception e)
        {
            logger.error("解析XML出现错误", e);
            return null;
        }
    }
    
    /**
     * 生成签名
     *
     * @param reqDate 待加密数据
     * @return
     */
    private static String signature(String reqDate)
    {
        String hashAlgorithmName = "HmacSHA1";
        SecretKeySpec spec = new SecretKeySpec(SECRET_KEY.getBytes(), hashAlgorithmName);
        Mac mac;
        String Signature = "";
        try
        {
            mac = Mac.getInstance(hashAlgorithmName);
            mac.init(spec);
            byte[] bytes = mac.doFinal(reqDate.getBytes());
            Signature = new String((new BASE64Encoder()).encodeBuffer(bytes));
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e)
        {
            logger.error("生成单点登录签名出现错误{}",e);
        }
        return Signature;
    }

    public static void ssoApply() throws Exception {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json;charset=utf-8");
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("joinChannelMark", "TEST0001"); //调用方ID
        paramMap.put("targetChannelMark", "NWAP001");//目标ID
        paramMap.put("mobileNo", "18773169583");	//手机号

        /*String key = "T1dIuKljVk35ko1P";//签名秘钥
        String content = JsonUtil.getJsonFromObject(paramMap);
        String sign = Encodes.encodeHex(Digests.md5((content + key).getBytes()));
        header.put("x-lemon-sign", sign);
        String url = BaseUrlEnum.BASE_URL + "/sso/foreign/foreign-token/apply";
        String result = HttpAgent.getInstance().post(url, header, content);
        System.out.println("===================分割线=======================");
        System.out.println(result);*/
    }


    public static String ssoCheck(String token) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("targetChannelMark", Constant.SSO_TARGET_CHANNEL_MARK);//调用方ID
        paramMap.put("foreignToken",token);

        String key = Constant.SSO_MD5_KEY;//签名秘钥
        String content = JSON.toJSONString(paramMap);
        //String sign = Encodes.encodeHex(Digests.md5((content + key).getBytes()));
        String sign = Md5Encrypt.md5(content + key,"utf-8");
        String result = OKHttpClientUtils.postJsonHead(URL,"【和包单点登陆TOKEN校验】",content,sign);
        return result;
    }

    public static String ssoApply2(Map paramMap) throws Exception {
        String key = Constant.SSO_APPLY_MD5_KEY;//签名秘钥
        String content = JSON.toJSONString(paramMap);
        String sign = Md5Encrypt.md5(content + key,"utf-8");
        String result = OKHttpClientUtils.postJsonHead(Constant.SSO_APPLY_URL,"【和包单点登陆APPLY】",content,sign);
        return result;
    }
}
