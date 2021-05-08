package com.chen.local.learn.dataStructureAndAlgorithm.search;

import com.chen.local.learn.dataStructureAndAlgorithm.IIndex;

import java.util.List;

/**
 * 跳表（双向链表）
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/6
 */
public class SkipList<T> implements IIndex<T> {

    private SkipList<T> root;       // 索引根节点
    private SkipList<T> top;        // 索引上级节点
    private SkipList<T> down;       // 索引下级节点

    private SkipList<T> sentinel;   // 哨兵节点
    private SkipList<T> tail;       // 尾节点
    private SkipList<T> prev;       // 前节点
    private SkipList<T> next;       // 后节点

    private int index;              // 索引
    private T data;                 // 数据
    private int size;               // 链表大小

    public SkipList() {
        this.init();
    }

    private SkipList(int index, T data, SkipList<T> prev, SkipList<T> next, SkipList<T> sentinel) {
        this.index = index;
        this.data = data;
        this.prev = prev;
        this.next = next;
        this.sentinel = sentinel;
    }

    private SkipList(SkipList<T> down, SkipList<T> prev, SkipList<T> next, SkipList<T> sentinel) {
        this.down = down;
        this.index = down.index;
        this.prev = prev;
        this.next = next;
        this.sentinel = sentinel;
    }

    private void init() {
        this.root = null;
        this.top = null;
        this.down = null;

        this.sentinel = null;
        this.tail = null;
        this.prev = null;
        this.next = null;

        this.index = -1;
        this.data = null;
        this.size = 0;
    }

    private void checkIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index " + index + " non-positive integer");
        }
    }

    private SkipList<T> search(SkipList<T> curr, int index, boolean getDataNode) {
        if (curr == null) {
//            System.out.println("curr null");
            return null;
        }
//        System.out.println("curr " + curr.index + ", index " + index);
        if (curr.index == index) {
            return getDataNode ? curr.getDataNode() : curr;
        }
        if (curr.next == null || index < curr.next.index) {
            return this.search(curr.down, index, getDataNode);
        }
        return this.search(curr.next, index, getDataNode);
    }

    private SkipList<T> getDataNode() {
        if (null == down) {
            return this;
        }
        return down.getDataNode();
    }

    @Override
    public void insert(int index, T data) {
        this.generateIndexAndResetRoot(this.insertByData(index, data));
        System.out.println("sentinel(" + this.hashCode() + ") add " + data + " size " + size);
    }

    private SkipList<T> getInsertPrevNode(int index) {
        this.checkIndex(index);
        // 从后往前, 找到索引插入位置
        SkipList<T> prev = tail == null ? this : tail;
        while (prev.index > index) {
            prev = prev.prev;
        }
        return prev;
    }

    private SkipList<T> insertNode(SkipList<T> prev, SkipList<T> insert) {
        if (prev.next != null) {
            prev.next.prev = insert;
        } else {
            // 插入尾节点
            tail = insert;
        }
        prev.next = insert;
        size++;
        return insert;
    }

    private SkipList<T> insertByData(int index, T data) {
        SkipList<T> prev = this.getInsertPrevNode(index);
        return this.insertNode(prev, new SkipList<>(index, data, prev, prev.next, this));
    }

    private SkipList<T> insertByIndex(SkipList<T> node) {
        SkipList<T> prev = this.getInsertPrevNode(node.index);
        return this.insertNode(prev, new SkipList<>(node, prev, prev.next, this));
    }

    @Override
    public T get(int index) {
        this.checkIndex(index);
        SkipList<T> node = this.search(root, index, true);
        return null == node ? null : node.data;
    }

    @Override
    public T remove(int index) {
        this.checkIndex(index);
        SkipList<T> removeNode = this.search(root, index, false);
        if (null == removeNode) {
            return null;
        }
        return removeNode.remove();
    }

    private T remove() {
        // 移除当前节点
        prev.next = next;
        if (null != next) {
            next.prev = prev;
        }
        --sentinel.size;

        // 获取返回数据, 清理当前节点
        T data = this.data;
        if (null != down) {
            data = down.remove();
        }
        this.clear();
        return data;
    }

    @Override
    public List<T> range(int begin, int end) {
        this.checkIndex(begin);
        this.checkIndex(end);
        // TODO
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.init();
    }

    /**
     * @description 生成索引, 并重置索引根节点
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2021/5/7 16:22
     */
    private void generateIndexAndResetRoot(SkipList<T> node) {
        // 生成索引
        boolean resetRoot = this.generateIndex(node);
        // 重置索引根节点
        if (resetRoot) {
            this.resetRoot();
        }
    }

    /**
     * @description 生成索引
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2021/5/7 16:22
     */
    private boolean generateIndex(SkipList<T> node) {
        // 生成上级索引 + 首个索引节点
        if (null == top) {
            top = new SkipList<>();
            top.insertByIndex(node.sentinel.next);
            System.out.println("sentinel(" + this.hashCode() + ") generate new index(" + top.hashCode() + ") "
                    + node.sentinel.next.index + ", size " + top.size);
            return true;
        }
        // size = 1, 3, 5, 7... 时生成索引
        if ((size - 1) % 2 != 0) {
            System.out.println("sentinel(" + this.hashCode() + ") size " + size + " reject generate index");
            return false;
        }
        // 插入索引
        SkipList<T> insert = top.insertByIndex(node);
        System.out.println("sentinel(" + this.hashCode() + ") generate index(" + top.hashCode() + ") "
                + node.index + ", size " + top.size);
        return top.generateIndex(insert);
    }

    /**
     * @description 重置索引根节点
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2021/5/7 16:22
     */
    private void resetRoot() {
        if (null == this.top) {
            return;
        }
        SkipList<T> top = this.top;
        while (null != top.top) {
            top = top.top;
        }
        root = top.next;
    }

    public void print() {
        if (size <= 0) {
            System.out.println("[]");
            return;
        }

        // 索引内容
        StringBuilder index = new StringBuilder();
        SkipList<T> sentinel = this;
        while (sentinel.top != null) {
            StringBuilder currIndex = new StringBuilder();
            SkipList<T> curr = sentinel.top.next;
            while (curr != null) {
                currIndex.append(",").append(curr.index);
                curr = curr.next;
            }
            if (currIndex.length() > 0) {
                index.insert(0, "index : [" + currIndex.substring(1) + "]\n");
            }
            sentinel = sentinel.top;
        }
        if (index.length() <= 0) {
            index = new StringBuilder("index : []\n");
        }

        // 链表内容
        StringBuilder linked = new StringBuilder();
        SkipList<T> curr = this.next;
        while (curr != null) {
            linked.append(",(").append(curr.index).append(")").append(curr.data);
            curr = curr.next;
        }
        linked = new StringBuilder("linked: [" + linked.substring(1) + "]\n");

        // root down
        StringBuilder root = new StringBuilder();
        SkipList<T> down = this.root;
        while (down != null) {
            root.append(",").append(down.hashCode());
            down = down.down;
        }
        root = new StringBuilder("root  : [" + root.substring(1) + "]\n");

        System.out.println(index.toString() + linked.toString() + root.toString());
    }

    @Override
    public String toString() {
        if (size <= 0) {
            return "[]";
        }
        StringBuilder linked = new StringBuilder();
        SkipList<T> curr = this.next;
        while (null != curr) {
            linked.append(",").append(curr.data);
            curr = curr.next;
        }
        return "[" + linked.substring(1) + "]";
    }

    public static void main(String[] args) {
        SkipList<String> skipList = new SkipList<>();
        skipList.insert(1, "aaa");
        skipList.insert(6, "fff");
        skipList.insert(5, "eee");
        skipList.insert(7, "ggg");
        skipList.insert(8, "hhh");
        skipList.insert(3, "ccc");
        skipList.insert(4, "ddd");
        skipList.insert(2, "bbb");
        skipList.insert(9, "iii");
        skipList.print();

        System.out.println(skipList);
        System.out.println("size: " + skipList.size);

        System.out.println(skipList.get(0));
        System.out.println(skipList.get(1));
        System.out.println(skipList.get(5));
        System.out.println(skipList.get(6));

        System.out.println("\n===========================================");
        System.out.println(skipList.remove(5));
        System.out.println(skipList.remove(7));
        System.out.println(skipList.remove(8));
        System.out.println(skipList.remove(9));
        skipList.print();
        System.out.println(skipList.remove(1));
        System.out.println(skipList.remove(2));
        System.out.println(skipList.remove(3));
        System.out.println(skipList.remove(4));
        skipList.print();

        System.out.println("\n===========================================");
        System.out.println(skipList);
        System.out.println("size: " + skipList.size);
        System.out.println(skipList.get(0));
        System.out.println(skipList.get(1));
        System.out.println(skipList.get(5));
        System.out.println(skipList.get(6));
    }

}


