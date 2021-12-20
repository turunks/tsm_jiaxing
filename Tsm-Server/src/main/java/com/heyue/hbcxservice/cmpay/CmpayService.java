package com.heyue.hbcxservice.cmpay;

import cn.com.heyue.utils.DateUtils;
import cn.com.heyue.utils.Md5Encrypt;
import cn.com.heyue.utils.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@Service
public class CmpayService {
    private static Logger logger = LoggerFactory.getLogger("CmpayService"); // 日志类

    /**
     * 支付地址
     */
    @Value("${HEBAO_PAY_URL}")
    private static String HEBAO_PAY_URL;

    /**
     * 支付回调地址
     */
    @Value("${CMPAY_NOTIFY_URL}")
    private static String CMPAY_NOTIFY_URL;

    /**
     * 支付回调页面地址
     */
    @Value("${CMPAY_CALLBACK_URL}")
    private static String CMPAY_CALLBACK_URL;

    /**
     * 支付商户号
     */
    @Value("${CMPAY_MERNO}")
    private static String CMPAY_MERNO;

    /**
     * 支付商户秘钥
     */
    @Value("${CMPAY_MER_KEY}")
    private static String CMPAY_MER_KEY;

    /**
     * 和包支付
     *
     * @param reqMap 订单业务参数
     * @return
     */
    public static Map<String, String> pay(Map<String, Object> reqMap) throws UnsupportedEncodingException {
        logger.info("【调用和包支付接口】");
        //logger.info("【商户编号及密钥{},{}】", mercnum, merckey);
        String mercnum = CMPAY_MERNO;
        logger.info("mercnum={}", mercnum);
        String merckey = CMPAY_MER_KEY;
        logger.info("merckey={}", merckey);
        String notifyUrl = CMPAY_NOTIFY_URL;
        logger.info("notifyUrl={}", notifyUrl);
        String callbackUrl = CMPAY_CALLBACK_URL;
        logger.info("callbackUrl={}", callbackUrl);

        Map<String, String> respMap = new HashMap<>();
        String characterSet = "00";
        String clientIp = reqMap.get("clientIp") == null ? "" : reqMap.get("clientIp").toString();
        String requestId = "SIMQ" + System.currentTimeMillis();
        String merchantCert = "";
        String signType = "MD5";
        String type = "WAPDirectPayConfirm";
        //String productName = "和包出行卡充值";
//        String productName = "HBCX";
        String productName = "和包出行";
        //String productName = "Recharge of Hebao travel card";
        String version = "2.0.0";

        String amount = reqMap.get("amount") == null ? "0" : reqMap.get("amount").toString();
        String bankAbbr = "";
        String currency = "00";
        String orderDate = reqMap.get("orderDate") == null ? "" : reqMap.get("orderDate").toString();
        String orderId = reqMap.get("orderId") == null ? "" : reqMap.get("orderId").toString();
        String merAcDate = orderDate;
        String period = "5";
        String periodUnit = "00";
        String merchantAbbr = "";
        String productDesc = "";
        String productId = "";
        String productNum = "";
        String reserved1 = "";
        String reserved2 = "";
        String userToken = reqMap.get("mobile") == null ? "" : reqMap.get("mobile").toString();
        String showUrl = "";
        String counPonsFlag = "40";//00 使用全部营销工具(默 认) 10 不支持使用电子券 20 不支持代金券 30-不支持积分 40-不支持所有营销工具

        logger.info("【请求流水{}】", requestId);
        Map<String, String> reqData = new HashMap();
        reqData.put("characterSet", characterSet);//00- GBK 01- GB2312 02- UTF-8
        reqData.put("callbackUrl", callbackUrl);
        reqData.put("notifyUrl", notifyUrl);
        reqData.put("ipAddress", clientIp);
        reqData.put("merchantId", mercnum);
        reqData.put("requestId", requestId);
        reqData.put("signType", signType);
        reqData.put("type", type);
        reqData.put("version", version);
        reqData.put("merchantCert", merchantCert);//如果 signType=RSA ，此是项必输
        // *************************生成签名*************************
        logger.info("开始生成签名串");
        StringBuffer signBuf = new StringBuffer();
        signBuf.append(characterSet);
        signBuf.append(callbackUrl);
        signBuf.append(notifyUrl);
        signBuf.append(clientIp);
        signBuf.append(mercnum);
        signBuf.append(requestId);
        signBuf.append(signType);
        signBuf.append(type);
        signBuf.append(version);
        signBuf.append(amount);
        // 银行代码
        signBuf.append(bankAbbr);
        signBuf.append(currency);
        signBuf.append(orderDate);
        signBuf.append(orderId);
        // 商户会计日期
        signBuf.append(merAcDate);
        signBuf.append(period);
        signBuf.append(periodUnit);
        // 商品展示名称
        signBuf.append(merchantAbbr);
        signBuf.append(productDesc);
        signBuf.append(productId);//产品编号
        // 产品名称
        signBuf.append(productName);
        // 产品数量
        signBuf.append(productNum);
        // 两个保留字段
        signBuf.append(reserved1);
        signBuf.append(reserved2);
        signBuf.append(userToken);
        // 商户展示地址
        signBuf.append(showUrl);
        signBuf.append(counPonsFlag);
        logger.info("签名原始串：{}", signBuf.toString());


        reqData.put("amount", amount);
        reqData.put("bankAbbr", bankAbbr);
        reqData.put("currency", currency);
        reqData.put("orderDate", orderDate);
        reqData.put("orderId", orderId);
        reqData.put("merAcDate", merAcDate);
        reqData.put("period", period);
        reqData.put("periodUnit", periodUnit);
        reqData.put("merchantAbbr", merchantAbbr);
        reqData.put("productDesc", productDesc);
        reqData.put("productId", productId);
        reqData.put("productName", productName);
        reqData.put("productNum", productNum);
        reqData.put("reserved1", reserved1);
        reqData.put("reserved2", reserved2);
        reqData.put("userToken", userToken);
        reqData.put("showUrl", showUrl);
        reqData.put("couponsFlag", counPonsFlag);

        String responseText = "";
        try {
            String sign = Md5Encrypt.md5_hisun(signBuf.toString(), merckey);
            /*HiCASignUtil2 rsa = new HiCASignUtil2(merckey,"");
            String sign =  rsa.sign(signBuf.toString());*/
            reqData.put("hmac", sign);
            responseText = sendPost(HEBAO_PAY_URL, reqData, "GBK");
        } catch (Exception e) {
            logger.error("{}", e);
            respMap.put("returnCode", "999999");
            respMap.put("message", "支付接口请求异常");
            return respMap;
        }
        logger.info("【和包支付请求结果:{}】", responseText);
        if (StringUtils.isBlank(responseText)) {
            respMap.put("returnCode", "999999");
            respMap.put("message", "支付接口请求超时");
            return respMap;
        }
        String returnCode = getValue(responseText, "returnCode");
        String message = URLDecoder.decode(getValue(responseText, "message"), "UTF-8");
        logger.info("returnCode:{},message:{}", returnCode, message);
        if ("000000".equals(returnCode)) {
            String merchantId_s = getValue(responseText, "merchantId");
            String requestId_s = getValue(responseText, "requestId");
            String signType_s = getValue(responseText, "signType");
            String type_s = getValue(responseText, "type");
            String version_s = getValue(responseText, "version");
            String payUrl = getValue(responseText, "payUrl");
            String serverCert_s = getValue(responseText, "serverCert");
            String hmac_s = getValue(responseText, "hmac");
            if (StringUtils.isBlank(hmac_s)) {
                hmac_s = "";
            }
            StringBuffer respsignBuf = new StringBuffer();
            respsignBuf.append(merchantId_s);
            respsignBuf.append(requestId_s);
            respsignBuf.append(signType_s);
            respsignBuf.append(type_s);
            respsignBuf.append(version_s);
            respsignBuf.append(returnCode);
            respsignBuf.append(message);
            respsignBuf.append(payUrl);

            logger.info("支付成功：原始签名串：" + respsignBuf.toString());
            String sign_succ = Md5Encrypt.md5_hisun(respsignBuf.toString(), merckey);
            logger.info("生成的签名串：" + sign_succ);
            if (!StringUtils.equals(sign_succ.toUpperCase(), hmac_s.toUpperCase())) {
                logger.error("【支付响应消息签名错误】");
                respMap.put("returnCode", "111000");
                respMap.put("message", "支付响应消息签名错误");
                return respMap;
            } else {
//                getPayUrl(payUrl, respMap);
                logger.info("【支付成功！！！】");
                respMap.put("payparm", payUrl);
                respMap.put("returnCode", "000000");
                respMap.put("message", "SUCCESS");
                /*respMap.put("payUrl", newUrl);*/
                return respMap;
            }

            //RSA方式解密
            /*HiCASignUtil2 rsa1 = new HiCASignUtil2(serverCert_s);
            Boolean flag =  rsa1.verify(respsignBuf.toString(), hmac_s, serverCert_s);
            if(!flag){
                logger.error("【支付响应消息签名错误】");
                respMap.put("returnCode", "111000");
                respMap.put("message", "支付响应消息签名错误");
                return respMap;
            }else{
                logger.info("【支付成功！！！】payUrl:{}", payUrl);
                respMap.put("returnCode", "000000");
                respMap.put("message", "SUCCESS");
                respMap.put("payUrl", payUrl);
                return respMap;
            }*/
        } else {
            respMap.put("returnCode", returnCode);
            respMap.put("message", message);
            return respMap;
        }
    }

    public static void getPayUrl(String payUrl, Map<String, String> respMap) {
        // 得到响应的串，分三部分，第一部分是URL，第二部分是method，第三部分是SESSIONID
        if (StringUtils.isNotBlank(payUrl)) {
            payUrl = payUrl.replaceAll(">", "&gt;");
            payUrl = payUrl.replaceAll("<", "&lt;");
        }
        // 得到响应的串，分三部分，第一部分是URL，第二部分是method，第三部分是SESSIONID
        String[] resStr = payUrl.split("&lt;hi:\\$\\$&gt;");

        String url = "";
//        String activeProfile = SpringContextUtil.getActiveProfile();
//        if ("pro".equals(activeProfile)) {
//            url = resStr[0].split("&lt;hi:=&gt;")[1];
//        } else {
            url = "https://211.138.236.210/wap/index.xhtml";
//        }

        String method = resStr[1].split("&lt;hi:=&gt;")[1];
        // TODO 只用用GET
        method = "get";
        String sessionId = resStr[2].split("&lt;hi:=&gt;")[1];

        StringBuffer pageBuf = new StringBuffer();
        pageBuf.append("<form id=\"payForm\" name=\"payForm\" action=\"" + url + "\" method=\"" + method + "\">");
        pageBuf.append("<input type=\"hidden\" value=\"" + sessionId + "\" name=\"SESSIONID\">");
        pageBuf.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none;\">");
        pageBuf.append("</form>");
        pageBuf.append("<script>document.forms['payForm'].submit();</script>");

        respMap.put("payUrl", url + "?method=" + method + "&sessionId=" + sessionId);
        respMap.put("formBuffer", pageBuf.toString());
        //return url+"?method="+method+"&sessionId="+sessionId;
    }

    public static void main(String[] args) {
        String payUrl = "merchantId=888073117000022&requestId=SIMQ1639478760560&signType=MD5&type=WAPDirectPayConfirm&version=2.0.0&returnCode=000000&message=SUCCESS&payUrl=url<hi:=>https://uatipos.10086.cn/wap/index.xhtml<hi:$$>method<hi:=>POST<hi:$$>sessionId<hi:=>20520617205206171918317132<hi:$$>MERC_PAY_TPY<hi:=>&hmac=57bb58d8ae9987b82c36df224b3858ed";
        payUrl = payUrl.replaceAll(">", "&gt;");
        payUrl = payUrl.replaceAll("<", "&lt;");
        System.out.println(payUrl);
    }

    /**
     * 订单查询接口
     *
     * @param reqMap
     * @return
     */
    public static Map<String, String> query(Map<String, Object> reqMap) {
        logger.info("【调用和包订单查询接口】");
        String mercnum = CMPAY_MERNO;
        String merckey = CMPAY_MER_KEY;
        Map<String, String> respMap = new HashMap<>();
        logger.info("【商户编号及密钥{},{}】", mercnum, merckey);
        String requestId = "SIMQ" + System.currentTimeMillis();
        String signType = "MD5";
        String type = "OrderQuery";
        String version = "2.0.4";
        String orderId = reqMap.get("orderId") == null ? "" : reqMap.get("orderId").toString();
        String merchantCert = "";

        Map<String, String> reqData = new HashMap();
        reqData.put("merchantId", mercnum);
        reqData.put("requestId", requestId);
        reqData.put("signType", signType);
        reqData.put("type", type);
        reqData.put("version", version);
        reqData.put("orderId", orderId);
        reqData.put("merchantCert", merchantCert);
        logger.info("开始生成签名串");
        StringBuffer signBuf = new StringBuffer();
        signBuf.append(mercnum);
        signBuf.append(requestId);
        signBuf.append(signType);
        signBuf.append(type);
        signBuf.append(version);
        signBuf.append(orderId);
        signBuf.append(merchantCert);
        logger.info("签名原始串：{}", signBuf.toString());

        String responseText = "";
        try {
            String sign = Md5Encrypt.md5_hisun(signBuf.toString(), merckey);
            /*HiCASignUtil2 rsa = new HiCASignUtil2(merckey,"");
            String sign =  rsa.sign(signBuf.toString());*/
            reqData.put("hmac", sign);
            responseText = sendPost(HEBAO_PAY_URL, reqData, "GBK");
        } catch (Exception e) {
            logger.error("{}", e);
            respMap.put("returnCode", "999999");
            respMap.put("message", "查询订单接口请求异常");
            return respMap;
        }
        logger.info("【和包订单状态查询结果:{}】", responseText);
        if (StringUtils.isBlank(responseText)) {
            respMap.put("returnCode", "999999");
            respMap.put("message", "支付接口请求超时");
            return respMap;
        }
        String returnCode = getValue(responseText, "returnCode");
        String message = getValue(responseText, "message");
        logger.info("returnCode:{},message:{}", returnCode, message);
        if ("000000".equals(returnCode)) {
            String status = getValue(responseText, "status");
            respMap.put("returnCode", "000000");
            respMap.put("message", message);
            respMap.put("status", status);
            return respMap;
        } else {
            respMap.put("returnCode", returnCode);
            respMap.put("message", message);
            return respMap;
        }
    }


    /**
     * 和包退款
     *
     * @param reqMap
     * @return
     */
    public static Map<String, String> refund(Map<String, Object> reqMap) {
        logger.info("【线上：调用和包退款接口】");
        String mercnum = CMPAY_MERNO;
        String merckey = CMPAY_MER_KEY;
        Map<String, String> respMap = new HashMap<>();
        String requestId = "SIMQ" + System.currentTimeMillis();
        String merchantCert = "";
        String signType = "MD5";
        String type = "OrderRefund";
        String version = "2.0.0";

        String amount = reqMap.get("amount") == null ? "0" : reqMap.get("amount").toString();
        String orderId = reqMap.get("orderId") == null ? "" : reqMap.get("orderId").toString();
        logger.info("【请求流水{}】", requestId);
        Map<String, String> reqData = new HashMap();
        reqData.put("merchantId", mercnum);
        reqData.put("requestId", requestId);
        reqData.put("signType", signType);
        reqData.put("type", type);
        reqData.put("version", version);
        reqData.put("merchantCert", merchantCert);//如果 signType=RSA ，此是项必输
        // *************************生成签名*************************
        logger.info("开始生成签名串");
        StringBuffer signBuf = new StringBuffer();
        signBuf.append(mercnum);
        signBuf.append(requestId);
        signBuf.append(signType);
        signBuf.append(type);
        signBuf.append(version);
        signBuf.append(orderId);
        signBuf.append(amount);
        logger.info("签名原始串：{}", signBuf.toString());

        reqData.put("amount", amount);
        reqData.put("orderId", orderId);

        String responseText = "";
        try {
            String sign = Md5Encrypt.md5_hisun(signBuf.toString(), merckey);
            /*HiCASignUtil2 rsa = new HiCASignUtil2(merckey,"");
            String sign =  rsa.sign(signBuf.toString());*/

            reqData.put("hmac", sign);
            responseText = sendPost(HEBAO_PAY_URL, reqData, "GBK");
        } catch (Exception e) {
            logger.error("{}", e);
            respMap.put("returnCode", "999999");
            respMap.put("message", "退款接口请求异常");
            return respMap;
        }
        logger.info("【和包退款请求结果:{}】", responseText);
        if (StringUtils.isBlank(responseText)) {
            respMap.put("returnCode", "999999");
            respMap.put("message", "退款接口请求超时");
            return respMap;
        }
        String returnCode = getValue(responseText, "returnCode");
        String message_s = getValue(responseText, "message");
        String message = message_s;
        if (StringUtils.isNotBlank(message)) {
            try {
                message = URLDecoder.decode(message_s, "UTF-8");
            } catch (Exception e) {
                message = message_s;
            }
        }
        logger.info("returnCode:{},message:{}", returnCode, message);
        if ("000000".equals(returnCode)) {
            String status_s = getValue(responseText, "status");
            logger.info("退款状态：" + status_s);

            respMap.put("returnCode", "000000");
            respMap.put("message", "");
            respMap.put("status", status_s);
            return respMap;
        } else {
            respMap.put("returnCode", returnCode);
            respMap.put("message", message);
            return respMap;
        }
    }

    /**
     * 获取请求值
     *
     * @param content 响应串
     * @param name    名称
     * @return
     */
    private static String getValue(String content, String name) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        String[] tmp = content.split("&");
        String value = "";
        for (String s : tmp) {
            if (s.startsWith(name)) {
                value = s.substring(name.length() + 1);
            }
        }
        return value;
    }

    /**
     * 发送请求
     *
     * @param url
     * @param params
     * @param encoding
     * @return
     * @throws Exception
     */
    private static String sendPost(String url, Map<String, String> params, String encoding) throws Exception {
        URL u = null;
        HttpURLConnection con = null;
        BufferedReader reader = null;
        // 构建请求参数
        String reqStr = "";
        if (params != null) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(StringUtils.isNotBlank(e.getValue()) ? e.getValue() : "");
                sb.append("&");
            }
            reqStr = sb.substring(0, sb.length() - 1);
        }
        logger.info("请求地址：" + url + "，请求数据：" + reqStr);
        // 尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            //// POST 只能为大写，严格限制，post会不识别
            con.setRequestMethod("POST");
            con.setConnectTimeout(20000);
            con.setReadTimeout(20000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=GBK");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), encoding);
            osw.write(reqStr);
            osw.flush();
            osw.close();

            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), encoding));
            String lines;
            StringBuffer rtnBuf = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                rtnBuf.append(lines);
            }

            return rtnBuf.toString();
        } catch (Exception e) {
            logger.error("{}", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }

    public static String getRequestInfo() throws Exception{
        String merckey = "9mX8MWS1ugUcboK8Og287eOpzl8VsKGVQrHEWZeDYlDC0nSEmlSitE3CL5kOMYY5";

        String requestId = DateUtils.format(new Date(),"yyyyMMddHHmmss");

        Map<String, String> params = new TreeMap<>();
        params.put("characterSet", "00");
        // 转成我方的通知地址及回调地址
        params.put("callbackUrl", "http://81.69.220.133:9012/sim_h5/index.html#/pay");
        params.put("notifyUrl", "http://81.69.220.133:9013/payServer/cmpayNotify");
        params.put("ipAddress", "27.19.200.46");
        params.put("merchantId", "888201200417444");
        params.put("requestId", requestId);
        params.put("signType", "MD5");
        params.put("type", "WAPDirectPayConfirm");
        params.put("version", "2.0.0");
        params.put("merchantCert", "");
        // *************************生成签名*************************
        StringBuffer signBuf = new StringBuffer();
        signBuf.append("00");
        signBuf.append("http://81.69.220.133:9012/sim_h5/index.html#/pay");
        signBuf.append("http://81.69.220.133:9013/payServer/cmpayNotify");
        signBuf.append("27.19.200.46");
        signBuf.append("888201200417444");
        signBuf.append(requestId);
        signBuf.append("MD5");
        signBuf.append("WAPDirectPayConfirm");
        signBuf.append("2.0.0");
        signBuf.append("10");
        // 银行代码
        signBuf.append("");
        signBuf.append("00");
        signBuf.append("20210611");
        signBuf.append(requestId);
        // 商户会计日期
        signBuf.append("20210611");
        signBuf.append("20");
        signBuf.append("00");
        // 商品展示名称
        signBuf.append("");
        signBuf.append("");
        signBuf.append("");
        // 产品名称
        signBuf.append("SIM");
        // 产品数量
        signBuf.append("");
        // 两个保留字段
        signBuf.append("");
        signBuf.append("");
        signBuf.append("13476257639");
        // 商户展示地址
        signBuf.append("");
        signBuf.append("00");
        System.out.println("签名原文:" + signBuf.toString());
        String sign = Md5Encrypt.md5_hisun(signBuf.toString(), merckey);
        params.put("hmac", sign);
        params.put("amount", "10");
        params.put("bankAbbr", "");
        params.put("currency", "00");
        params.put("orderDate", "20210611");
        params.put("orderId", requestId);
        params.put("merAcDate", "20210611");
        params.put("period", "20");
        params.put("periodUnit", "00");
        params.put("merchantAbbr", "");
        params.put("productDesc", "");
        params.put("productId", "");
        params.put("productName", "SIM");
        params.put("productNum", "");
        params.put("reserved1", "");
        params.put("reserved2", "");
        params.put("userToken", "13476257639");
        params.put("showUrl", "");
        params.put("couponsFlag", "00");

        StringBuffer buf = new StringBuffer();
        Iterator it = params.keySet().iterator();
        while (it.hasNext()){
            String key = it.next().toString();
            buf.append(key).append("=").append(params.get(key)).append("&");
        }
        //System.out.println(buf.toString().substring(0,buf.length()-1));
        return buf.toString().substring(0,buf.length()-1);
    }

//    public static void main(String[] args) throws Exception {
//        for (int i = 0; i < 20; i++) {
//            String reqStr = getRequestInfo();
//
//            URL u = new URL("https://ipos.10086.cn/ips/cmpayService");
//            HttpURLConnection con = (HttpURLConnection) u.openConnection();
//            //// POST 只能为大写，严格限制，post会不识别
//            con.setRequestMethod("POST");
//            con.setConnectTimeout(20000);
//            con.setReadTimeout(20000);
//            con.setDoOutput(true);
//            con.setDoInput(true);
//            con.setUseCaches(false);
//            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "GBK");
//            osw.write(reqStr);
//            osw.flush();
//            osw.close();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "GBK"));
//            String lines;
//            StringBuffer rtnBuf = new StringBuffer();
//            while ((lines = reader.readLine()) != null) {
//                rtnBuf.append("请求：" + reqStr + "响应：" + lines);
//            }
//            System.out.println(rtnBuf.toString());
//            Thread.sleep(1000);
//        }
//    }
}
