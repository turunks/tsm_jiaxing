package cn.com.heyue.utils;

import cn.com.heyue.nfc.service.NfcService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：yinbeng
 * @date ：Created in 2020-04-22 12:56
 * @description：SocketPool
 */
@Slf4j
//@Component
public class SocketPool {
    private static int SOCKET_DEFAULT_COUNT = NfcConstant.SOCKET_DEFAULT_COUNT; //默认连接池数量
    private static int SOCKET_CHECK_TIME = NfcConstant.SOCKET_CHECK_TIME;//连接检查间隔 10秒
    private static int SOCKET_SESSION_TIME_OUT = NfcConstant.SOCKET_SESSION_TIME_OUT;//会话过期时间 600秒

    /**
     * socketMap
     */
    public static ConcurrentHashMap<Integer, SocketInfo> socketMap = new ConcurrentHashMap<Integer, SocketInfo>();

    private static SocketPool instance = new SocketPool();

    private SocketPool(){}

    public static SocketPool getInstance(){
        if(instance == null){
            synchronized (SocketPool.class) {
                if(instance == null){
                    instance = new SocketPool();
                }
            }
        }
        return instance;
    }

    /*static {
        instance.initSocket(true);
    }*/

    /**
     * @DateTime 2014-8-25 下午3:18:52
     * @User liuxingmi
     * @Desc 初始化链接池
     * @param isAllReInit  是否全部重新初始化
     * @return void
     */
    public  void initSocket(boolean isAllReInit){
        log.info("initSocket开始初始化socket客户端连接池，连接池数量：{}",SOCKET_DEFAULT_COUNT);
        for (int i = 0; i < SOCKET_DEFAULT_COUNT; i++) {

            if(isAllReInit){
                socketMap.put(i, setSocketInfo( i, true, false));
            } else {
                if(socketMap.get(i) == null || socketMap.get(i).isClosed()){
                    socketMap.put(i, setSocketInfo( i, true, false));
                }
            }
        }

        log.info("initSocket完成初始化socket客户端连接池");
        new CheckSocketThread().start();

    }

    /**
     * @Desc 设置socketInfo值
     * @param key
     * @param isFree
     * @param isClosed
     * @return NewSocketInfo
     */
    private static SocketInfo setSocketInfo(Integer key, boolean isFree, boolean isClosed){
        SocketInfo socketInfo = new SocketInfo();
        Socket socket = createSocket();
        socketInfo.setFree(isFree);
        socketInfo.setSocket(socket);
        socketInfo.setSocketId(key);
        socketInfo.setFreeStartTimeStamp(DateUtils.getNowTimeStamp());

        Map map = NfcService.signIn(socketInfo);
        if(map != null){
            socketInfo.setClosed(isClosed);
            socketInfo.setSessionCode(map.get("sessionCode").toString());
            socketInfo.setBackListVer(map.get("backListVer").toString());
        }else{
            socketInfo.setClosed(!isClosed);
        }
        return socketInfo;
    }

    /**
     * @DateTime 2014-8-25 下午3:19:06
     * @User liuxingmi
     * @Desc 获取名字服务器链接
     * @return
     * NewSocketInfo
     */
    public  SocketInfo getSocketInfo(){

        SocketInfo socketInfo = null;

        if(socketMap.size() < SOCKET_DEFAULT_COUNT){
            initSocket(false);
        }

        if(socketMap.size() > 0){
            for (Map.Entry<Integer, SocketInfo> entry : socketMap.entrySet()) {
                socketInfo = entry.getValue();
                if(socketInfo.isFree() && ! socketInfo.getSocket().isClosed()){
                    socketInfo.setFree(false);
                    return socketInfo;
                }
            }
        } else {
            log.info("socket连接池数量为零,初始连接池");
            initSocket(true);
            for (Map.Entry<Integer, SocketInfo> entry : socketMap.entrySet()) {
                socketInfo = entry.getValue();
                if(socketInfo.isFree() && ! socketInfo.getSocket().isClosed()){
                    socketInfo.setFree(false);
                    return socketInfo;
                }
            }
        }

        log.info("socket连接池所有连接都忙，创建临时链接。");

        socketInfo = setSocketInfo(-1, true, true);
        log.info("成功创建socket临时连接");
        return socketInfo;

    }

    /**
     * 释放socket
     * @param socketId
     */
    public static void distorySocketById(Integer socketId){

        log.debug("释放socket连接,连接id:{}",socketId);
        SocketInfo socketInfo = socketMap.get(socketId);
        socketInfo.setFree(true);
        socketInfo.setFreeStartTimeStamp(DateUtils.getNowTimeStamp());
    }

    /**
     * @DateTime 2014-8-25 下午3:19:42
     * @User liuxingmi
     * @Desc 释放socket
     * @param socketInfo
     * void
     */
    public static void distorySocket(SocketInfo socketInfo){
        if(socketInfo == null){
            return;
        }

        if( ! socketInfo.isClosed()){
            log.debug("链接池socket，释放资源。");
            distorySocketById(socketInfo.getSocketId());
            return;
        }

        log.debug("可关闭临时链接，关闭socket");
        try {
            if(socketInfo.getSocket() != null){
                //socketInfo.getSocket().close();
                closeConnection(socketInfo);
            }
        } catch (IOException e) {
            log.error("关闭socket连接失败,{}", e);
        }
        socketInfo = null;

    }

    public static void closeConnection(SocketInfo socketInfo) throws IOException {
        Socket socket = socketInfo.getSocket();
        if(!socket.isClosed()){
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    System.out.println("关闭客户机输出流时发生异常,堆栈信息如下");
                    e.printStackTrace();
                }
            }
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    System.out.println("关闭客户机输入流时发生异常,堆栈信息如下");
                    e.printStackTrace();
                }
            }
            if (null!=socket && socket.isConnected() && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("关闭客户机Socket时发生异常,堆栈信息如下");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @DateTime 2014-8-25 下午3:19:51
     * @User liuxingmi
     * @Desc 创建socket
     * @return
     * Socket
     */
    public static Socket createSocket(){
        Socket socket = null;
        try {
            socket = new Socket(NfcConstant.NFC_TSM_HOST,NfcConstant.NFC_TSM_PORT);
        } catch (IOException e) {
            log.error("连接服务器失败 ip:{},port:{},失败原因:{}",NfcConstant.NFC_TSM_HOST ,NfcConstant.NFC_TSM_PORT,e);
        }
        return socket;
    }

    class CheckSocketThread extends Thread {
        @Override
        public void run() {
            while (true) {
                log.debug("开始检测连接池状态...");
                if(socketMap.size() < SOCKET_DEFAULT_COUNT){
                    log.info("socket连接池数量小于默认连接数，增加socket连接");
                    initSocket(false);
                }

                try{
                    for (Map.Entry<Integer, SocketInfo> entry : socketMap.entrySet() ) {
                        SocketInfo socketInfo = entry.getValue();
                        if(socketInfo.getSocket() == null || socketInfo.isClosed()){
                            if(socketInfo.getSocket() != null && socketInfo.isClosed()){
                                log.info("关闭第{}个socket连接",entry.getKey());
                                closeConnection(socketInfo);
                            }
                            log.info("第{}个socket连接已关闭，重新创建连接",entry.getKey());
                            socketInfo = setSocketInfo(entry.getKey(),true,false);
                            socketMap.put(entry.getKey(),socketInfo);
                        } else {
                            if(socketInfo.isFree()){
                                log.info("第{}个socket连接空闲",entry.getKey());
                                Long freeTimeStamp = socketInfo.getFreeStartTimeStamp();
                                Long curTimeStamp = DateUtils.getNowTimeStamp();
                                if(curTimeStamp - freeTimeStamp >= SOCKET_SESSION_TIME_OUT){//连接空闲时间超过10分钟就关闭
                                    log.info("第{}个socket连接空闲时间超期，关闭连接并重新创建连接",entry.getKey());
                                    closeConnection(socketInfo);
                                    socketInfo = setSocketInfo(entry.getKey(),true,false);
                                    socketMap.put(entry.getKey(),socketInfo);
                                    continue;
                                }
                            }else{
                                log.info("第{}个socket连接正在工作",entry.getKey());
                            }
                        }
                    }
                }catch (Exception e){
                    log.error("检查出现异常:{}",e);
                }


                try {
                    sleep(SOCKET_CHECK_TIME);
                } catch (Exception e) {
                }
            }
        }
    }
}
