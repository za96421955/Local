package com.chen.local.learn.dataStructureAndAlgorithm;

/**
 * 链表
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/22
 */
public interface ILinked<T> {

    void add(T t);

    T get(int i);

    T remove(int i);

    int size();

    void clear();

    T getFirst();

    T getMid();

    T getLast();

    /**
     * @description 反转
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 17:40
     **/
    void reverse();

    /**
     * @description 环检测
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 17:40
     **/
    boolean checkCycle();

    /**
     * @description 合并
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 17:41
     * @param merged
     **/
    void merge(ILinked<T> merged);

    /**
     * @description 冷数据移除
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 17:44
     **/
    void lru(int size);

}


