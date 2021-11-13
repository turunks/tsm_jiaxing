package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 卡圈存请求
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardTrapReq {

    private String issue_inst; // 发卡机构代码

    private String city_code; // 城市代码

    private String card_no; // 卡应用序列号

    private String card_type; // 卡类型

    private String terminal_code; // 终端编号

    private String money; // 交易金额

    private String card_transaction_num; // 卡交易序号

    private String transaction_datetime; // 交易日期时间

    private String card_balance; // 卡片交易前余额

    private String random; // 随机数：获取卡的4字节随机数

    private String mac1; // MAC1

    private String merchant_num; // 商户号

    private String algorithm_id; // 加密算法标识: 00国际/01国密

    private String region_code; // 地区代码

    private String card_species; // 卡种类型

    public String getIssue_inst() {
        return issue_inst;
    }

    public void setIssue_inst(String issue_inst) {
        this.issue_inst = issue_inst;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCard_transaction_num() {
        return card_transaction_num;
    }

    public void setCard_transaction_num(String card_transaction_num) {
        this.card_transaction_num = card_transaction_num;
    }

    public String getTransaction_datetime() {
        return transaction_datetime;
    }

    public void setTransaction_datetime(String transaction_datetime) {
        this.transaction_datetime = transaction_datetime;
    }

    public String getCard_balance() {
        return card_balance;
    }

    public void setCard_balance(String card_balance) {
        this.card_balance = card_balance;
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

    public String getMerchant_num() {
        return merchant_num;
    }

    public void setMerchant_num(String merchant_num) {
        this.merchant_num = merchant_num;
    }

    public String getAlgorithm_id() {
        return algorithm_id;
    }

    public void setAlgorithm_id(String algorithm_id) {
        this.algorithm_id = algorithm_id;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getCard_species() {
        return card_species;
    }

    public void setCard_species(String card_species) {
        this.card_species = card_species;
    }
}
