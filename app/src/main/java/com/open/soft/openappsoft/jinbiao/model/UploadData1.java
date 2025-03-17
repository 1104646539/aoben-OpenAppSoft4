/**
  * Copyright 2020 json.cn 
  */
package com.open.soft.openappsoft.jinbiao.model;

/**
 * Auto-generated: 2020-11-07 13:54:27
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class UploadData1 {

    private String ErrCode;
    private String ErrMsg;
    private UploadData2 UploadData2;
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

    public void setUploadData2(UploadData2 UploadData2) {
         this.UploadData2 = UploadData2;
     }
     public UploadData2 getUploadData2() {
         return UploadData2;
     }

    @Override
    public String toString() {
        return "UploadData1{" +
                "ErrCode='" + ErrCode + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                ", UploadData2=" + UploadData2 +
                '}';
    }
}