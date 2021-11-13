package com.heyue.cityservice.message.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author ：dengjie
 * @date ：2021/1/20
 * @description ：掌厅用户信息
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserHandRes {

    @JSONField(name = "UserName")
    private String userName;

    @JSONField(name = "Province")
    private String province;

    @JSONField(name = "Brand")
    private String brand;

    @JSONField(name = "Status")
    private String status;

    @JSONField(name = "AuthUserID")
    private String authUserID;

    @JSONField(name = "IdentCode")
    private String identCode;

    @JSONField(name = "IdentCodeLevel")
    private String identCodeLevel;

    @JSONField(name = "UID")
    private String UID;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthUserID() {
        return authUserID;
    }

    public void setAuthUserID(String authUserID) {
        this.authUserID = authUserID;
    }

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    public String getIdentCodeLevel() {
        return identCodeLevel;
    }

    public void setIdentCodeLevel(String identCodeLevel) {
        this.identCodeLevel = identCodeLevel;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

}

 