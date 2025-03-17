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
public class UploadData2 {

    private String QRCode;
    private String SamplingNumber;
    private String ErrCode;
    private String ErrMsg;
    public void setQRCode(String QRCode) {
         this.QRCode = QRCode;
     }
     public String getQRCode() {
         return QRCode;
     }

    public void setSamplingNumber(String SamplingNumber) {
         this.SamplingNumber = SamplingNumber;
     }
     public String getSamplingNumber() {
         return SamplingNumber;
     }

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

    @Override
    public String toString() {
        return "UploadData2{" +
                "QRCode='" + QRCode + '\'' +
                ", SamplingNumber='" + SamplingNumber + '\'' +
                ", ErrCode='" + ErrCode + '\'' +
                ", ErrMsg='" + ErrMsg + '\'' +
                '}';
    }
}