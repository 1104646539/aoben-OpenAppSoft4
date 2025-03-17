package com.example.utils.http;

public class GetSamplingInfoBean {


    private String SamplingNumber;

    private String AreaId;

    public String getSamplingNumber() {
        return SamplingNumber;
    }

    public void setSamplingNumber(String samplingNumber) {
        SamplingNumber = samplingNumber;
    }


    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String areaId) {
        AreaId = areaId;
    }


    public GetSamplingInfoBean(String samplingNumber,String areaId) {
        SamplingNumber = samplingNumber;
        AreaId = areaId;
    }

}
