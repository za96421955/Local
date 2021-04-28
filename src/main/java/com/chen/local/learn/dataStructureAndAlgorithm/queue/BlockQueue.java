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
        this.queue = new CasQueue<>(length);
    }

    @Override
    public boolean enqueue(T t) {
        synchronized (enqueueLock) {
            if (queue.size() >= length) {
                System.out.println("enqueue wait <<<===️");
                try {
                    enqueueLock.wait();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        queue.enqueue(t);
        synchronized (dequeueLock) {
            dequeueLock.notify();
        }
        return true;
    }

    @Override
    public T dequeue() {
        synchronized (dequeueLock) {
            if (queue.size() <= 0) {
                System.out.println("dequeue wait ===>>>");
                try {
                    dequeueLock.wait();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
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
        int size = 1000;
        int threads = 2;
        final int step = size / threads;

        IQueue<String> queue = new BlockQueue<>(4);

        // input
        for (int j = 0; j < threads; j++) {
            final int j100 = j * step;
            new Thread(() -> {
                try {
                    for (int i = j100 + 1; i <= j100 + step; i++) {
                        try {
                            queue.enqueue(i + "");
                            System.out.println("EN <<<===️: " + i);
                        } catch (Exception e) {
                            e.printStackTrace();
                            i--;
                        }
//                    Thread.sleep((long) (Math.random() * 50));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // out
//        for (int j = 0; j < threads; j++) {
//            new Thread(() -> {
//                try {
//                    for (int i = 0; i < step; i++) {
//                        try {
//                            String e = queue.dequeue();
//                            System.out.println("DE ===>>>: " + e);
//                        } catch (Exception e) {
//                            i--;
//                        }
////                    Thread.sleep((long) (Math.random() * 20));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }

        new Thread(() -> {
            try {
                for (int i = 0; i < size; i++) {
                    try {
                        String e = queue.dequeue();
                        System.out.println("DE ===>>>: " + e);
                    } catch (Exception e) {
                        e.printStackTrace();
                        i--;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}


