package cn.com.heyue.utils;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class SocketTest {

    private static Socket socket = null;
    // 测试
    private static String host = "58.48.161.4";
    private static int port = 47777;

    // 生产
    /*private static String host = "114.242.105.153";
    private static int port = 47777;*/

    public static void main(String[] args) throws Exception {
        // 测试
//        String s = SocketUtils.shortConnectSend("U1X101109K7A00010166D0A30E6F2869B2C5F03E1EFA34ADC51");
        // 生产
        //String s = SocketUtils.shortConnectSend("U1X101109K010001016F907C325D54CDD2645F9592BC0ACA5A6");
        //System.out.println(s);

        String s = SocketTest.decryData("7ABC4D04611F9C88E2B3557E9FA5D617");
        System.out.println(s);
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

            System.out.println("发送请求:"+msg);
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

            System.out.println("发送请求:{}"+msg);
            out = new DataOutputStream(socketShort.getOutputStream());
            out.write(reqByteArr);
            out.flush();

            byte[] received = new byte[1024];
            socketShort.getInputStream().read(received);

            // 第二个是长度
            byte[] resByteArr = new byte[received[1]];
            System.arraycopy(received, 2, resByteArr, 0, received[1]);

            String rtnResult = new String(resByteArr, "utf-8");
            System.out.println("接收请求:{}"+rtnResult);
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
    public static String decryData(String data) throws Exception {
        // TODO 暂写死
        data = "U1X101109K010001016" + data;
        String rtnData = shortConnectSend(data);
        if (rtnData.substring(0, 2).equals("U2")) {
            if (rtnData.substring(2, 4).equals("00")) {
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
