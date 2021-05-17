package com.chen.local.learn.dataStructureAndAlgorithm.hash;

import com.chen.local.learn.dataStructureAndAlgorithm.IHash;
import com.chen.local.learn.dataStructureAndAlgorithm.IHashNode;

import java.util.HashSet;
import java.util.Set;

/**
 * 散列表
 * 散列冲突解决: 链表
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/10
 */
public class LinkedHashTable<K, V> implements IHash<K, V> {
    private static final int DEFAULT_LENGTH = 4;
    private int length;

    private static final double LOAD_FACTOR = 0.75;
    private static final int EXPAND_MULTIPLE = 2;

    private LinkedHashNode<K, V>[] nodes;
    private int size;
    private Set<K> keySet;

    private LinkedHashTable() {
        this.length = DEFAULT_LENGTH;
        this.nodes = new LinkedHashNode[this.length];
        this.keySet = new HashSet<>();
    }

    private int hash(K k) {
        // 第一次Hash
        int location = k.hashCode()  % length;
        // 第二次Hash
        String doubleHashCode = "doubleHashCode@" + location;
        return doubleHashCode.hashCode() % length;
    }

    private void put(LinkedHashNode<K, V>[] nodes, int location, K k, V v) {
        if (null == nodes[location]) {
            nodes[location] = new LinkedHashNode<>();
        }
        nodes[location].add(k, v);
    }

    private void expand() {
        length *= EXPAND_MULTIPLE;
        LinkedHashNode<K, V>[] newNodes = new LinkedHashNode[this.length];
        for (LinkedHashNode<K, V> node : nodes) {
            if (node == null) {
                continue;
            }
            LinkedHashNode<K, V> curr = node.getNext();
            while (null != curr) {
                this.put(newNodes, this.hash(curr.getKey()), curr.getKey(), curr.getValue());
                curr = curr.getNext();
            }
        }
        nodes = newNodes;
    }

    private LinkedHashNode<K, V> getNode(K k) {
        LinkedHashNode<K, V> node = nodes[this.hash(k)];
        return null != node ? node.get(k) : null;
    }

    @Override
    public void put(K k, V v) {
        LinkedHashNode<K, V> node = this.getNode(k);
        if (null != node) {
            node.setValue(v);
            return;
        }
        ++size;
        keySet.add(k);
        this.put(nodes, this.hash(k), k, v);

        if (size >= length * LOAD_FACTOR) {
            this.expand();
        }
    }

    @Override
    public V get(K k) {
        LinkedHashNode<K, V> node = this.getNode(k);
        return null != node ? node.getValue() : null;
    }

    @Override
    public V remove(K k) {
        LinkedHashNode<K, V> node = this.getNode(k);
        if (null == node) {
            return null;
        }
        --size;
        keySet.remove(k);
        return node.remove(k).getValue();
    }

    @Override
    public boolean contains(K k) {
        return null != this.get(k);
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
        for (LinkedHashNode<K, V> node : nodes) {
            if (null == node) {
                continue;
            }
            LinkedHashNode<K, V> curr = node.getNext();
            while (null != curr) {
                sb.append(",\"").append(curr.getKey()).append("\":").append(curr.getValue());
                curr = curr.getNext();
            }
        }
        return "{" + sb.substring(1) + "}";
    }

    public static void main(String[] args) {
        IHash<String, Object> hashTable = new LinkedHashTable<>();
        hashTable.put("1", "aaa");
        hashTable.put("2", "bbb");
        System.out.println(hashTable);
        hashTable.put("3", "ccc");
        hashTable.put("4", "ddd");
        System.out.println(hashTable);
        hashTable.remove("3");
        hashTable.remove("1");
        System.out.println(hashTable);

        System.out.println("\n============================");
        hashTable.put("1", "aaa");
        hashTable.put("2", "bbb");
        hashTable.put("3", "ccc");
        hashTable.put("4", "ddd");
        System.out.println(hashTable);
        hashTable.put("5", "eee");
        hashTable.put("6", "fff");
        System.out.println(hashTable);
        System.out.println(hashTable.keySet());
    }

}


