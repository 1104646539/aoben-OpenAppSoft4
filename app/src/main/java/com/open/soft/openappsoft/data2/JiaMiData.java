package com.open.soft.openappsoft.data2;

public class JiaMiData {


    private String Id;
    private String Name;
    private String EncryptionType;
    private int Order;
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getId() {
        return Id;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    public String getName() {
        return Name;
    }

    public void setEncryptionType(String EncryptionType) {
        this.EncryptionType = EncryptionType;
    }
    public String getEncryptionType() {
        return EncryptionType;
    }

    public void setOrder(int Order) {
        this.Order = Order;
    }
    public int getOrder() {
        return Order;
    }

}
