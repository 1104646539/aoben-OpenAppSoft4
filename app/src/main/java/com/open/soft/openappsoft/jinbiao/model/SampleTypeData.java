package com.open.soft.openappsoft.jinbiao.model;

public class SampleTypeData {

    private String sampleId;
    private String sampleName;
    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }
    public String getSampleId() {
        return sampleId;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }
    public String getSampleName() {
        return sampleName;
    }

    @Override
    public String toString() {
        return "SampleTypeData{" +
                "sampleId='" + sampleId + '\'' +
                ", sampleName='" + sampleName + '\'' +
                '}';
    }
}
