package com.zhedian.common.domain;


/**
 * 常用ApiCode
 */

public enum ResultCode {
    SUCCESS(200, "操作成功"),
    UNAUTHORIZED(401, "未授权,无此操作权限"),
    FORBIDDEN(403, "暂未登录或登录已经过期"),
    VALIDATE_FAILED(404, "404"),
    FAILED(500, "操作失败"),
    INTERNAL_SERVER_ERROR(500, "服务器错误,请联系管理员");

    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
