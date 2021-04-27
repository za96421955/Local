package com.chen.local.learn.dataStructureAndAlgorithm.queue;

import com.chen.local.learn.dataStructureAndAlgorithm.IQueue;

/**
 * 循环队列（数组，有界，无需迁移数据，空间复用）
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/27
 */
public class CycleQueue<T> implements IQueue<T> {
    protected static int DEFAULT_LENGTH = 4;

    private int length;
    private Object[] array;
    private int head;
    private int tail;

    public CycleQueue() {
        this.init(DEFAULT_LENGTH);
    }

    public CycleQueue(int length) {
        this.init(length);
    }

    private void init(int length) {
        this.length = length;
        this.array = new Object[length];
        this.head = 0;
        this.tail = 0;
    }

    @Override
    public void enqueue(T t) {

    }

    @Override
    public T dequeue() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {

    }
    @Override
    public String toString() {
        return super.toString();
    }

    public static void main(String[] args) {
        CycleQueue<String> queue = new CycleQueue<>();
        queue.enqueue("aaa");
        System.out.println(queue);
        queue.enqueue("bbb");
        System.out.println(queue);
        queue.enqueue("ccc");
        System.out.println(queue);
        queue.enqueue("ddd");
        System.out.println(queue);
        queue.enqueue("eee");
        System.out.println(queue);
        System.out.println(queue.size());

        System.out.println("\n=========================================");
        System.out.println(queue.dequeue());
        System.out.println(queue);
        System.out.println(queue.dequeue());
        System.out.println(queue);
        System.out.println(queue.dequeue());
        System.out.println(queue);
        System.out.println(queue.dequeue());
        System.out.println(queue);
        System.out.println(queue.dequeue());
        System.out.println(queue);

        System.out.println("\n=========================================");
        queue.enqueue("eee");
        System.out.println(queue);
        System.out.println(queue.size());
        queue.clear();
        System.out.println(queue);
    }

}


