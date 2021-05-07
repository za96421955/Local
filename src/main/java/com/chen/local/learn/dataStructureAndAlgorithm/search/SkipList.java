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
public class SkipList implements IList<Integer> {

    private SkipList root;       // 索引根节点
    private SkipList index;      // 索引哨兵节点
    private SkipList down;       // 索引下级节点

//    private SkipList head;       // 头节点
    private SkipList tail;       // 尾节点

    private SkipList prev;       // 前节点
    private SkipList next;       // 后节点

    private Integer element;              // 元素
    private int size;               // 链表大小

    public SkipList() {
        this.init();
    }

    private SkipList(Integer element, SkipList prev, SkipList next) {
        this.element = element;
        this.prev = prev;
        this.next = next;
    }

    private SkipList(SkipList down, SkipList prev, SkipList next) {
        this.down = down;
        this.element = down.element;
        this.prev = prev;
        this.next = next;
    }

    private void init() {
        this.root = null;
        this.down = null;
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

    private SkipList getNode(int i) {
        int cursor = 0;
        SkipList curr = next;
        while (cursor < i && curr != null) {
            ++cursor;
            curr = curr.next;
        }
        return curr;
    }

    private SkipList getNodeByValue(SkipList curr, int t) {
        if (curr == null) {
            System.out.println("curr null");
            return null;
        }
        System.out.println("curr " + curr.element + ", t " + t);
        if (curr.element == t) {
            return curr.getDataNode();
        }
        if (curr.next == null || t < curr.next.element) {
            return this.getNodeByValue(curr.down, t);
        }
        return this.getNodeByValue(curr.next, t);
    }

    private SkipList getDataNode() {
        if (down == null) {
            return this;
        }
        return down.getDataNode();
    }

    @Override
    public void add(Integer t) {
        if (tail == null) {
            next = new SkipList(t, this, null);
            tail = next;
        } else {
            tail.next = new SkipList(t, tail, null);
            tail = tail.next;
        }
        ++size;
        System.out.println("sentinel(" + this.hashCode() + ") add " + tail.element + " size " + size);
        this.generateIndexAndResetRoot(tail);
    }

    @Override
    public void insert(Integer t, int i) {
        this.checkIndex(i);
        ++size;
        SkipList node = this.getNode(i);
        SkipList insertNode = new SkipList(t, node.prev, node);
        node.prev.next = insertNode;
        node.prev = insertNode;
    }

    @Override
    public Integer get(int i) {
        this.checkIndex(i);
        SkipList node = this.getNode(i);
        return null == node ? null : node.element;
    }

    private Integer remove(SkipList removeNode) {
        if (removeNode == null) {
            return -1;
        }
        Integer element = removeNode.element;
        removeNode.prev.next = removeNode.next;
        if (removeNode.next != null) {
            removeNode.next.prev = removeNode.prev;
        }
        removeNode.clear();
        if (next == null) {
            this.clear();
        }
        --size;
        return element;
    }

    @Override
    public Integer remove(int i) {
        this.checkIndex(i);
        return this.remove(this.getNode(i));
    }

    @Override
    public Integer remove(Integer t) {
        return this.remove(this.getNodeByValue(root.down, t));
    }

    @Override
    public boolean contains(Integer t) {
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

    private SkipList getFirst() {
        if (prev == null) {
            return next;
        }
        return prev.getFirst();
    }

    /**
     * @description 生成索引
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2021/5/7 16:22
     */
    private void generateIndexAndResetRoot(SkipList node) {
        // 生成索引
        this.generateIndex(node);
        // 重置索引根节点
        this.resetRoot();
    }

    /**
     * @description 生成索引
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2021/5/7 16:22
     */
    private void generateIndex(SkipList node) {
        // 生成哨兵 + 首个索引节点
        if (index == null) {
            index = new SkipList();
            index.size = 1;
            index.next = new SkipList(node.getFirst(), index, null);
            index.tail = index.next;
            System.out.println("sentinel(" + this.hashCode() + ") generate new index(" + index.hashCode() + ") "
                    + node.getFirst().element + ", size " + index.size);
            return;
        }
        // size = 1, 3, 5, 7... 时生成索引
        if ((size - 1) % 2 != 0) {
            System.out.println("sentinel(" + this.hashCode() + ") size " + size + " reject generate index");
            return;
        }

        ++index.size;
        System.out.println("sentinel(" + this.hashCode() + ") generate index(" + index.hashCode() + ") "
                + node.element + ", size " + index.size);
        // 尾部节点, 追加索引
        if (node.next == null) {
            index.tail.next = new SkipList(node, index.tail, null);
            index.tail = index.tail.next;
            index.generateIndex(index.tail);
            return;
        }

        throw new RuntimeException("unrealized 中间插入节点");
        // 中间插入节点
//        SkipList lastLENode = index.next;
//        while (lastLENode != null && lastLENode) {
//
//        }
//        index.generateIndex(lastLENode);
    }

    /**
     * @description 重置索引根节点
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2021/5/7 16:22
     */
    private void resetRoot() {
        if (this.index == null) {
            return;
        }
        SkipList index = this.index;
        while (index.index != null) {
            index = index.index;
        }
        root = index.next;
    }

    public void print() {
        if (size <= 0) {
            System.out.println("[]");
            return;
        }

        // 索引内容
        StringBuilder index = new StringBuilder();
        SkipList sentinel = this;
        while (sentinel.index != null) {
            StringBuilder currIndex = new StringBuilder();
            SkipList curr = sentinel.index.next;
            while (curr != null) {
                currIndex.append(",").append(curr.element);
                curr = curr.next;
            }
            index.insert(0, "index : [" + currIndex.substring(1) + "]\n");
            sentinel = sentinel.index;
        }
        if (index.length() <= 0) {
            index = new StringBuilder("index : []\n");
        }

        // 链表内容
        StringBuilder linked = new StringBuilder();
        SkipList curr = this.next;
        while (curr != null) {
            linked.append(",").append(curr.element);
            curr = curr.next;
        }
        linked = new StringBuilder("linked: [" + linked.substring(1) + "]\n");

        // root down
        StringBuilder root = new StringBuilder();
        SkipList down = this.root;
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
        SkipList curr = this.next;
        while (curr != null) {
            linked.append(",").append(curr.element);
            curr = curr.next;
        }
        return "[" + linked.substring(1) + "]";
    }

    public static void main(String[] args) {
        SkipList skipList = new SkipList();
        skipList.add(1);
        skipList.print();
        skipList.add(2);
        skipList.print();
        skipList.add(3);
        skipList.print();
        skipList.add(4);
        skipList.print();
        skipList.add(5);
        skipList.print();
        skipList.add(6);
        skipList.print();
        skipList.add(7);
        skipList.print();
        skipList.add(8);
        skipList.print();
        skipList.add(9);
        skipList.print();

        System.out.println(skipList);
        System.out.println("size: " + skipList.size);

        System.out.println("\n===========================================");
        System.out.println(skipList.remove(Integer.valueOf(5)));
        skipList.print();
        System.out.println(skipList);
        System.out.println("size: " + skipList.size);
    }

}


