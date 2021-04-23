package com.chen.local.base.annos;

import java.lang.annotation.*;

/**
 * @description Redis自动过期缓存
 * <p>〈功能详细描述〉</p>
 *
 * @auther  陈晨(17061934)
 * @date    2019/8/19 15:03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface Cache {

    /**
     * 过期时间, 秒
     */
    int expireTime();

    /**
     * 是否自动刷新过期时间
     * 默认: false
     */
    boolean isAutoRefresh() default false;

    /**
     * 防穿透时间
     * 默认: 0, 不防穿透
     */
    int penetrateTime() default 0;

}


