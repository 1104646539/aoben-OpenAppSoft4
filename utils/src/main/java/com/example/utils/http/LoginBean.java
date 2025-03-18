package com.example.utils.http;

public class LoginBean {

    /**
     * sn : W123
     * name : qdadmin
     * cypher : 123456
     * type : 2
     * key : 688961777307881472
     */

    private String sn;
    private String name;
    private String cypher;
    private String type;
    private String key;

    public LoginBean(String sn, String name, String cypher, String type, String key) {
        this.sn = sn;
        this.name = name;
        this.cypher = cypher;
        this.type = type;
        this.key = key;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCypher() {
        return cypher;
    }

    public void setCypher(String cypher) {
        this.cypher = cypher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
