package com.open.soft.openappsoft.jinbiao.model;

import java.util.List;

public class SampleSourceJsonRootBean {

    private String ErrCode;
    private String ErrMsg;
    private List<SampleSourceData> Data;
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

    public void setData(List<SampleSourceData> Data) {
        this.Data = Data;
    }
    public List<SampleSourceData> getData() {
        return Data;
    }
}
