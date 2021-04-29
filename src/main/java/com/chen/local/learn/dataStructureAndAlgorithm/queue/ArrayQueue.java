package com.chen.local.learn.dataStructureAndAlgorithm.queue;

import com.chen.local.learn.dataStructureAndAlgorithm.IQueue;

/**
 * 顺序队列（数组，有界）
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/27
 */
public class ArrayQueue<T> implements IQueue<T> {
    private static int DEFAULT_LENGTH = 4;

    private int length;
    private Object[] array;
    private int head;
    private int tail;

    public ArrayQueue() {
        this.init(DEFAULT_LENGTH);
    }

    public ArrayQueue(int length) {
        this.init(length);
    }

    private void init(int length) {
        this.length = length;
        this.array = new Object[length];
        this.head = 0;
        this.tail = 0;
    }

    @Override
    public boolean enqueue(T t) {
        if (this.isFull()) {
            this.compress();
            if (tail >= length) {
                System.out.println("queue is full");
                return false;
            }
        }
        array[tail++] = t;
        return true;
    }

    @Override
    public T dequeue() {
        if (this.isEmpty()) {
            return null;
        }
        T e = (T) array[head];
        array[head++] = null;
        return e;
    }

    private void compress() {
        if (head <= 0) {
            return;
        }
        for (int i = head; i < tail; i++) {
            array[i - head] = array[i];
            array[i] = null;
        }
        tail -= head;
        head = 0;
    }

    @Override
    public int size() {
        return tail - head;
    }

    @Override
    public void clear() {
        this.init(length);
    }

    @Override
    public boolean isFull() {
        return tail >= length;
    }

    @Override
    public boolean isEmpty() {
        return this.size() <= 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = head; i < tail; i++) {
            sb.append(",").append(array[i]);
        }
        return "[" + (sb.length() <= 0 ? "" : sb.substring(1)) + "]";
    }

    public static void main(String[] args) {
        IQueue<String> queue = new ArrayQueue<>();
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


