package com.open.soft.openappsoft.jinbiao.model;

import java.util.List;

public class NewSampleNameDataBean {


    private String ErrCode;
    private String ErrMsg;
    private List<NewSampleNameData> Data;

    public NewSampleNameDataBean(){}

    public NewSampleNameDataBean(String errCode, String errMsg, List<NewSampleNameData> data) {
        ErrCode = errCode;
        ErrMsg = errMsg;
        Data = data;
    }

    public String getErrCode() {
        return ErrCode;
    }

    public void setErrCode(String errCode) {
        ErrCode = errCode;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

    public List<NewSampleNameData> getData() {
        return Data;
    }

    public void setData(List<NewSampleNameData> data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "NewSampleNameDataBean{" +
                "ErrCode='" + ErrCode + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", Data=" + Data +
                '}';
    }
}
