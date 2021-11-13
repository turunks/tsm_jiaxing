package com.heyue.cityservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 卡账户信息请求
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardAccountInfoReq {

    private String card_no; // 卡应用序列号

    private String terminal_code; // 终端编号

    private String algorithm_id; // 加密算法标识

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    public String getAlgorithm_id() {
        return algorithm_id;
    }

    public void setAlgorithm_id(String algorithm_id) {
        this.algorithm_id = algorithm_id;
    }
}
