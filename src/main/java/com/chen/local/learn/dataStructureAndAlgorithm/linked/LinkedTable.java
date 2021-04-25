package com.chen.local.learn.dataStructureAndAlgorithm.linked;

import com.chen.local.learn.dataStructureAndAlgorithm.ILinked;

/**
 * 链表
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/22
 */
public class LinkedTable<T> implements ILinked<T> {

    private LinkedTable<T> prev;
    private LinkedTable<T> next;

    private final boolean isSentinel;
    private LinkedTable<T> sentinel;

    private T element;

    public LinkedTable() {
        this.isSentinel = true;
    }

    private LinkedTable(T element, ILinked<T> sentinel, ILinked<T> prev, ILinked<T> next) {
        this.element = element;
        this.isSentinel = false;
        this.sentinel = (LinkedTable<T>) sentinel;
        this.prev = (LinkedTable<T>) prev;
        this.next = (LinkedTable<T>) next;
    }

    @Override
    public ILinked<T> add(T e) {
        if (isSentinel) {
            LinkedTable<T> oriNext = next;
            next = new LinkedTable<>(e, this, this, oriNext);
            if (oriNext != null) {
                oriNext.prev = next;
            }
            return this;
        }
        return sentinel.add(e);
    }

    @Override
    public T get(int i) {
        if (isSentinel) {
            if (next == null) {
                return null;
            }
            return next.get(i, 0);
        }
        return sentinel.get(i);
    }

    private T get(int i, int cursor) {
        if (i == cursor) {
            return this.lru();
        }
        if (next == null) {
            return null;
        }
        return next.get(i, ++cursor);
    }

    @Override
    public T remove(int i) {
        if (isSentinel) {
            if (next == null) {
                return null;
            }
            return next.remove(i, 0);
        }
        return sentinel.remove(i);
    }

    private T remove(int i, int cursor) {
        if (i == cursor) {
            return this.remove();
        }
        if (next == null) {
            return null;
        }
        return next.remove(i, ++cursor);
    }

    private T remove() {
        T element = this.element;
        if (prev != null) {
            prev.next = next;
        }
        if (next != null) {
            next.prev = prev;
        }
        this.element = null;
        this.sentinel = null;
        this.prev = null;
        this.next = null;
        return element;
    }

    @Override
    public ILinked<T> reverse() {
        if (isSentinel) {
            next.reverseNode(null);
            return this;
        }
        return sentinel.reverse();
    }

    private void reverseNode(LinkedTable<T> last) {
        prev = next;
        next = last;
        // 尾节点, 挂链
        if (prev == null) {
            prev = sentinel;
            sentinel.next = this;
            return;
        }
        prev.reverseNode(this);
    }

    @Override
    public boolean checkCycle() {
        if (isSentinel) {
            if (next == null) {
                return false;
            }
            next.checkCyclePrint();
            return false;
        }
        return sentinel.checkCycle();
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
    public ILinked<T> merge(ILinked<T> merged) {
        if (isSentinel) {
            LinkedTable<T> mergedFirst = (LinkedTable<T>) merged.getFirst();
            LinkedTable<T> mergedLast = (LinkedTable<T>) merged.getLast();
            LinkedTable<T> thisFirst = (LinkedTable<T>) this.getFirst();

            thisFirst.prev = mergedLast;
            mergedLast.next = thisFirst;
            mergedFirst.prev = this;
            this.next = mergedFirst;
            next.resetSentinel(this);
            return this;
        }
        return prev.merge(merged);
    }

    private void resetSentinel(LinkedTable<T> sentinel) {
        this.sentinel = sentinel;
        if (next != null) {
            next.resetSentinel(sentinel);
        }
    }

    @Override
    public T lru() {
        LinkedTable<T> first = (LinkedTable<T>) this.getFirst();
        element = this.remove();
        // 挂链
        sentinel = first.sentinel;
        prev = first.prev;
        next = first;
        // 插入
        prev.next = this;
        first.prev = this;
        return element;
    }

    @Override
    public ILinked<T> getPrev() {
        if (isSentinel) {
            return next;
        }
        return prev;
    }

    @Override
    public ILinked<T> getNext() {
        return next;
    }

    @Override
    public ILinked<T> getFirst() {
        if (isSentinel) {
            return next;
        }
        return prev.getFirst();
    }

    @Override
    public ILinked<T> getMid() {
        return null;
    }

    @Override
    public ILinked<T> getLast() {
        if (next == null) {
            return this;
        }
        return next.getLast();
    }

    @Override
    public String toString() {
        if (isSentinel) {
            if (next == null) {
                return "null";
            }
            return "[" + next.printBuilder().substring(1) + "]";
        }
        return sentinel.toString();
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
        LinkedTable<String> linked = new LinkedTable<>();
        System.out.println(linked.get(0));
        System.out.println(linked.add("aaa"));
        System.out.println(linked.add("bbb"));
        System.out.println(linked.add("ccc"));
        linked.checkCycle();

        LinkedTable<String> linked2 = new LinkedTable<>();
        System.out.println(linked2.add("ddd"));
        System.out.println(linked2.add("eee"));
        System.out.println(linked2.add("fff"));

        System.out.println(linked.merge(linked2));
        System.out.println(linked.reverse());

//        System.out.println(linked.get(1) + " : " + linked);
//        System.out.println(linked.get(1) + " : " + linked);
//        linked.checkCycle();
//
//        System.out.println(linked.remove(0) + " : " + linked);
//        System.out.println(linked.remove(0) + " : " + linked);
//        linked.checkCycle();
//
//        System.out.println(linked.get(0));
    }

}


