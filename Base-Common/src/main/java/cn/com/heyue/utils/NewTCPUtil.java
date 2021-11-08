package cn.com.heyue.utils;

import cn.com.heyue.pool.SocketClientObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class NewTCPUtil {
    private static final char PPP_FRAME_FLAG = 0x7F;
    private static final char PPP_FRAME_ESC = 0x7E;
    private static final char PPP_FRAME_ENC = 0x20;

    /**
     * 发送TCP请求
     * @param IP         远程主机地址
     * @param port       远程主机端口
     * @param reqData    待发送报文的中文字符串形式
     * @param reqCharset 该方法与远程主机间通信报文的编码字符集(编码为byte[]发送到Server)
     * @return localPort--本地绑定的端口,reqData--请求报文,respData--响应报文,respDataHex--远程主机响应的原始字节的十六进制表示
     */
    public static String sendTCPRequest(String IP, int port, String reqData, String reqCharset){
        int maxTryNum = 5;
        String strReturn = null;
        Socket socket = null;
        try {
            socket = new Socket(IP,port); //客户机
            //2.得到socket读写流
            OutputStream os=socket.getOutputStream();
            //输入流
            InputStream is=socket.getInputStream();
            //BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), reqCharset));

            //将十六进制的字符串转换成字节数组
            byte[] cmdInfor = hexStrToBinaryStr(reqData);

            //3.利用流按照一定的操作，对socket进行读写操作
            os.write(cmdInfor);
            os.flush();
            Thread.sleep(100);
            socket.shutdownOutput();

            /*StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = br.readLine())!= null) {
                System.out.println("收到报文1:"+line);
                buffer.append(line);
            }
            strReturn= binaryToHexString(buffer.toString().getBytes());*/

            int i = 0;
            while (is.available() == 0){
                i++;
                if(i == maxTryNum){
                    return null;
                }
                Thread.sleep(50);
            }
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            strReturn= binaryToHexString(buffer);

            os.close();
            //br.close();
            is.close();
        } catch (Exception e) {
            System.out.println("与[" + IP + ":" + port + "]通信遇到异常,堆栈信息如下");
            e.printStackTrace();
        } finally {
            if (null!=socket && socket.isConnected() && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("关闭客户机Socket时发生异常,堆栈信息如下");
                    e.printStackTrace();
                }
            }
        }
        return strReturn;
    }

    /**
     * 发送TCP请求 多个请求可以使用同一连接
     * @param is
     * @param os
     * @param reqData
     * @return
     */
    public static String sendTCPRequest(Socket socket, InputStream is, OutputStream os, String reqData){
        long startTime = System.currentTimeMillis();
        int maxTryNum = 5;
        String strReturn = null;
        try {
            //将十六进制的字符串转换成字节数组
            byte[] cmdInfor = hexStrToBinaryStr(reqData);

            //3.利用流按照一定的操作，对socket进行读写操作
            os.write(cmdInfor);
            os.flush();
            //Thread.sleep(1000);
            //socket.shutdownOutput();

            int i = 0;
            while (is.available() <= 0){
                if ((System.currentTimeMillis() - startTime) > 5 * 1000) { // 如果超过10秒没有收到服务器发回来的信息，说明socket连接可能已经被远程服务器关闭
                    System.out.println("服务端5秒未返回结果，开始关闭连接");
                    is.close();
                    os.close();
                    socket.close(); // 这时候可以关闭socket连接
                    startTime = System.currentTimeMillis();
                    System.out.println("连接关闭完成");
                    return null;
                }
                i++;
                /*if(i == maxTryNum){
                    return null;
                }*/
                Thread.sleep(50);
            }
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            strReturn= binaryToHexString(buffer);
            strReturn= strReturn.replaceAll(" ", "");
            System.out.println("sendTCPRequest返回报文："+strReturn);
            strReturn = strReturn.substring(0,2)+ NewTCPUtil.hexStrDecode(strReturn.substring(2,strReturn.length()-2))+strReturn.substring(strReturn.length()-2);
        } catch (Exception e) {
            System.out.println("通信遇到异常,堆栈信息如下");
            e.printStackTrace();
            try {
                is.close();
                os.close();
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
        }
        return strReturn;
    }

    //public static String retry(3,()->testsendTCPRequestShare(),(list, e) -> e != null || list == null || list.isEmpty());// 对结果处理


    public static String sendTCPRequestShare(GenericObjectPool<SocketClientObject> pool, SocketClientObject client, String reqData){

        String strReturn = null;
        // 重试次数
        int retryCount = 0;
        // 调用服务的开关，默认打开
        boolean callSwitch = true;
        // 只要调用服务开关开着，并且重试次数不大于最大的重试次数，就调用服务
        while (callSwitch && retryCount <= 1) {//最多调用两次
            try {
                // 调用服务
                System.out.println("开始调用sendTCPRequest");

                Socket socket = client.getNewSocketInfo().getSocket();

                strReturn = NewTCPUtil.sendTCPRequest(socket,socket.getInputStream(),socket.getOutputStream(),reqData);
                if(StringUtils.isNotBlank(strReturn)){
                    System.out.println("返回不为空，关闭重试开关");
                    // 返回不为空把调用服务开关关掉
                    callSwitch = false;
                }else{
                    System.out.println("返回为空，继续重试");
                    callSwitch = true;
                    retryCount++;

                    /*socketInfo.setClosed(true);//
                    SocketPool.distorySocket(socketInfo);
                    System.out.println("开启新连接");
                    socketInfo = SocketPool.getInstance().getSocketInfo();
                    System.out.println("开启新连接完成");*/

                    client.invalidate();
                    client =pool.borrowObject();
                }
            } catch (Exception e) {
                // 发生了异常（比如超时，就需要重试了）
                System.out.println("发生了异常："+e);
                // 调用服务的开关打开
                callSwitch = true;
                retryCount++;

                client.invalidate();
                try {
                    client =pool.borrowObject();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        System.out.println("返回");
        return strReturn;
    }

    /**
     * 将十六进制的字符串转换成字节数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStrToBinaryStr(String hexString) {

        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    /**
     * 将字节数组转换成十六进制的字符串
     *
     * @return
     */
    public static String binaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex = "";
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result += hex + " ";
        }
        return result;
    }

    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName ("UTF-8");//指定字符集编码
        ByteBuffer bb = ByteBuffer.allocate (bytes.length);
        bb.put (bytes);//装载数据
        bb.flip ();//调整回指针为0
        CharBuffer cb = cs.decode (bb);//按照指定字符集进行解码
        return cb.array();//返回字符数组
    }

    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName ("UTF-8");//设定字符集编码代号
        CharBuffer cb = CharBuffer.allocate (chars.length);//按照字符数组长度进行分配空间
        cb.put (chars); //装载数据
        cb.flip (); //指针复位
        //按照编码规则进行编码
        ByteBuffer bb = cs.encode (cb);
        return bb.array();
    }

    /**
     * 特殊字符串转换 做0x7E+value^0x20 即 0x7E –> 0x7E + 0x5E ,0x7F -> 0x7E + 0x5F；
     *
     * @param hexString
     * @return
     */
    public static String hexStrCover(String hexString) {
        hexString = hexString.toUpperCase();
        /*int len = hexString.length()/2;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i ++) {
            String s = hexString.substring(i*2,(i+1)*2);
            if("7E".equals(s)){
                sb.append("7E5E");
            }else if("7F".equals(s)){
                sb.append("7E5F");
            }else {
                sb.append(s);
            }
        }
        return sb.toString();*/


        String ascStr = HexStringUtils.hexToAscii(hexString);
        char[] in = ascStr.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < in.length; i++) {
            if (in[i] == PPP_FRAME_FLAG || in[i] == PPP_FRAME_ESC) {
                sb.append((char) PPP_FRAME_ESC);
                sb.append((char) (in[i] ^ PPP_FRAME_ENC));
            } else {
                sb.append(in[i]);
            }
        }
        //System.out.println("转换后的asc："+sb.toString());

        //System.out.println("转换后的hex:"+HexStringUtils.asciiToHex(sb.toString()));
        return HexStringUtils.asciiToHex(sb.toString());
    }

    /**
     * 特殊字符串转换解码：将 0x7E 紧跟的后一字节做：value^0x20 还原，0x7E 丢弃
     *
     * @param hexString
     * @return
     */
    public static String hexStrDecode(String hexString) {
        hexString = hexString.toUpperCase();


        String ascStr = HexStringUtils.hexToAscii(hexString);
        char[] in = ascStr.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < in.length; i++) {
            if (in[i] == PPP_FRAME_ESC) {
                sb.append((char)(in[i+1] ^ PPP_FRAME_ENC));
                i++;
            } else {
                sb.append(in[i]);
            }
        }
        //System.out.println("转换后的asc："+sb.toString());

        //System.out.println("转换后的hex:"+HexStringUtils.asciiToHex(sb.toString()));
        return HexStringUtils.asciiToHex(sb.toString());
    }

    public static void main(String[] args){
        /*String s = "801D";
        char[] rs = TCPUtil.ppp_encode(getChars(hexStrToBinaryStr(s)));
        String ns = HexStringUtils.bytesToHexString(getBytes(rs));
        System.out.println(ns);

        char[] nsc = TCPUtil.ppp_decode(getChars(hexStrToBinaryStr(ns)));
        System.out.println(HexStringUtils.bytesToHexString(getBytes(nsc)));*/


        /*System.out.println(TCPUtil.hexStrCover("00007E5F05212482FFFFFFFF2482031048951999000000810124820100000420200401202012212025D82A00000012"));
        System.out.println(TCPUtil.hexStrCover("00007E5F"));

        ConcurrentHashMap map = new ConcurrentHashMap();

        System.out.println(String.format("%04x",127));
        System.out.println(TCPUtil.hexStrCover(String.format("%04x",127)));

        String s = HexStringUtils.strToHexString(String.format("%128s","23458798768889987899111111111111"));
        System.out.println(s);

        System.out.println(binaryToHexString(s.getBytes()));*/


        String hexStr = "AAAAACAAA88DEE7EBB7F11007E5F";
        System.out.println("原串："+hexStr);
        System.out.println("特殊字符转换后："+ NewTCPUtil.hexStrCover(hexStr));

        System.out.println("还原hex："+ NewTCPUtil.hexStrDecode(NewTCPUtil.hexStrCover(hexStr)));

        /*String srcCode = String.format("%64s","23458798768889987899111111111111");
        System.out.println("原串："+srcCode);
        String hex1 = HexStringUtils.asciiToHex(srcCode);
        System.out.println("转换后的hex:"+hex1);

        System.out.println(binaryToHexString(hex1.getBytes()));
        String src = HexStringUtils.hexToAscii(binaryToHexString(hex1.getBytes()).replaceAll(" ",""));
        System.out.println("还原:"+src);
        System.out.println("还原:"+HexStringUtils.hexToAscii(src));

        System.out.println(String.format("%08x", Integer.parseInt("100")));

        System.out.println(String.format("%64s","A1100000000000001841587457138"));
        System.out.println(HexStringUtils.asciiToHex(String.format("%64s","A1100000000000001841587457138")));*/

        String resp = "7F42B040000000004100000A0000000000000B195642000000EDE0007F";
        System.out.println("头:"+resp.substring(0,2));
        System.out.println("尾："+resp.substring(resp.length()-2));
        System.out.println("中间:"+resp.substring(2,resp.length()-2));

        System.out.println(resp.substring(0,2)+ NewTCPUtil.hexStrDecode(resp.substring(2,resp.length()-2))+resp.substring(resp.length()-2));
    }
}
