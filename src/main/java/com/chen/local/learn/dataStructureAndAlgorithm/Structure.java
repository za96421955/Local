package com.chen.local.learn.dataStructureAndAlgorithm;

/**
 * 数据结构
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/22
 */
public interface Structure<T> {

    Structure<T> add(T e);

    T get(int i);

    T remove(int i);

}


