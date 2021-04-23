package com.chen.local.base.dal;

import com.chen.local.base.services.ICallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @version v1.0
 * @Description: 多线程处理服务
 * <p>〈功能详细描述〉</p>
 *
 * @ClassName MultiThreadHandlerImpl
 * @author 陈晨(17061934)
 * @date 2019/11/17 12:37
 */
public class MultiThreadService {
    private static Logger logger = LoggerFactory.getLogger(MultiThreadService.class);

    private ThreadPoolTaskExecutor threadPool;

    public void setThreadPool(ThreadPoolTaskExecutor threadPool) {
        this.threadPool = threadPool;
    }

    public void run(final CountDownLatch latch, final ICallback callback) {
        if (latch == null || callback == null) {
            return;
        }
        threadPool.execute(() -> {
            try {
                callback.callback();
            } catch (Exception e) {
                logger.error("多线程执行异常, {}", e.getMessage(), e);
            }
            latch.countDown();
        });
    }

    public void wait(CountDownLatch latch, long timeout) throws TimeoutException {
        if (latch == null) {
            return;
        }
        try {
            latch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("多线程等待异常, {}", e.getMessage(), e);
        }
        // 若未执行结束, 则抛出超时异常
        if (latch.getCount() > 0) {
            throw new TimeoutException("execute timeout[" + timeout + "]");
        }
    }

}


