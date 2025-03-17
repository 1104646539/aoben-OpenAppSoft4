package com.open.soft.openappsoft.jinbiao.model;

public class TestDataBean3 {

	private String ErrCode;
	private String ErrMsg;
	private TestDataBean2 Data;

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

	public void setData(TestDataBean2 Data) {
		this.Data = Data;
	}

	public TestDataBean2 getData() {
		return Data;
	}

	@Override
	public String toString() {
		return "TestDataBean3{" +
				"ErrCode='" + ErrCode + '\'' +
				", ErrMsg='" + ErrMsg + '\'' +
				", Data=" + Data +
				'}';
	}
}