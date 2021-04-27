package com.chen.local.learn.dataStructureAndAlgorithm;

/**
 * 队列
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/27
 */
public interface IQueue<T> {

    /**
     * @description 入队
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2021/4/27 15:35
     */
    void enqueue(T t);

    /**
     * @description 出队
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2021/4/27 15:35
     */
    T dequeue();

    int size();

    void clear();

}


