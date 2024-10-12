package com.zhedian.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回结果集
 *
 * @author zhedian
 * @date 2023/6/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    private long code;
    private String message;
    private T data;


    /**
     * 成功返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> success(String message) {
        return new CommonResult<T>(200, message, null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(200, "true", data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(200, message, data);
    }


    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> failed() {
        return new CommonResult<T>(-1, "fail", null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(-1, message, null);
    }

    /**
     * 失败返回结果
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public static <T> CommonResult<T> failed(long code, String message) {
        return new CommonResult<T>(code, message, null);
    }

    /**
     * 失败返回结果
     *
     * @param resultCode 枚举API
     */
    public static <T> CommonResult<T> failed(ResultCode resultCode) {
        return new CommonResult<T>(resultCode.getCode(), resultCode.getMessage(), null);
    }

}
