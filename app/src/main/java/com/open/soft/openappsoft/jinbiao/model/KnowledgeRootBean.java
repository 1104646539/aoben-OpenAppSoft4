package com.open.soft.openappsoft.jinbiao.model;

import java.util.List;

public class KnowledgeRootBean {
    private String ErrCode;
    private String ErrMsg;
    private List<KnowledgeData> Data;
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

    public void setData(List<KnowledgeData> Data) {
        this.Data = Data;
    }
    public List<KnowledgeData> getData() {
        return Data;
    }
}
