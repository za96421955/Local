package com.chen.local.learn.dataStructureAndAlgorithm;

import java.util.Set;

/**
 * 散列
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/10
 */
public interface IHash<K, V> {

    void put(K k, V v);

    V get(K k);

    V remove(K k);

    boolean contains(K k);

    int size();

    Set<K> keySet();

}


