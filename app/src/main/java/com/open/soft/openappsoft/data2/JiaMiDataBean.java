package com.open.soft.openappsoft.data2;

import java.util.List;

public class JiaMiDataBean {

    private String ErrCode;
    private String ErrMsg;
    private List<JiaMiData> Data;
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

    public void setData(List<JiaMiData> Data) {
        this.Data = Data;
    }
    public List<JiaMiData> getData() {
        return Data;
    }
}
