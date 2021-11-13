package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 卡激活
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardActiveSubmitReq {

    private String issue_inst; // 发卡机构代码

    private String city_code; // 城市代码

    private String card_no; // 卡应用序列号

    private String card_type; // 卡类型

    private String terminal_code; // 终端编号

    private String random; // 随机数

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

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
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
