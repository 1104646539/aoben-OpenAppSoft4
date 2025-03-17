package com.open.soft.openappsoft.jinbiao.model;

import java.util.List;

public class LinkageJsonRootBean {

    private String ErrMsg;
    private String ErrCode;
    private List<LinkageData> Data;
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

    public void setData(List<LinkageData> Data) {
        this.Data = Data;
    }
    public List<LinkageData> getData() {
        return Data;
    }

}
