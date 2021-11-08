package cn.com.heyue.utils;

import java.net.Socket;

/**
 * @author ：yinbeng
 * @date ：Created in 2020-04-22 12:54
 * @description：SocketInfo
 */
public class SocketInfo {
    /**
     * socket
     */
    private Socket socket;
    /**
     * 是否空闲 （是：true  否：false）
     */
    private boolean isFree;

    /**
     * 空闲开始时间戳 秒
     */
    private Long freeStartTimeStamp;
    /**
     * socket id
     */
    private Integer socketId;

    /**
     * 是否为可关闭链接 （是：true  否：false）
     */
    private boolean isClosed;

    /**
     * 会话码 本次连接的唯一标识
     */
    private String sessionCode;

    private String backListVer;

    public String getBackListVer() {
        return backListVer;
    }

    public void setBackListVer(String backListVer) {
        this.backListVer = backListVer;
    }

    public Long getFreeStartTimeStamp() {
        return freeStartTimeStamp;
    }

    public void setFreeStartTimeStamp(Long freeStartTimeStamp) {
        this.freeStartTimeStamp = freeStartTimeStamp;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean isFree) {
        this.isFree = isFree;
    }

    public Integer getSocketId() {
        return socketId;
    }

    public void setSocketId(Integer socketId) {
        this.socketId = socketId;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }
}
