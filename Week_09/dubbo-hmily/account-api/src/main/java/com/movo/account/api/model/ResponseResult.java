package com.movo.account.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 响应信息
 * @author Movo
 * @create 2021/4/23 12:02
 */
@Getter
@Setter
@ToString
public class ResponseResult implements Serializable {
    private int statusCode;
    private String msg;
    private Object data;

    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 500;
    private static final String SUCCESS_MSG = "ok";

    public static ResponseResult ok() {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setStatusCode(ResponseResult.SUCCESS_CODE);
        responseResult.setMsg(ResponseResult.SUCCESS_MSG);
        return responseResult;
    }

    public static ResponseResult ok(Object data) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setStatusCode(ResponseResult.SUCCESS_CODE);
        responseResult.setMsg(ResponseResult.SUCCESS_MSG);
        responseResult.setData(data);
        return responseResult;
    }

    public static ResponseResult fail(String msg) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setStatusCode(ResponseResult.ERROR_CODE);
        responseResult.setMsg(msg);
        return responseResult;
    }
}
