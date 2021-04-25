package com.chen.local.learn.dataStructureAndAlgorithm;

/**
 * 栈
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/25
 */
public interface IStack<T> {

    /**
     * @description 压栈
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/25 15:02
     **/
    void push(T e);

    /**
     * @description 弹出
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/25 15:02
     **/
    T pop();

    /**
     * @description 深度
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/25 15:02
     **/
    int size();

    /**
     * @description 清除
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/25 16:32
     **/
    void clear();

}


