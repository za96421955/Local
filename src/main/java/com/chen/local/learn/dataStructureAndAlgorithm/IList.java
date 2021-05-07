package com.chen.local.learn.dataStructureAndAlgorithm;

/**
 * 有序集合
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/6
 */
public interface IList<T> {

    void add(T t);

    void insert(T t, int i);

    T get(int i);

    T remove(int i);

    T remove(T t);

    boolean contains(T t);

    int size();

    void clear();

}


