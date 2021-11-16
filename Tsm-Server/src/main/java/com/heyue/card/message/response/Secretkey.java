package com.heyue.card.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 城市平台回馈文件数据体--国际密钥
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Secretkey {

    private String consumeKey; // 消费密钥

    private String contactEcashKey; // 互通记录保护密钥-电子现金

    private String contactEwalletKey; // 互通记录保护密钥（现金备用）

    private String contactWalletSpareKey; // 用户卡应用维护密钥（应用解锁

    private String pinKey; // PIN 密钥

    private String rechargeKey; // 充值密钥

    private String reserveKey1; // 预留密钥 1

    private String reserveKey2; // 预留密钥 2

    private String tacKey; // TAC 密钥

    private String userAppKey; // 用户卡主控密钥

    private String userAppMaintainKey; // 用户卡应用主控密钥

    private String userCardKey; //用户卡维护密钥

    private String userCardMaintainKey; // 用户卡应用维护密钥

    private String userLockAppMaintainKey; // 用户卡应用维护密钥（应用锁定）

    private String userUnlockAppMaintainKey; // 用户卡应用维护密钥（应用解锁）



//    @Override
//    public String joint() {
//    }
}
