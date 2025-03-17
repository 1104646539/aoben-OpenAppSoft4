package com.open.soft.openappsoft.jinbiao.model;

import java.util.List;

public class SampleTypeJsonRootBean {

    private String ErrCode;
    private String ErrMsg;
    private List<SampleTypeData> Data;
    public void setErrCode(String ErrCode) {
        this.ErrCode = ErrCode;
    }
    public String getErrCode() {
        return ErrCode;
    }

    public void setErrMsg(String ErrMsg) {
        this.ErrMsg = ErrMsg;
    }
    public String getErrMsg() {
        return ErrMsg;
    }

    public void setData(List<SampleTypeData> Data) {
        this.Data = Data;
    }
    public List<SampleTypeData> getData() {
        return Data;
    }

    @Override
    public String toString() {
        return "SampleTypeJsonRootBean{" +
                "ErrCode='" + ErrCode + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", Data=" + Data +
                '}';
    }
}
