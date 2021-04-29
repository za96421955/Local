package com.chen.local.learn.dataStructureAndAlgorithm.queue;

import com.chen.local.learn.dataStructureAndAlgorithm.IQueue;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发队列（CAS循环队列）
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/28
 */
public class CasQueue<T> implements IQueue<T> {
    private static int DEFAULT_LENGTH = 4;

    private int length;
    private Object[] array;
    private AtomicInteger head;
    private AtomicInteger tail;

    public CasQueue() {
        this.init(DEFAULT_LENGTH + 1);
    }

    public CasQueue(int length) {
        this.init(length + 1);
    }

    private void init(int length) {
        this.length = length;
        this.array = new Object[length];
        this.head = new AtomicInteger(0);
        this.tail = new AtomicInteger(0);
    }

    private int nextLocation(int curr) {
        return ++curr >= length ? 0 : curr;
    }

    @Override
    public boolean enqueue(T t) {
        do {
            if (this.isFull()) {
                throw new RuntimeException("queue is full");
            }
        } while (!this.casEnqueue(t));
        return true;
    }

    private boolean casEnqueue(T t) {
        int curr = tail.get();
        int next = this.nextLocation(curr);
        if (!tail.compareAndSet(curr, next)) {
            return false;
        }
        array[curr] = t;
        return true;
    }

    @Override
    public T dequeue() {
        int curr;
        do {
            if (this.isEmpty()) {
                throw new RuntimeException("queue is empty");
            }
        } while ((curr = this.casDequeue()) == -1);
        T element = (T) array[curr];
        array[curr] = null;
        return element;
    }

    private int casDequeue() {
        int curr = head.get();
        int next = this.nextLocation(curr);
        if (!head.compareAndSet(curr, next)) {
            return -1;
        }
        return curr;
    }

    @Override
    public int size() {
        int finalLength = length;
        int finalHead = head.get();
        int finalTail = tail.get();
        if (finalTail >= finalHead) {
            return finalTail - finalHead;
        }
        return finalLength - finalHead + finalTail;
    }

    @Override
    public void clear() {
        this.init(length);
    }

    @Override
    public boolean isFull() {
        return (tail.get() + 1) % length == head.get();
    }

    @Override
    public boolean isEmpty() {
        return head.get() == tail.get();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int curr = head.get();
        while (curr != tail.get()) {
            sb.append(",").append(array[curr]);
            curr = this.nextLocation(curr);
        }
        return "[" + (sb.length() <= 0 ? "" : sb.substring(1)) + "]";
    }

    public static void main(String[] args) throws Exception {
        IQueue<String> queue = new CasQueue<>(4);
        // input
        for (int j = 0; j < 10; j++) {
            new Thread(() -> {
                for (int i = 1; i <= 100; i++) {
                    try {
                        queue.enqueue(i + "");
                        System.out.println("EN <<<===️: " + i + ", " + queue.size());
                    } catch (Exception e) {
//                        e.printStackTrace();
                        try {
                            Thread.sleep(50L);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        i--;
                    }
                }
                System.out.println(queue.size());
            }).start();
        }

//        Thread.sleep(30L);

        // out
        for (int j = 0; j < 10; j++) {
            new Thread(() -> {
                for (int i = 1; i <= 100; i++) {
                    try {
                        String result = queue.dequeue();
                        System.out.println("DE <<<===️: " + result + ", " + queue.size());
                    } catch (Exception e) {
//                        e.printStackTrace();
                        try {
                            Thread.sleep(50L);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        i--;
                    }
                }
                System.out.println(queue.size());
            }).start();
        }
    }

}


