package com.open.soft.openappsoft.jinbiao.model;

import java.util.List;

public class TestDataBean1 {

	private String ErrCode;
	private String ErrMsg;
	private List<TestDataBean2> Data;

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

	public void setData(List<TestDataBean2> Data) {
		this.Data = Data;
	}

	public List<TestDataBean2> getData() {
		return Data;
	}

	@Override
	public String toString() {
		return "TestDataBean1{" +
				"ErrCode='" + ErrCode + '\'' +
				", ErrMsg='" + ErrMsg + '\'' +
				", Data=" + Data +
				'}';
	}
}