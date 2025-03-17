package com.example.utils.http;

import java.util.List;

public class SendResultBean {

    /**
     * QRCode : 4hjxkgO6T
     * LocationX : 117
     * LocationY : 30
     * Result : 1
     * ResultValue : 0.005
     * SamplingNumber : 123
     * AreaId : TianjinManage
     * OperatorId : hxchenwei
     * ResultData : [{"Title":"a","Id":"b","Value":"c"}]
     */

    private String QRCode = "";
    private String LocationX = "";
    private String LocationY = "";
    private String Result = "";
    private String ResultValue = "";
    private String SamplingNumber = "";
    private String AreaId = "";
    private String OperatorId = "";
    private List<ResultDataBean> ResultData;

    @Override
    public String toString() {
        return "SendResultBean{" +
                "QRCode='" + QRCode + '\'' +
                ", LocationX='" + LocationX + '\'' +
                ", LocationY='" + LocationY + '\'' +
                ", Result='" + Result + '\'' +
                ", ResultValue='" + ResultValue + '\'' +
                ", SamplingNumber='" + SamplingNumber + '\'' +
                ", AreaId='" + AreaId + '\'' +
                ", OperatorId='" + OperatorId + '\'' +
                ", ResultData=" + ResultData +
                '}';
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public String getLocationX() {
        return LocationX;
    }

    public void setLocationX(String LocationX) {
        this.LocationX = LocationX;
    }

    public String getLocationY() {
        return LocationY;
    }

    public void setLocationY(String LocationY) {
        this.LocationY = LocationY;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public String getResultValue() {
        return ResultValue;
    }

    public void setResultValue(String ResultValue) {
        this.ResultValue = ResultValue;
    }

    public String getSamplingNumber() {
        return SamplingNumber;
    }

    public void setSamplingNumber(String SamplingNumber) {
        this.SamplingNumber = SamplingNumber;
    }

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String AreaId) {
        this.AreaId = AreaId;
    }

    public String getOperatorId() {
        return OperatorId;
    }

    public void setOperatorId(String OperatorId) {
        this.OperatorId = OperatorId;
    }

    public List<ResultDataBean> getResultData() {
        return ResultData;
    }

    public void setResultData(List<ResultDataBean> ResultData) {
        this.ResultData = ResultData;
    }

    public static class ResultDataBean {
        /**
         * Title : a
         * Id : b
         * Value : c
         */

        private String Title;
        private String Id;
        private String Value;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }
    }
}
