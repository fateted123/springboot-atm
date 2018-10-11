package com.dayuanit.dy9.springboot.atm.springbootatm.dto;

public class ResponseDTO {

    private int code = 1000;//1000成功处理 9999-未知异常  5000-重新登录
    private Object data;
    private String message = "";

    private ResponseDTO() {

    }

    private ResponseDTO(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private ResponseDTO(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseDTO sucess() {
        return new ResponseDTO();
    }

    public static ResponseDTO sucess(Object data) {
        return new ResponseDTO(1000, "", data);
    }

    public static ResponseDTO faild(String message) {
        return new ResponseDTO(9999, message);
    }

    public static ResponseDTO toLogin() {
        return new ResponseDTO(5000, "重新登录");
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
