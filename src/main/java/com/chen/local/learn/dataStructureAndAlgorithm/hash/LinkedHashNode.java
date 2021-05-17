package com.chen.local.learn.dataStructureAndAlgorithm.hash;

import com.chen.local.learn.dataStructureAndAlgorithm.IHashNode;

/**
 * 链表散列节点
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/10
 */
public class LinkedHashNode<K, V> implements IHashNode<K, V> {

    private LinkedHashNode<K, V> tail;
    private LinkedHashNode<K, V> prev;
    private LinkedHashNode<K, V> next;

    private K key;
    private V value;

    public LinkedHashNode() {}

    private LinkedHashNode(K k, V v, LinkedHashNode<K, V> prev, LinkedHashNode<K, V> next) {
        this.key = k;
        this.value = v;
        this.prev = prev;
        this.next = next;
    }

    public void add(K k, V v) {
        LinkedHashNode<K, V> prev = null == tail ? this : tail;
        prev.next = new LinkedHashNode<>(k, v, prev, null);
        tail = prev.next;
    }

    public LinkedHashNode<K, V> get(K k) {
        if (k == null) {
            return null;
        }
        if (k.equals(key)) {
            return this;
        }
        return null == next ? null : next.get(k);
    }

    public LinkedHashNode<K, V> remove(K k) {
        LinkedHashNode<K, V> removed = this.get(k);
        if (null == removed) {
            return null;
        }
        removed.prev.next = removed.next;
        if (null != removed.next) {
            removed.next.prev = removed.prev;
        } else {
            if (removed.prev.equals(this)) {
                tail = null;
            } else {
                tail = removed.prev;
            }
        }
        return removed;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    public void setValue(V v) {
        this.value = v;
    }

    public LinkedHashNode<K, V> getNext() {
        return next;
    }

}


