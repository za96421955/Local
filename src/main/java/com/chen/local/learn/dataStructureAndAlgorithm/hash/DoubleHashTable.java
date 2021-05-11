package com.chen.local.learn.dataStructureAndAlgorithm.hash;

import com.chen.local.learn.dataStructureAndAlgorithm.IHash;
import com.chen.local.learn.dataStructureAndAlgorithm.IHashNode;

import java.util.HashSet;
import java.util.Set;

/**
 * 散列表
 * 散列冲突解决: 双重散列
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/10
 */
public class DoubleHashTable<K, V> implements IHash<K, V> {
    private static final int DEFAULT_LENGTH = 4;
    private int length;

    private static final double LOAD_FACTOR = 0.75;
    private static final int EXPAND_MULTIPLE = 2;

    private HashNode<K, V>[] nodes;
    private int size;
    private Set<K> keySet;

    private DoubleHashTable() {
        this.length = DEFAULT_LENGTH;
        this.nodes = new HashNode[this.length];
        this.keySet = new HashSet<>();
    }

    private int hash(K k) {
        // 第一次Hash
        int location = k.hashCode()  % length;
        // 第二次Hash
        String doubleHashCode = "doubleHashCode@" + location;
        return doubleHashCode.hashCode() % length;
    }

    private void expand() {
        length *= EXPAND_MULTIPLE;
        HashNode<K, V>[] newNodes = new HashNode[this.length];
        for (HashNode<K, V> node : nodes) {
            if (node == null) {
                continue;
            }
            newNodes[this.hash(node.getKey())] = node;
        }
        nodes = newNodes;
    }

    @Override
    public void put(K k, V v) {
        int location = this.hash(k);
        if (null == nodes[location]) {
            ++size;
        }
        nodes[location] = new HashNode<>(k, v);
        keySet.add(k);
        if (size >= length * LOAD_FACTOR) {
            this.expand();
        }
    }

    @Override
    public V get(K k) {
        IHashNode<K, V> hashNode = nodes[this.hash(k)];
        return null != hashNode ? hashNode.getValue() : null;
    }

    @Override
    public V remove(K k) {
        int location = this.hash(k);
        IHashNode<K, V> hashNode = nodes[location];
        if (null == hashNode) {
            return null;
        }
        --size;
        nodes[location] = null;
        keySet.remove(k);
        return hashNode.getValue();
    }

    @Override
    public boolean contains(K k) {
        IHashNode<K, V> hashNode = nodes[this.hash(k)];
        return null != hashNode;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IHashNode<K, V> node : nodes) {
            if (null == node) {
                continue;
            }
            sb.append(",\"").append(node.getKey()).append("\":").append(node.getValue());
        }
        return "{" + sb.substring(1) + "}";
    }

    public static void main(String[] args) {
        IHash<String, Object> hashTable = new DoubleHashTable<>();
        hashTable.put("1", "aaa");
        hashTable.put("2", "bbb");
        hashTable.put("3", "ccc");
        hashTable.put("4", "ddd");
        System.out.println(hashTable);
        hashTable.remove("3");
        hashTable.remove("1");
        System.out.println(hashTable);
        hashTable.put("1", "aaa");
        hashTable.put("2", "bbb");
        hashTable.put("3", "ccc");
        hashTable.put("4", "ddd");
        hashTable.put("5", "ddd");
        hashTable.put("6", "ddd");
        System.out.println(hashTable);
        System.out.println(hashTable.keySet());
    }

}


