package com.chen.local.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 结果
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/11/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {
    private static final long serialVersionUID = 5799968667480893305L;

    private boolean success;
    private String message;
    private Object data;

    public static Result buildSuccess() {
        return new Result(true, "Success", null);
    }

    public static Result buildSuccess(Object data) {
        return new Result(true, "Success", data);
    }

    public static Result buildFail(String message) {
        return new Result(false, message, null);
    }

    public static Result buildFail(String message, Object data) {
        return new Result(false, message, data);
    }

}


