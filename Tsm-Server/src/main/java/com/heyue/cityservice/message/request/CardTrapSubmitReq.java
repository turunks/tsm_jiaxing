package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 卡圈存请求提交
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardTrapSubmitReq {

    private String issue_inst; // 发卡机构代码

    private String city_code; // 城市代码

    private String card_no; // 卡应用序列号

    private String card_type; // 卡类型

    private String terminal_code; // 终端编号

    private String transaction_num; // 终端交易序号

    private String card_transaction_num; // 卡交易序号

    private String transaction_datetime; // 交易日期时间

    private String transaction_amount; // 交易金额

    private String merchant_num; // 商户号

    private String ret_status; // 写卡状态

    private String TAC; // 交易验证码

    private String algorithm_id; // 加密算法标识

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

    public String getTransaction_num() {
        return transaction_num;
    }

    public void setTransaction_num(String transaction_num) {
        this.transaction_num = transaction_num;
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

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getMerchant_num() {
        return merchant_num;
    }

    public void setMerchant_num(String merchant_num) {
        this.merchant_num = merchant_num;
    }

    public String getRet_status() {
        return ret_status;
    }

    public void setRet_status(String ret_status) {
        this.ret_status = ret_status;
    }

    public String getTAC() {
        return TAC;
    }

    public void setTAC(String TAC) {
        this.TAC = TAC;
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
