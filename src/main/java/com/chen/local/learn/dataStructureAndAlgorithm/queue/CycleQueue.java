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
    private static int DEFAULT_LENGTH = 4;

    private int length;
    private Object[] array;
    private int head;
    private int tail;

    public CycleQueue() {
        this.init(DEFAULT_LENGTH + 1);
    }

    public CycleQueue(int length) {
        this.init(length + 1);
    }

    private void init(int length) {
        this.length = length;
        this.array = new Object[length];
        this.head = 0;
        this.tail = 0;
    }

    private int nextLocation(int curr) {
        if (curr >= length - 1) {
            return 0;
        }
        return curr + 1;
    }

    @Override
    public boolean enqueue(T t) {
        if (this.nextLocation(tail) == head) {
            System.out.println("queue is full");
            return false;
        }
        array[tail] = t;
        tail = this.nextLocation(tail);
        return true;
    }

    @Override
    public T dequeue() {
        if (head == tail) {
            System.out.println("queue is empty");
            return null;
        }
        T element = (T) array[head];
        head = this.nextLocation(head);
        return element;
    }

    @Override
    public int size() {
        if (head == tail) {
            return 0;
        }
        if (tail > head) {
            return tail - head;
        }
        return length - head + tail;
    }

    @Override
    public void clear() {
        this.init(length);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int curr = head;
        while (curr != tail) {
            sb.append(",").append(array[curr]);
            curr = this.nextLocation(curr);
        }
        return "[" + (sb.length() <= 0 ? "" : sb.substring(1)) + "]";
    }

    public static void main(String[] args) {
        IQueue<String> queue = new CycleQueue<>();
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
        System.out.println(queue.dequeue() + " : " + queue);
        System.out.println(queue.dequeue() + " : " + queue);
        System.out.println(queue.dequeue() + " : " + queue);
        System.out.println(queue.dequeue() + " : " + queue);
        System.out.println(queue.dequeue() + " : " + queue);
        System.out.println(queue.size());

        System.out.println("\n=========================================");
        queue.enqueue("a1a1");
        System.out.println(queue);
        queue.enqueue("b1b1");
        System.out.println(queue);
        System.out.println(queue.dequeue() + " : " + queue);
        queue.enqueue("a2a2");
        System.out.println(queue);
        queue.enqueue("b2b2");
        System.out.println(queue);
        System.out.println(queue.dequeue() + " : " + queue);
        queue.enqueue("a3a3");
        System.out.println(queue);
        queue.enqueue("b3b3");
        System.out.println(queue);
        System.out.println(queue.dequeue() + " : " + queue);

        System.out.println(queue.size());
        queue.clear();
        System.out.println(queue);
    }

}


