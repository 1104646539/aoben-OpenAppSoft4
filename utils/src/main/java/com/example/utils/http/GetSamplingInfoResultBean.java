package com.example.utils.http;

public class GetSamplingInfoResultBean {

    /**
     * SamplingName : 千禧 样本名称
     * SamplingType : 水果类 样本类型
     * SamplingDept : 菜市场 被检单位
     * SamplingSource : 摊位 样本来源
     */

    private String SamplingName;
    private String SamplingType;
    private String SamplingDept;
    private String SamplingSource;
    private String SamplingNumber;

    public String getSamplingName() {
        return SamplingName;
    }

    public void setSamplingName(String SamplingName) {
        this.SamplingName = SamplingName;
    }

    public String getSamplingType() {
        return SamplingType;
    }

    public void setSamplingType(String SamplingType) {
        this.SamplingType = SamplingType;
    }

    public String getSamplingDept() {
        return SamplingDept;
    }

    public void setSamplingDept(String samplingDept) {
        SamplingDept = samplingDept;
    }

    public String getSamplingSource() {
        return SamplingSource;
    }

    public void setSamplingSource(String samplingSource) {
        SamplingSource = samplingSource;
    }

    public String getSamplingNumber() {
        return SamplingNumber;
    }

    public void setSamplingNumber(String samplingNumber) {
        SamplingNumber = samplingNumber;
    }

    @Override
    public String toString() {
        return "GetSamplingInfoResultBean{" +
                "SamplingName='" + SamplingName + '\'' +
                ", SamplingType='" + SamplingType + '\'' +
                ", SamplingDept='" + SamplingDept + '\'' +
                ", SamplingSource='" + SamplingSource + '\'' +
                ", SamplingNumber='" + SamplingNumber + '\'' +
                '}';
    }
}
