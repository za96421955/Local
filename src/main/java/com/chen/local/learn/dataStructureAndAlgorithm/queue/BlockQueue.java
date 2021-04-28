package com.chen.local.learn.dataStructureAndAlgorithm.queue;

import com.chen.local.learn.dataStructureAndAlgorithm.IQueue;

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

    private int length;
    private IQueue<T> queue;

    private final Object enqueueLock = new Object();
    private final Object dequeueLock = new Object();

    public BlockQueue() {
        this.init(DEFAULT_LENGTH);
    }

    public BlockQueue(int length) {
        this.init(length);
    }

    private void init(int length) {
        this.length = length;
        this.queue = new CycleQueue<>(length);
    }

    @Override
    public boolean enqueue(T t) throws InterruptedException {
        synchronized (enqueueLock) {
            if (queue.size() >= length) {
                System.out.println("enqueue wait <<<===️");
                enqueueLock.wait();
            }
        }
        boolean result = queue.enqueue(t);
        synchronized (dequeueLock) {
            dequeueLock.notify();
        }
        return result;
    }

    @Override
    public T dequeue() throws InterruptedException {
        synchronized (dequeueLock) {
            if (queue.size() <= 0) {
                System.out.println("dequeue wait ===>>>");
                dequeueLock.wait();
            }
        }
        T element = queue.dequeue();
        synchronized (enqueueLock) {
            enqueueLock.notify();
        }
        return element;
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public void clear() {
        queue.clear();
        dequeueLock.notifyAll();
        enqueueLock.notifyAll();
    }

    @Override
    public String toString() {
        return queue.toString();
    }

    public static void main(String[] args) {
        IQueue<String> queue = new BlockQueue<>(4);
        new Thread(() -> {
            try {
                for (int i = 1; i <= 100; i++) {
                    boolean result = queue.enqueue(i + "");
                    if (result) {
                        System.out.println("EN <<<===️: " + i);
                    }
                    Thread.sleep((long) (Math.random() * 50));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                for (int i = 1; i <= 100; i++) {
                    String e = queue.dequeue();
                    System.out.println("DE ===>>>: " + e);
                    Thread.sleep((long) (Math.random() * 20));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}


