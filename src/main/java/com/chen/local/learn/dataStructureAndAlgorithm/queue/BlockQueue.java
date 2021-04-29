package com.chen.local.learn.dataStructureAndAlgorithm.queue;

import com.chen.local.learn.dataStructureAndAlgorithm.IQueue;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 阻塞队列（基于循环队列）
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/28
 */
public class BlockQueue<T> implements IQueue<T> {
    private static final int DEFAULT_LENGTH = 4;

    private IQueue<T> queue;

    private AtomicInteger enqueueLock;
    private AtomicInteger dequeueLock;

    public BlockQueue() {
        this.init(DEFAULT_LENGTH);
    }

    public BlockQueue(int length) {
        this.init(length);
    }

    private void init(int length) {
        this.queue = new CasQueue<>(length);
        this.enqueueLock = new AtomicInteger(0);
        this.dequeueLock = new AtomicInteger(0);
    }

    private boolean getLock(AtomicInteger lock) {
        while (!lock.compareAndSet(0, 1));
        return true;
    }

    private void unlock(AtomicInteger lock) {
        lock.set(0);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean enqueue(T t) {
        while (this.getLock(enqueueLock)) {
            if (this.isFull()) {
                this.unlock(enqueueLock);
                this.sleep(33L);
            } else {
                break;
            }
        }
        queue.enqueue(t);
        this.unlock(enqueueLock);
        return true;
    }

    @Override
    public T dequeue() {
        while (this.getLock(dequeueLock)) {
            if (this.isEmpty()) {
                this.unlock(dequeueLock);
                this.sleep(33L);
            } else {
                break;
            }
        }
        T element = queue.dequeue();
        this.unlock(dequeueLock);
        return element;
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public void clear() {
        this.getLock(enqueueLock);
        this.getLock(dequeueLock);
        queue.clear();
        this.unlock(enqueueLock);
        this.unlock(dequeueLock);
    }

    @Override
    public boolean isFull() {
        return queue.isFull();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public String toString() {
        return queue.toString();
    }

    public static void main(String[] args) {
        IQueue<String> queue = new BlockQueue<>(100);

        // input
        for (int j = 0; j < 100; j++) {
            final int finalJ = j;
            new Thread(() -> {
                long begin = System.currentTimeMillis();
                try {
                    for (int i = 1; i <= 10000; i++) {
                        queue.enqueue(i + "");
                        System.out.println("EN <<<===️: " + i + ", " + queue.size());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long end = System.currentTimeMillis();
                System.out.println("EN线程[" + finalJ + "], 耗时: " + (end - begin) + "ms");
            }).start();
        }

        // out
        for (int j = 0; j < 100; j++) {
            final int finalJ = j;
            new Thread(() -> {
                long begin = System.currentTimeMillis();
                try {
                    for (int i = 0; i < 10000; i++) {
                        String e = queue.dequeue();
                        System.out.println("DE ===>>>: " + e + ", " + queue.size());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long end = System.currentTimeMillis();
                System.out.println("DE线程[" + finalJ + "], 耗时: " + (end - begin) + "ms");
            }).start();
        }
    }

}


