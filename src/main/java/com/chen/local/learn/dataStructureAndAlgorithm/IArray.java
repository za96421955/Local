package com.chen.local.learn.dataStructureAndAlgorithm;

/**
 * 数组
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/22
 */
public interface IArray<T> {

    void add(T t);

    T get(int i);

    T remove(int i);

    int size();

    void clear();

}


