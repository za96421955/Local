package com.chen.local.learn.dataStructureAndAlgorithm.hash;

import com.chen.local.learn.dataStructureAndAlgorithm.IHashNode;

/**
 * 散列节点
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/10
 */
public class HashNode<K, V> implements IHashNode<K, V> {

    private K key;
    private V value;

    public HashNode(K k, V v) {
        this.key = k;
        this.value = v;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

}


