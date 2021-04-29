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
        return ++curr >= length ? 0 : curr;
    }

    @Override
    public boolean enqueue(T t) {
        if (this.isFull()) {
            throw new RuntimeException("queue is full");
        }
        array[tail] = t;
        tail = this.nextLocation(tail);
        return true;
    }

    @Override
    public T dequeue() {
        if (this.isEmpty()) {
            throw new RuntimeException("queue is empty");
        }
        T element = (T) array[head];
        array[head] = null;
        head = this.nextLocation(head);
        return element;
    }

    @Override
    public int size() {
        if (tail >= head) {
            return tail - head;
        }
        return length - head + tail;
    }

    @Override
    public void clear() {
        this.init(length);
    }

    @Override
    public boolean isFull() {
        return (tail + 1) % length == head;
    }

    @Override
    public boolean isEmpty() {
        return head == tail;
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
//        IQueue<String> queue = new CycleQueue<>();
//        queue.enqueue("aaa");
//        System.out.println(queue);
//        queue.enqueue("bbb");
//        System.out.println(queue);
//        queue.enqueue("ccc");
//        System.out.println(queue);
//        queue.enqueue("ddd");
//        System.out.println(queue);
//        System.out.println(queue.size());
//
//        System.out.println("\n=========================================");
//        System.out.println(queue.dequeue() + " : " + queue);
//        System.out.println(queue.dequeue() + " : " + queue);
//        System.out.println(queue.dequeue() + " : " + queue);
//        System.out.println(queue.dequeue() + " : " + queue);
//        System.out.println(queue.size());
//
//        System.out.println("\n=========================================");
//        queue.enqueue("a1a1");
//        System.out.println(queue);
//        queue.enqueue("b1b1");
//        System.out.println(queue);
//        System.out.println(queue.dequeue() + " : " + queue);
//        System.out.println(queue.size());
//        queue.enqueue("a2a2");
//        System.out.println(queue);
//        queue.enqueue("b2b2");
//        System.out.println(queue);
//        System.out.println(queue.dequeue() + " : " + queue);
//        System.out.println(queue.size());
//        queue.enqueue("a3a3");
//        System.out.println(queue);
//        queue.enqueue("b3b3");
//        System.out.println(queue);
//        System.out.println(queue.dequeue() + " : " + queue);
//        System.out.println(queue.size());
//
//        queue.clear();
//        System.out.println(queue);
//        System.out.println(queue.size());


        IQueue<String> queue = new CycleQueue<>(1000000);
        // input
        for (int j = 0; j < 1000; j++) {
            new Thread(() -> {
                try {
                    for (int i = 1; i <= 1000; i++) {
                        queue.enqueue(i + "");
                        System.out.println("EN <<<===️: " + i + ", " + queue.size());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(queue.size());
            }).start();
        }
    }

}


