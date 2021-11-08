package cn.com.heyue.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import javax.net.ssl.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author fuzuyuan
 * @Description
 * @create 2021-05-26 1:35 下午
 */
public class Test {

    private static Socket socket = null;

    // 生产
    private static String host = "111.48.26.202";
    private static int port = 47777;

    private MyTrustManager mMyTrustManager;

    private SSLSocketFactory createSSLSocketFactory() {
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
    public class MyTrustManager implements X509TrustManager {
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
    private class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private void test(){
        OkHttpClient client = new OkHttpClient();
        client.setSslSocketFactory(createSSLSocketFactory()).setHostnameVerifier(new TrustAllHostnameVerifier());
        Request request = new Request.Builder().url("https://36.158.218.168:20914/api/v1/number/queryNumberSegment?mobileNo=15750315747").build();
//        Request request = new Request.Builder().url("http://211.138.236.210:9201/api/v1/query/numberSegment?mobileNo=15750315747").build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            System.out.println("查询成功，结果：" + result);
            if (response.isSuccessful()) {
//                String result = response.body().string();
//                System.out.println("查询成功，结果：" + result);
            }else{
                System.out.println("查询失败，结果：" + response.isSuccessful());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
//        Test test = new Test();
//        test.test();

        // 国际 44C010CFBCABB1C914A19C75B8694CAB DB6CEC
        String gj = Test.decryDataDes("C57E8996DA99DB4DF0F8C27BBE1414CE","712");
        System.out.println("国际:" + gj);

        // 国密 8CDA26A2DE560A4F2D309E9F5BAE14F8 446F3E
        String gm = Test.decryDataSm("0A81D2C03D148C647F1EDFFE2C6941ED","713");
        System.out.println("国密:" + gm);

        // 国际 44C010CFBCABB1C914A19C75B8694CAB DB6CEC
        String gj1 = Test.decryDataDes("C57E8996DA99DB4DF0F8C27BBE1414CE","714");
        System.out.println("国际1:" + gj1);

        // 国密 8CDA26A2DE560A4F2D309E9F5BAE14F8 446F3E
        String gm1 = Test.decryDataSm("0A81D2C03D148C647F1EDFFE2C6941ED","715");
        System.out.println("国密1:" + gm1);
    }

    /**
     * 初始化
     */
    public static void initSocket() throws Exception {
        System.out.println("初始化socket");
        if (socket == null || socket.isClosed()) {
            socket = new Socket(host, port);
            System.out.println("初始化socket成功");
        } else {
            System.out.println("socket已初始化");
        }
    }

    /**
     * 长链接发送
     *
     * @param msg 发送信息
     * @return
     * @throws Exception
     */
    public static String longConnectSend(String msg) throws Exception {
        DataOutputStream out = null;
        InputStream is = null;
        try {
            initSocket();

            byte[] reqByteArr = new byte[msg.length() + 2];
            reqByteArr[0] = 0x00;
            reqByteArr[1] = (byte) msg.length();
            System.arraycopy(msg.getBytes(), 0, reqByteArr, 2, msg.length());

            System.out.println("发送请求:" + msg);
            out = new DataOutputStream(socket.getOutputStream());
            out.write(reqByteArr);
            out.flush();

            System.out.println("接收请求");
            is = socket.getInputStream();
            byte[] resp = new byte[is.available()];
            is.read(resp);

            byte[] resByteArr = new byte[resp.length - 2];
            System.arraycopy(resp, 2, resByteArr, 0, resp.length - 2);
            return new String(resByteArr, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                out.close();
            }
            if (null != is) {
                is.close();
            }
        }
        return null;
    }

    /**
     * 短链接发送
     *
     * @param msg
     * @return
     * @throws Exception
     */
    public static String shortConnectSend(String msg) throws Exception {
        DataOutputStream out = null;
        InputStream is = null;
        Socket socketShort = null;
        try {
            socketShort = new Socket(host, port);
            System.out.println("初始化socket成功");

            byte[] reqByteArr = new byte[msg.length() + 2];
            reqByteArr[0] = 0x00;
            reqByteArr[1] = (byte) msg.length();
            System.arraycopy(msg.getBytes(), 0, reqByteArr, 2, msg.length());

            System.out.println("发送请求:" + msg);
            out = new DataOutputStream(socketShort.getOutputStream());
            out.write(reqByteArr);
            out.flush();

            byte[] received = new byte[1024];
            socketShort.getInputStream().read(received);

            // 第二个是长度
            byte[] resByteArr = new byte[received[1]];
            System.arraycopy(received, 2, resByteArr, 0, received[1]);

            String rtnResult = new String(resByteArr, "utf-8");
            System.out.println("接收请求:" + rtnResult);
            return rtnResult;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                out.close();
            }
            if (null != is) {
                is.close();
            }
            if (null != socketShort) {
                socketShort.close();
            }
            System.out.println("关闭成功");
        }
        return null;
    }

    /**
     * 解密
     *
     * @param data 密文数据
     * @return
     */
    public static String decryDataDes(String data, String keyIndex) throws Exception {
        data = "U1X101109K" + keyIndex + "001016" + data;
        String rtnData = shortConnectSend(data);
        if ("U2".equals(rtnData.substring(0, 2))) {
            if ("00".equals(rtnData.substring(2, 4))) {
                // 数据长度
                int length = Integer.valueOf(rtnData.substring(4, 7));
                return rtnData.substring(7, 7 + 16 * 2);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 解密
     *
     * @param data 密文数据
     * @return
     */
    public static String decryDataSm(String data, String keyIndex) throws Exception {
        data = "U1S101109K" + keyIndex + "001016" + data;
        String rtnData = shortConnectSend(data);
        if ("U2".equals(rtnData.substring(0, 2))) {
            if ("00".equals(rtnData.substring(2, 4))) {
                // 数据长度
                int length = Integer.valueOf(rtnData.substring(4, 7));
                return rtnData.substring(7, 7 + 16 * 2);
            } else {
                return null;
            }
        }
        return null;
    }
}
