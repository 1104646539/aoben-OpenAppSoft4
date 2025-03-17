package com.open.soft.openappsoft.jinbiao.model;

public class NewSampleNameData {


    private String productId;
    private String objectName;
    private int objectId;


    public NewSampleNameData(String productId, String objectName, int objectId) {
        this.productId = productId;
        this.objectName = objectName;
        this.objectId = objectId;
    }

    public NewSampleNameData(){}

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    @Override
    public String toString() {
        return "NewSampleNameData{" +
                "productId='" + productId + '\'' +
                ", objectName='" + objectName + '\'' +
                ", objectId=" + objectId +
                '}';
    }
}
