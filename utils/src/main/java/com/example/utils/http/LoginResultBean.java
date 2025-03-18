package com.example.utils.http;

public class LoginResultBean {

    /**
     * accessToken : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiYWJ4ZGEiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJBZG1pbiIsImFpZCI6IjEwMDAiLCJ1aWQiOiIxMDAwIiwidGlkIjoiMTAwIiwia2V5IjoiNjg4OTYxNzc3MzA3ODgxNDcyIiwic2FsdCI6IjB3SVkwYkFma2lmMSIsImV4cCI6MTc0MjMxMDUxOSwiaXNzIjoiYWJ4ZGEiLCJhdWQiOiJhYnhkYXFpbmRhbyJ9.CQ8rwgTqpYPKDCnM-RpXHlCKiUVMPWTrwhjTVTOksSo
     * refreshToken : 036f435ea2ff4c73b5b6a1027fe9bb8d
     * salt : 0wIY0bAfkif1
     * timeout : 0
     */

    private String accessToken;
    private String refreshToken;
    private String salt;
    private int timeout;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
