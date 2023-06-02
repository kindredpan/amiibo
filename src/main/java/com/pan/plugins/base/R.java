package com.pan.plugins.base;

/**
 * @author panqj
 * @date 2023/06/01
 */

public class R {
    private final int code;
    private String msg;
    private Object result;

    public R(int code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return this.code > 0;
    }

    public static R ok(String msg, Object result) {
        return new R(1, "msg", result);
    }

    public static R ok(String msg) {
        return new R(1, msg, null);
    }

    public static R fail(String msg) {
        return new R(0, msg, null);
    }


}
