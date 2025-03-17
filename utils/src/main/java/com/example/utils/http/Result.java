package com.example.utils.http;

public class Result<T> {
    public String ErrCode;
    public String ErrMsg;
    public T Data;

    public Result(String errCode, String errMsg, T data) {
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

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "ErrCode='" + ErrCode + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", Data=" + Data +
                '}';
    }
}
