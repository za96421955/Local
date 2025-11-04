package com.chen.local.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2025/11/4
 */
@Slf4j
public class MessageExecutor {

    private int corePoolSize = 16;
    private int maxPoolSize = 64;
    private int keepAliveTime = 60;
    private int queueCapacity = 10000;
    private float alarmLimit = 0.6F;

    // 有界任务队列
    private BlockingQueue<Runnable> taskQueue;
    // 线程池
    private ThreadPoolExecutor executor;

    public MessageExecutor() {
        // 创建有界任务队列
        taskQueue = new LinkedBlockingQueue<>(queueCapacity);

        // 线程ID生成器（增加重置逻辑，避免无意义增长）
        AtomicInteger threadId = new AtomicInteger(1);

        AtomicInteger pollCount = new AtomicInteger(1);

        // 创建线程池
        executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                taskQueue,
                // 线程工厂：保持简洁，仅在ID接近上限时重置
                r -> {
                    int id = threadId.getAndIncrement();
                    // 当ID接近Integer.MAX_VALUE时重置（无需监控日志，仅默默处理）
                    if (id < 0) { // 溢出后变为负数，此时重置
                        threadId.set(2); // 下次获取会是2，当前id用1
                        id = 1;
                    }
                    Thread thread = new Thread(r, "mqtt-message-processor-" + id);
                    thread.setDaemon(true);
                    return thread;
                },
                // 拒绝策略：保持原有逻辑，确保任务不丢失
                (r, exec) -> {
                    if (!exec.isShutdown()) {
                        try {
                            if (exec.getQueue().size() >= queueCapacity) {
//                                exec.getQueue().poll();
//                                int count = pollCount.getAndIncrement();
//                                log.info("等待队列已满，poll 第一个任务, 累计丢弃任务数: {}", count);
                            }
                            exec.getQueue().put(r);
                            log.info("任务已加入等待队列，当前队列大小: {}", exec.getQueue().size());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            log.error("任务等待被中断", e);
                        }
                    }
                }
        );

        // 允许核心线程超时退出，增强复用效率
        executor.allowCoreThreadTimeOut(true);

        // 启动守护线程
        executor.submit(() -> {
            log.info("启动守护线程");
            while (!Thread.currentThread().isInterrupted()) {
//            while (true) {
                try {
                    if (taskQueue.size() > queueCapacity * alarmLimit) {
                        log.info("MQTT 请求处理缓慢，等待队列长度已超过 {}%（{}/{}）", alarmLimit * 100, taskQueue.size(), queueCapacity);
                        // 抓取所有活跃线程的堆栈
                        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
                        for (Map.Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
                            Thread thread = entry.getKey();
                            if (thread.getName().startsWith("mqtt-message-processor")) {
                                log.info("线程: {}, 状态: {}", thread.getName(), thread.getState());
                            }
                        }
                    }
                    Thread.sleep(3000L);
                } catch (Exception ignored) {}
            }
        });
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

}


