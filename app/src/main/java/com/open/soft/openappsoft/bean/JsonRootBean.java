/**
 * Copyright 2020 bejson.com
 */
package com.open.soft.openappsoft.bean;

/**
 * Auto-generated: 2020-09-16 15:14:25
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private String ErrCode;
    private String ErrMsg;
    private AppUpdateBean Data;

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

    public void setData(AppUpdateBean Data) {
        this.Data = Data;
    }

    public AppUpdateBean getData() {
        return Data;
    }

}