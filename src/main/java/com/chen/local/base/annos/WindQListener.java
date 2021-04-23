package com.chen.local.base.annos;

import java.lang.annotation.*;

/**
 * @description WindQ监听注册
 * <p>〈功能详细描述〉</p>
 *
 * @auther  陈晨
 * @date    2020/1/21 9:18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
public @interface WindQListener {

    /**
     * @description 主题/队列名
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/21 9:32
     */
    String name();

    /**
     * @description 订阅名
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/21 9:32
     */
    String subscriber();

    /**
     * @description 主题/队列，默认：主题
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/21 9:32
     */
    boolean isTopic() default true;

    /**
     * @description 是否注册监听，默认：是
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/21 9:32
     */
    boolean isRegister() default true;

}


