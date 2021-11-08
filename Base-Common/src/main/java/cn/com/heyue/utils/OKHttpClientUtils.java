package cn.com.heyue.utils;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 周成瑜
 * @Date: 9:51 2019/5/24
 * @Description 单例模式的httpclient
 */
public class OKHttpClientUtils {

    private final static Logger log = LoggerFactory.getLogger(OKHttpClientUtils.class);

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8"); // 接口数据格式


    /**
     * 使用volatile保持内存可见性
     */
    private static volatile OkHttpClient okHttpClient;

    private OKHttpClientUtils() {
    }

    /**
     * 双重锁检查
     *
     * @return
     */
    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (OKHttpClientUtils.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient();
                }
            }
        }
        return okHttpClient;
    }

    public static String getJson(String url, String sign, String json) {
        log.info("{}请求:{}", sign, json);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                log.info("查询成功，结果：{}", result);
                return result;
            } else {
                log.info("查询失败，结果：{}", response.isSuccessful());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * POST提交JSON
     *
     * @param url  请求地址
     * @param sign 签名
     * @param json 请求json
     * @return 返回null失败
     */
    public static String postJson(String url, String sign, String json) {
        log.info("{}请求url:{},请求json:{}", sign, url, json);
        OkHttpClient httpClient = new OkHttpClient();
        // TODO 生产换成10 5
        httpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        httpClient.setReadTimeout(30, TimeUnit.SECONDS);
        httpClient.getDispatcher().setMaxRequests(2000);
        httpClient.getDispatcher().setMaxRequestsPerHost(1000);
        RequestBody requestBody = RequestBody.create(JSON_TYPE, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            String body = response.body().string();
            boolean result = response.isSuccessful();
            if (result) {
                log.info("{}接口返回成功：{}", sign, body);
                return body;
            } else {
                log.warn("{}接口返回失败：{}", sign, body);
            }
        } catch (IOException e) {
            e.printStackTrace();

            log.error("{}接口异常：{}", sign, e.getMessage());
        }
        return null;
    }


    /**
     * POST提交JSON
     *
     * @param url            请求地址
     * @param sign           签名
     * @param json           请求json
     * @param connectTimeout 连接超时时间,单位秒
     * @param readTimeout    读取超时时间,单位秒
     * @return 返回null失败
     */
    public static String postJsonData(String url, String sign, String json, int connectTimeout, int readTimeout) {
        log.info("{}请求url:{},请求json{}", sign, url, json);
        OkHttpClient httpClient = OKHttpClientUtils.getInstance();
        httpClient.setConnectTimeout(connectTimeout, TimeUnit.SECONDS);
        httpClient.setReadTimeout(readTimeout, TimeUnit.SECONDS);
        httpClient.getDispatcher().setMaxRequests(2000);
        httpClient.getDispatcher().setMaxRequestsPerHost(1000);
        RequestBody requestBody = RequestBody.create(JSON_TYPE, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            String body = response.body().string();
            boolean result = response.isSuccessful();
            if (result) {
                log.info("{}接口返回成功：{}", sign, body);
                return body;
            } else {
                log.warn("{}接口返回失败：{}", sign, body);
            }
        } catch (IOException e) {
            e.printStackTrace();

            log.error("{}接口异常：{}", sign, e.getMessage());
        }
        return null;
    }

    /**
     * POST提交JSON
     *
     * @param url  请求地址
     * @param sign 签名
     * @param json 请求json
     * @return 返回null失败
     */
    public static String postJsonHead(String url, String sign, String json, String headSign) {
        log.info("{}请求url:{},请求json:{}", sign, url, json);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        httpClient.setReadTimeout(10, TimeUnit.SECONDS);
        httpClient.getDispatcher().setMaxRequests(2000);
        httpClient.getDispatcher().setMaxRequestsPerHost(1000);
        RequestBody requestBody = RequestBody.create(JSON_TYPE, json);
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addHeader("x-lemon-sign", headSign)
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            String body = response.body().string();
            boolean result = response.isSuccessful();
            if (result) {
                log.info("{}接口返回成功：{}", sign, body);
                return body;
            } else {
                log.warn("{}接口返回失败：{}", sign, body);
            }
        } catch (IOException e) {
            e.printStackTrace();

            log.error("{}接口异常：{}", sign, e.getMessage());
        }
        return null;
    }


    /**
     * JSONObject转Map
     *
     * @param map
     * @param jsonObject
     */
    private static void jsonObjectToMap(Map<String, Object> map, JSONObject jsonObject) throws Exception {
        Map<String, Object> resMap = null;
        Iterator it = jsonObject.keySet().iterator();
        // 遍历jsonObject数据，添加到Map对象
        while (it.hasNext()) {
            String key = String.valueOf(it.next());
            Object value = jsonObject.get(key);

            if (value != null) {
                if (StringUtils.equals("resData", key)) {
                    resMap = new HashMap<>();
                    jsonObjectToMap(resMap, (JSONObject) value);
                    map.put(key, resMap);
                } else {
                    map.put(key, value);
                }
            }
        }
    }

    /**
     * get请求，同步方式，获取网络数据，是在主线程中执行的，需要新起线程，将其放到子线程中执行
     *
     * @param url
     * @return
     */
    public static String getDataSynFromNet(String url, String sign) {
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        httpClient.setReadTimeout(10, TimeUnit.SECONDS);
        httpClient.getDispatcher().setMaxRequests(2000);
        httpClient.getDispatcher().setMaxRequestsPerHost(1000);
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        try {
            Response response = httpClient.newCall(request).execute();
            String body = response.body().string();
            boolean result = response.isSuccessful();
            if (result) {
                log.info("{}接口返回成功：{}", sign, body);
                return body;
            } else {
                log.warn("{}接口返回失败：{}", sign, body);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("{}接口异常：{}", sign, e.getMessage());
        }
        return null;
    }


    /**
     * get
     *
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        if (url == null || "".equals(url)) {
            log.error("url为null!");
            return "";
        }

        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() == 200) {
                log.info("http GET 请求成功; [url={}]", url);
                return response.body().string();
            } else {
                log.warn("Http GET 请求失败; [errorCode = {} , url={}]", response.code(), url);
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http GET 请求失败,url:" + url, e);
        }
        return null;
    }

    /**
     * POST提交JSON
     *
     * @param url  请求地址
     * @param sign 签名
     * @param json 请求json
     * @return 返回null失败
     */
    public static String httpPost(String url, String sign, String json, Map<String, String> headers) {
        log.info("{}请求url:{},请求json:{}", sign, url, json);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        httpClient.setReadTimeout(10, TimeUnit.SECONDS);
        httpClient.getDispatcher().setMaxRequests(2000);
        httpClient.getDispatcher().setMaxRequestsPerHost(1000);
        RequestBody requestBody = RequestBody.create(JSON_TYPE, json);

        Request.Builder builder = new Request.Builder();
        headers.forEach((String key, String value) -> builder.header(key, value));
        Request request = builder
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            String body = response.body().string();
            boolean result = response.isSuccessful();
            if (result) {
                log.info("{}接口返回成功：{}", sign, body);
                return body;
            } else {
                log.warn("{}接口返回失败：{}", sign, body);
            }
        } catch (IOException e) {
            e.printStackTrace();

            log.error("{}接口异常：{}", sign, e.getMessage());
        }
        return null;
    }


    /**
     * get with params
     *
     * @param url
     * @param headers
     * @return
     */
    public static String httpGet(String url, Map<String, String> headers, String sign) {
        getInstance();
        log.info("{}请求url:{}", sign, url);
        if (CollectionUtils.isEmpty(headers)) {
            return httpGet(url);
        }

        Request.Builder builder = new Request.Builder();
        headers.forEach((String key, String value) -> builder.header(key, value));
        Request request = builder.get().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() == 200) {
                log.info("http GET 请求成功; [url={}]", url);
                return response.body().string();
            } else {
                log.warn("Http GET 请求失败; [errorxxCode = {} , url={}]", response.code(), url);
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http GET 请求失败,url:" + url, e);
        }
        return null;
    }

    /**
     * 组装get请求参数
     *
     * @param params
     * @return
     */
    public static String paramsConvertUrl(Map<String, String> params) {
        StringBuilder urlParams = new StringBuilder("?");
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlParams.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String urlParamsStr = urlParams.toString();
        return urlParamsStr.substring(0, urlParamsStr.length() - 1);
    }

    private static MyTrustManager mMyTrustManager;

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            mMyTrustManager = new MyTrustManager();
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{mMyTrustManager}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return ssfFactory;
    }

    //实现X509TrustManager接口
    public static class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    //实现HostnameVerifier接口
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static String getJsonSSL(String url, String sign, String json) {
        log.info("{}get请求-支持SSL:{},{}", sign, url, json);
        OkHttpClient client = new OkHttpClient();
        client.setSslSocketFactory(createSSLSocketFactory()).setHostnameVerifier(new TrustAllHostnameVerifier());
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                log.info("查询成功，结果：{}", result);
                return result;
            } else {
                log.info("查询失败，结果：{}", response.isSuccessful());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String postJsonSSL(String url, String sign, String json) {
        log.info("{}post请求-支持SSL:{},{}", sign, url, json);
        OkHttpClient client = new OkHttpClient();
        client.setSslSocketFactory(createSSLSocketFactory()).setHostnameVerifier(new TrustAllHostnameVerifier());
        RequestBody body = RequestBody.create(JSON_TYPE, json);
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                log.info("查询成功，结果：{}", result);
                return result;
            } else {
                if (response.body() != null) {
                    log.info("查询失败，结果：{},{}", response.isSuccessful(), response.body().string());
                } else {
                    log.info("查询失败，结果：{}", response.isSuccessful());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
