package com.open.soft.openappsoft.jinbiao.model;

public class Data {

    private String sampleName;
    private String sampleId;
    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }
    public String getSampleName() {
        return sampleName;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }
    public String getSampleId() {
        return sampleId;
    }

    @Override
    public String toString() {
        return "Data{" +
                "sampleName='" + sampleName + '\'' +
                ", sampleId='" + sampleId + '\'' +
                '}';
    }
}
