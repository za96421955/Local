package com.chen.local.learn.dataStructureAndAlgorithm;

import java.util.List;

/**
 * 索引
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/8
 */
public interface IIndex<T> {

    void insert(int index, T data);

    T get(int index);

    T remove(int index);

    List<T> range(int begin, int end);

    int size();

    void clear();

}


