package cn.com.heyue.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import javax.net.ssl.SSLContext;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * function: 发送Http请求 <br/>
 * author:wangjie <br/>
 * date:2016/5/16 <br/>
 */
public class HttpRequestUtils {
	private static final Log log = LogFactory.getLog(HttpRequestUtils.class);

	public static String doPost(String url, String jsonStr) {
		CloseableHttpClient client = HttpClients.createDefault();
		// DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(5000).setConnectionRequestTimeout(5000)
				.setSocketTimeout(10000).build();
		post.setConfig(requestConfig);
		String result = "";
		try {
			StringEntity s = new StringEntity(jsonStr,"UTF-8");
			//s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);
			org.apache.http.HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str="";
				BufferedReader reade = new BufferedReader(new InputStreamReader(res.getEntity().getContent(),"UTF-8"));
			    while((str = reade.readLine()) != null){
			    	result=result+str;
				}
				//result = EntityUtils.toString(res.getEntity(), "utf-8");// 返回json格式：
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 创建基于SSL的请求
	 *
	 * @param url      请求地址
	 * @param param    参数
	 * @param encoding 编码
	 * @return
	 */
	public static String doPostSSL(String url, Map<String, String> param, String encoding) throws Exception {
		log.info("请求开始：" + url + "，编码：" + encoding);
		// 创建信任证书
		CloseableHttpClient httpClient = createSSLClientDefault();
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		UrlEncodedFormEntity urlEntity = null;
		try {
			log.info("拼接请求参数");
			httpPost = new HttpPost(url);
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			for (String key : param.keySet()) {
				paramList.add(new BasicNameValuePair(key, param.get(key)));
			}
			urlEntity = new UrlEncodedFormEntity(paramList, encoding);
			log.info("请求接口地址");
			httpPost.setEntity(urlEntity);
			httpPost.setConfig(RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build());
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			// 如果响应码是200
			if (HttpStatus.SC_OK == statusCode) {
				String rtnStr = EntityUtils.toString(entity);
				log.info("返回结果：" + rtnStr);
				return rtnStr;
			}
		} finally {
			if (response != null) {
				response.close();
			}
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
			httpClient.close();
		}
		return null;
	}

	/**
	 * 创建基于SSL的请求
	 *
	 * @param url      请求地址
	 * @param reqJson  请求的JSON
	 * @param encoding 编码
	 * @return
	 */
	public static String doPostSSL(String url, String reqJson, String encoding) throws Exception {
		log.info("请求开始：" + url + "，编码：" + encoding);
		// 创建信任证书
		CloseableHttpClient httpClient = createSSLClientDefault();
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		try {
			httpPost = new HttpPost(url);
			httpPost.setHeader("Content-type", "application/json; charset=utf-8");
			httpPost.setConfig(RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build());
			StringEntity s = new StringEntity(reqJson, Charset.forName(encoding));
			s.setContentType("application/json");// 发送json数据需要设置contentType
			httpPost.setEntity(s);
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();
			// 如果响应码是200
			if (HttpStatus.SC_OK == statusCode) {
				String rtnStr = EntityUtils.toString(entity, encoding);
				log.info("返回结果：" + rtnStr);
				return rtnStr;
			}
		} finally {
			if (response != null) {
				response.close();
			}
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
			httpClient.close();
		}
		return null;
	}

	// 创建链接
	public static CloseableHttpClient createSSLClientDefault() throws Exception {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new AllTrustStrategy()).build();
		SSLConnectionSocketFactory sslSf = new SSLConnectionSocketFactory(sslContext);
		return HttpClients.custom().setSSLSocketFactory(sslSf).build();
	}

	// 加载证书
	private static class AllTrustStrategy implements TrustStrategy {
		public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
			return true;
		}
	}

	/**
	 * 微信请求的方法
	 *
	 * @param url
	 * @param data
	 * @param p12Path
	 * @param key
	 * @return
	 */
	public static String weixinRequest(String url, String data, String p12Path, String key) {
		log.info("调用微信SSL辅助请求方法");
		StringBuffer message = new StringBuffer();
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(p12Path));
			keyStore.load(instream, key.toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, key.toCharArray()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
					null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			log.info("初始化完成");
			HttpPost httpost = new HttpPost(url);
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept", "*/*");
			httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpost.addHeader("Host", "api.mch.weixin.qq.com");
			httpost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpost.addHeader("Cache-Control", "max-age=0");
			httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			httpost.setEntity(new StringEntity(data, "UTF-8"));
			log.info("请求：" + httpost.getRequestLine());

			CloseableHttpResponse response = httpclient.execute(httpost);
			try {
				HttpEntity entity = response.getEntity();
				log.info("response.getStatusLine:" + response.getStatusLine());
				if (entity != null) {
					log.info("响应长度: " + entity.getContentLength());
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"));
					String text;
					while ((text = bufferedReader.readLine()) != null) {
						message.append(text);
					}
				}
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
				log.error("发生错误：" + e.getMessage());
			} finally {
				response.close();
				log.error("结束finally");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error("异常:" + e1);
		}
		return message.toString();
	}


	/**
	 * 向指定URL发送post方法的请求，字符串
	 * 
	 * @param urlString 地址
	 * @param reqString 请求参数 name1=value1&name2=value2 的形式。
	 * @return String 返回内容
	 */
	public static String doURLPost(String urlString, String reqString) {
		StringBuffer sb = new StringBuffer("");
		try {
			// 创建连接
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			// 下边这行注释掉
			// connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(reqString.getBytes("UTF-8"));
			out.flush();
			out.close();
			// 读取响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String lines;

			while ((lines = reader.readLine()) != null) {
				sb.append(lines);
			}
			reader.close();
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sb.toString().length() == 0 | sb.toString().trim().length() == 0) {
			return "";
		}
		return sb.toString();
	}
	
	/**
	 * 对象转换成key=value字符串
	 * 
	 * @param     obj
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String getStringInfo(Object obj) throws Exception {
		List<Field> fieldList = ReflectionUtils.getDeclaredField(obj);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < fieldList.size(); i++) {
			Field field = fieldList.get(i);
			field.setAccessible(true);
			String fieldName = field.getName();
			if (ReflectionUtils.getFieldValue(obj, fieldName) != null) {
				buf.append(fieldName).append("=").append(ReflectionUtils.getFieldValue(obj, fieldName)).append("&");
			}

		}
		String signSrc = buf.substring(0, buf.length() - 1);
		return signSrc;
	}



	public static String doPost2(String url, String jsonStr,Object obj) {
		CloseableHttpClient client = HttpClients.createDefault();
		// DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(5000).setConnectionRequestTimeout(5000)
				.setSocketTimeout(10000).build();

		String result = "";
		try {
			post.addHeader("content-type", "application/json;charset=UTF-8");
			List<Field> fieldList = ReflectionUtils.getDeclaredField(obj);
			for (int i = 0; i < fieldList.size(); i++) {
				Field field = fieldList.get(i);
				field.setAccessible(true);
				String fieldName = field.getName();
				if (ReflectionUtils.getFieldValue(obj, fieldName) != null) {
					post.addHeader(fieldName, String.valueOf(ReflectionUtils.getFieldValue(obj, fieldName)));
				}
			}
			StringEntity s = new StringEntity(jsonStr,"UTF-8");
			//s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);
			post.setConfig(requestConfig);
			org.apache.http.HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str="";
				BufferedReader reade = new BufferedReader(new InputStreamReader(res.getEntity().getContent(),"UTF-8"));
			    while((str = reade.readLine()) != null){
			    	result=result+str;
				}
				//result = EntityUtils.toString(res.getEntity(), "utf-8");// 返回json格式：
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}


	/**
	 * url进行转码，常用于网络请求
	 *
	 * @param text
	 *            需要加密的文本
	 * @return 返回加密后的文本
	 */
	public static String urlEncoderText(String text) {
		String result = "";
		try {
			result = java.net.URLEncoder.encode(text, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 把json数据转化为参数，为get请求和post请求stringentity的时候使用
	 *
	 * @param argument 请求参数，json数据类型，map类型，可转化
	 * @return 返回拼接参数后的地址
	 */
	public static String changeJsonToArguments(JSONObject argument) {
		Set<String> keys = argument.keySet();
		for (String key : keys) {
			String value = argument.getString(key);
			argument.put(key, urlEncoderText(value));
		}
		String one = argument.toString();
		String two = "?" + one.substring(1, one.length() - 1).replace(",", "&").replace(":", "=").replace("\"", "");
		return two;
	}

}