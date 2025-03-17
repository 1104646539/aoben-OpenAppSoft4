package com.open.soft.openappsoft.jinbiao.model;

import java.util.List;

public class CompanyNameRootBean {

    private String ErrCode;
    private String ErrMsg;
    private List<CompanyNameData> Data;
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

    public void setData(List<CompanyNameData> Data) {
        this.Data = Data;
    }
    public List<CompanyNameData> getData() {
        return Data;
    }
}
