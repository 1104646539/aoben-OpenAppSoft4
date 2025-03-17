package com.open.soft.openappsoft.jinbiao.model;

public class PdfRootBean {

    private String ErrCode;
    private String ErrMsg;
    private PdfData Data;
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

    public void setData(PdfData Data) {
        this.Data = Data;
    }
    public PdfData getData() {
        return Data;
    }


}
