package com.heyue.refund.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSignInfo {
    private String mobileNo;
    private String joinChannelMark;//接入渠道标识
    private String extraString;//备用字段

    private String foreignToken;
    private String jumpUrl;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getJoinChannelMark() {
        return joinChannelMark;
    }

    public void setJoinChannelMark(String joinChannelMark) {
        this.joinChannelMark = joinChannelMark;
    }

    public String getExtraString() {
        return extraString;
    }

    public void setExtraString(String extraString) {
        this.extraString = extraString;
    }

    public String getForeignToken() {
        return foreignToken;
    }

    public void setForeignToken(String foreignToken) {
        this.foreignToken = foreignToken;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
}
