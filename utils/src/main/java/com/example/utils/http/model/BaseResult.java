package com.example.utils.http.model;

public class BaseResult<T> {
    public int code;
    public String message;
    public T data;

    public BaseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
