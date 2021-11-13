package cn.com.heyue.bin;

import java.io.Serializable;

/**
 * @ClassName Domestickey
 * @Description 国内密钥obj
 * @Author ly
 * @Date 2020/11/6 19:33
 * @Version 1.0
 */
public class DomesticKey implements Serializable {
    /**
     * 用户卡主控密钥
     */
    private String userCardKey;
    /**
     * 用户卡维护密钥
     */
    private String userCardMaintainKey;
    /**
     * 用户卡应用主控密钥
     */
    private String userAppKey;
    /**
     * 用户卡应用维护密钥
     */
    private String userAppMaintainKey;
    /**
     * 消费密钥
     */
    private String consumeKey;
    /**
     * 充值密钥
     */
    private String rechargeKey;
    /**
     * TAC 密钥
     */
    private String tacKey;
    /**
     * 用户卡应用维护密钥（应用锁定
     */
    private String userLockAppMaintainKey;
    /**
     * PIN 密钥
     */
    private String pinKey;
    /**
     * 互通记录保护密钥-电子现金
     */
    private String contactEcashKey;
    /**
     * 互通记录保护密钥-电子钱包
     */
    private String contactEwalletKey;
    /**
     * 互通记录保护密钥（现金备用）
     */
    private String contactWalletSpareKey;
    /**
     * 用户卡应用维护密钥（应用解锁）
     */
    private String userUnlockAppMaintainKey;
    /**
     * 预留密钥 1
     */
    private String reserveKey1;
    /**
     * 预留密钥 2
     */
    private String reserveKey2;
    /**
     * 充值密钥 2（国际） 密文（3DES）+KCV（明文）。TK 加密  ***通过加密机还原出来的值4907494927122D2CEAFBDC29AC9DDE01 不明确，需要确认***
     */
    private String rechargeKeyBak;

    public String getUserCardKey() {
        return userCardKey;
    }

    public void setUserCardKey(String userCardKey) {
        this.userCardKey = userCardKey;
    }

    public String getUserCardMaintainKey() {
        return userCardMaintainKey;
    }

    public void setUserCardMaintainKey(String userCardMaintainKey) {
        this.userCardMaintainKey = userCardMaintainKey;
    }

    public String getUserAppKey() {
        return userAppKey;
    }

    public void setUserAppKey(String userAppKey) {
        this.userAppKey = userAppKey;
    }

    public String getUserAppMaintainKey() {
        return userAppMaintainKey;
    }

    public void setUserAppMaintainKey(String userAppMaintainKey) {
        this.userAppMaintainKey = userAppMaintainKey;
    }

    public String getConsumeKey() {
        return consumeKey;
    }

    public void setConsumeKey(String consumeKey) {
        this.consumeKey = consumeKey;
    }

    public String getRechargeKey() {
        return rechargeKey;
    }

    public void setRechargeKey(String rechargeKey) {
        this.rechargeKey = rechargeKey;
    }

    public String getTacKey() {
        return tacKey;
    }

    public void setTacKey(String tacKey) {
        this.tacKey = tacKey;
    }

    public String getUserLockAppMaintainKey() {
        return userLockAppMaintainKey;
    }

    public void setUserLockAppMaintainKey(String userLockAppMaintainKey) {
        this.userLockAppMaintainKey = userLockAppMaintainKey;
    }

    public String getPinKey() {
        return pinKey;
    }

    public void setPinKey(String pinKey) {
        this.pinKey = pinKey;
    }

    public String getContactEcashKey() {
        return contactEcashKey;
    }

    public void setContactEcashKey(String contactEcashKey) {
        this.contactEcashKey = contactEcashKey;
    }

    public String getContactEwalletKey() {
        return contactEwalletKey;
    }

    public void setContactEwalletKey(String contactEwalletKey) {
        this.contactEwalletKey = contactEwalletKey;
    }

    public String getContactWalletSpareKey() {
        return contactWalletSpareKey;
    }

    public void setContactWalletSpareKey(String contactWalletSpareKey) {
        this.contactWalletSpareKey = contactWalletSpareKey;
    }

    public String getUserUnlockAppMaintainKey() {
        return userUnlockAppMaintainKey;
    }

    public void setUserUnlockAppMaintainKey(String userUnlockAppMaintainKey) {
        this.userUnlockAppMaintainKey = userUnlockAppMaintainKey;
    }

    public String getReserveKey1() {
        return reserveKey1;
    }

    public void setReserveKey1(String reserveKey1) {
        this.reserveKey1 = reserveKey1;
    }

    public String getReserveKey2() {
        return reserveKey2;
    }

    public void setReserveKey2(String reserveKey2) {
        this.reserveKey2 = reserveKey2;
    }

    public String getRechargeKeyBak() {
        return rechargeKeyBak;
    }

    public void setRechargeKeyBak(String rechargeKeyBak) {
        this.rechargeKeyBak = rechargeKeyBak;
    }
}
