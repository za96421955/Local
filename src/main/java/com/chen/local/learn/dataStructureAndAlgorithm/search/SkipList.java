package com.chen.local.learn.dataStructureAndAlgorithm.search;

import com.chen.local.learn.dataStructureAndAlgorithm.IList;

/**
 * 跳表（双向链表）
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/6
 */
public class SkipList<T> implements IList<T> {

    private SkipList<T> root;       // 索引根节点
    private SkipList<T> down;       // 索引下级节点
    private boolean isIndex;        // 是否为索引节点

    private SkipList<T> head;       // 头节点
    private SkipList<T> tail;       // 尾节点

    private SkipList<T> prev;       // 前节点
    private SkipList<T> next;       // 后节点

    private T element;              // 元素
    private int size;               // 链表大小

    public SkipList() {
        this.init();
    }

    private SkipList(T element, SkipList<T> prev, SkipList<T> next) {
        this.element = element;
        this.prev = prev;
        this.next = next;
    }

    private SkipList(SkipList<T> down, SkipList<T> prev, SkipList<T> next) {
        this.isIndex = true;
        this.down = down;
        this.element = down.element;
        this.prev = prev;
        this.next = next;
    }

    private void init() {
        this.root = null;
        this.down = null;
        this.isIndex = false;
        this.head = null;
        this.tail = null;
        this.prev = null;
        this.next = null;
        this.element = null;
        this.size = 0;
    }

    private void checkIndex(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("size: " + size + ", i: " + i);
        }
    }

    private SkipList<T> getNode(int i) {
        int cursor = 0;
        SkipList<T> curr = next;
        while (cursor < i && curr != null) {
            ++cursor;
            curr = curr.next;
        }
        return curr;
    }

    @Override
    public void add(T t) {
        if (head == null || tail == null) {
            size = 1;
            next = new SkipList<>(t, this, null);
            head = next;
            tail = next;
            return;
        }
        ++size;
        tail.next = new SkipList<>(t, tail, null);
        tail = tail.next;
    }

    @Override
    public void insert(T t, int i) {
        this.checkIndex(i);
        ++size;
        SkipList<T> node = this.getNode(i);
        SkipList<T> insertNode = new SkipList<>(t, node.prev, node);
        node.prev.next = insertNode;
        node.prev = insertNode;
        if (i == 0) {
            head = next;
        }
    }

    @Override
    public void insert(T t, T first) {

    }

    @Override
    public T get(int i) {
        this.checkIndex(i);
        SkipList<T> node = this.getNode(i);
        return null == node ? null : node.element;
    }

    @Override
    public int get(T t) {
        return 0;
    }

    @Override
    public T remove(int i) {
        this.checkIndex(i);
        --size;
        SkipList<T> node = this.getNode(i);
        T element = node.element;
        node.prev.next = node.next;
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        node.clear();
        if (i == 0) {
            head = next;
        }
        if (head == null) {
            this.clear();
        }
        return element;
    }

    @Override
    public int remove(T t) {
        return 0;
    }

    @Override
    public boolean contains(T t) {
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.init();
    }

    private void generateIndex(SkipList<T> node) {
        root = new SkipList<T>(node, null, null);

    }

}


