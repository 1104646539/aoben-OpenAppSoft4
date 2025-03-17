package com.open.soft.openappsoft.data2;

import java.util.List;

public class JsonRootBean {

    private String ErrCode;
    private String ErrMsg;
    private List<Data> Data;
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

    public void setData(List<Data> Data) {
        this.Data = Data;
    }
    public List<Data> getData() {
        return Data;
    }

    @Override
    public String toString() {
        return "JsonRootBean{" +
                "ErrCode='" + ErrCode + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", Data=" + Data +
                '}';
    }
}
