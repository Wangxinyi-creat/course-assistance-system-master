package com.zhedian.common.domain;

/**
 * 异常返回类
 */
public class CustomExceptionResult extends RuntimeException {

    protected long errorCode;
    protected String errorMsg;

    public CustomExceptionResult() {

    }

    public CustomExceptionResult(long errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CustomExceptionResult(String errorMsg) {
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

    public long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
