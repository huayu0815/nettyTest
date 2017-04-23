package com.xiaoju.ecom.huayu.javaSerializable;

import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * Created by zhaohuayu on 16/9/9.
 */
public class SubscribeReq implements Serializable {
    private static final long serialVersionUID = 1L;
    long subReqId;
    String userName;
    String productName;
    String phoneNumber;
    String address;

    public long getSubReqId() {
        return subReqId;
    }

    public void setSubReqId(long subReqId) {
        this.subReqId = subReqId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().create().toJson(this).toString();
    }
}
