package com.heyue.refund.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @Author: zcy
 * @Date: 2020/9/18 9:19 上午
 * Copyright (c) 2020 武汉和悦数字科技有限公司
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardRechargeReq {

    private String issueInst;

    private String cardNo;

    private Integer dealSum;

    private Integer dealType;

    private Integer cardDealNo;

    private String balance;

    private String random;

    private String mac1;

    private Integer skey;

    public String getIssueInst() {
        return issueInst;
    }

    public void setIssueInst(String issueInst) {
        this.issueInst = issueInst;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getDealSum() {
        return dealSum;
    }

    public void setDealSum(Integer dealSum) {
        this.dealSum = dealSum;
    }

    public Integer getDealType() {
        return dealType;
    }

    public void setDealType(Integer dealType) {
        this.dealType = dealType;
    }

    public Integer getCardDealNo() {
        return cardDealNo;
    }

    public void setCardDealNo(Integer cardDealNo) {
        this.cardDealNo = cardDealNo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getMac1() {
        return mac1;
    }

    public void setMac1(String mac1) {
        this.mac1 = mac1;
    }

    public Integer getSkey() {
        return skey;
    }

    public void setSkey(Integer skey) {
        this.skey = skey;
    }
}
