package com.xiaoju.ecom.huayu.javaSerializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * Created by zhaohuayu on 16/9/9.
 */
public class SubscribeResp implements Serializable {
    private static final long serialVersionUID = 1L;

    long subReqID;
    int respCode;
    String desc;

    public long getSubReqID() {
        return subReqID;
    }

    public void setSubReqID(long subReqID) {
        this.subReqID = subReqID;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return new GsonBuilder().serializeNulls().create().toJson(this).toString();
    }
}
