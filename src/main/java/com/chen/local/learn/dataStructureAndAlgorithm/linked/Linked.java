package com.chen.local.learn.dataStructureAndAlgorithm.linked;

import com.chen.local.learn.dataStructureAndAlgorithm.ILinked;

/**
 * 双向链表
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/22
 */
public class Linked<T> implements ILinked<T> {
    private final boolean isSentinel;

    private Linked<T> prev;
    private Linked<T> next;

    private T element;
    private int length;

    public Linked() {
        this.isSentinel = true;
        this.length = 0;
    }

    private Linked(T element, ILinked<T> prev, ILinked<T> next) {
        this.isSentinel = false;
        this.element = element;
        this.prev = (Linked<T>) prev;
        this.next = (Linked<T>) next;
    }

    @Override
    public void add(T e) {
        Linked<T> lastNext = next;
        next = new Linked<>(e, this, lastNext);
        if (lastNext != null) {
            lastNext.prev = next;
        }
        length++;
    }

    @Override
    public T get(int i) {
        if (next == null) {
            return null;
        }
        Linked<T> node = next.getNode(i, 0);
        return node == null ? null : node.element;
    }

    private Linked<T> getNode(int i, int cursor) {
        if (i == cursor) {
            return this;
        }
        if (next == null) {
            return null;
        }
        return next.getNode(i, ++cursor);
    }

    @Override
    public T remove(int i) {
        if (next == null) {
            return null;
        }
        Linked<T> removed = next.getNode(i, 0);
        if (removed == null) {
            return null;
        }
        length--;
        return this.removeNode(removed);
    }

    private T removeNode(Linked<T> removed) {
        T element = removed.element;
        if (removed.prev != null) {
            removed.prev.next = removed.next;
        }
        if (removed.next != null) {
            removed.next.prev = removed.prev;
        }
        removed.element = null;
        removed.prev = null;
        removed.next = null;
        return element;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public void clear() {
        this.lru(0);
    }

    @Override
    public T getFirst() {
        return this.getFirstNode().element;
    }

    private Linked<T> getFirstNode() {
        return next;
    }

    @Override
    public T getMid() {
        return this.getMidNode().element;
    }

    public Linked<T> getMidNode() {
        return this.getNode(length / 2, 0);
    }

    @Override
    public T getLast() {
        return this.getLastNode().element;
    }

    public Linked<T> getLastNode() {
        if (next == null) {
            if (isSentinel) {
                return null;
            }
            return this;
        }
        return next.getLastNode();
    }

    @Override
    public void reverse() {
        next = next.reverseNode(null);
        next.prev = this;
    }

    private Linked<T> reverseNode(Linked<T> last) {
        prev = next;
        next = last;
        // 尾节点, 挂链
        if (prev == null) {
            return this;
        }
        return prev.reverseNode(this);
    }

    @Override
    public boolean checkCycle() {
        if (next == null) {
            return false;
        }
        next.checkCyclePrint();
        return false;
    }

    private void checkCyclePrint() {
        StringBuilder sb = new StringBuilder();
        sb.append("this: ").append(element);
        sb.append(", ").append("prev: ").append(prev != null ? prev.element : null);
        sb.append(", ").append("next: ").append(next != null ? next.element : null);
        System.out.println(sb.toString());
        if (next != null) {
            next.checkCyclePrint();
        }
    }

    @Override
    public void merge(ILinked<T> merged) {
        Linked<T> convertMerged = (Linked<T>) merged;
        Linked<T> mergedFirst = convertMerged.getFirstNode();
        Linked<T> mergedLast = convertMerged.getLastNode();
        Linked<T> thisFirst = this.getFirstNode();

        thisFirst.prev = mergedLast;
        mergedLast.next = thisFirst;
        mergedFirst.prev = this;
        this.next = mergedFirst;
        length += convertMerged.length;
    }

    @Override
    public void lru(int size) {
        while (length > size) {
            this.remove(size);
        }
    }

    @Override
    public String toString() {
        if (next == null) {
            return "null";
        }
        return "[" + next.printBuilder().substring(1) + "]";
    }

    private String printBuilder() {
        StringBuilder sb = new StringBuilder();
        sb.append(",").append(element == null ? "null" : element.toString());
        if (next == null) {
            return sb.toString();
        }
        return sb.append(next.printBuilder()).toString();
    }

    public static void main(String[] args) {
        ILinked<String> linked = new Linked<>();
        System.out.println(linked.get(0));
        linked.add("aaa");
        System.out.println(linked);
        linked.add("bbb");
        System.out.println(linked);
        linked.add("ccc");
        System.out.println(linked);
        linked.checkCycle();

        System.out.println("\n=========================================");
        Linked<String> linked2 = new Linked<>();
        linked2.add("ddd");
        System.out.println(linked2);
        linked2.add("eee");
        System.out.println(linked2);
        linked2.add("fff");
        System.out.println(linked2);

        linked.merge(linked2);
        System.out.println(linked);
        linked.reverse();
        System.out.println(linked);
        linked.lru(3);
        System.out.println(linked);
        System.out.println(linked.size());
        linked.clear();
        System.out.println(linked);

        System.out.println("\n=========================================");
        linked = new Linked<>();
        linked.add("aaa");
        System.out.println(linked);
        linked.add("bbb");
        System.out.println(linked);
        System.out.println(linked.get(1) + " : " + linked);
        System.out.println(linked.get(1) + " : " + linked);
        linked.checkCycle();

        System.out.println(linked.remove(0) + " : " + linked);
        System.out.println(linked.remove(0) + " : " + linked);
        linked.checkCycle();

        System.out.println(linked.get(0));
    }

}


