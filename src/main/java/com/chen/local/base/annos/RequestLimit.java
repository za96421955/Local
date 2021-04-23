package com.chen.local.base.annos;

import java.lang.annotation.*;

/**
 * @description 访问限制
 * <p>〈功能详细描述〉</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RequestLimit {
    int limitCount() default 1;   //默认限制次数1次

    int time() default 60;  //默认时间60秒

    String message() default "请求过于频繁,请稍后再试";
}
