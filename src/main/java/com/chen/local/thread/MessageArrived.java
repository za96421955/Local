package com.chen.local.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2025/11/4
 */
@Slf4j
public class MessageArrived {

    private MessageContext context;
    private MessagePublish publish;

    public MessageArrived(MessageContext context, MessagePublish publish) {
        this.context = context;
        this.publish = publish;
    }

    public void arrived(String message) throws InterruptedException {
        synchronized (context.getResponseLock()) {
            log.info("----------- 解 -----------");
            context.getResponseLock().notifyAll();
        }
        context.getExecutor().getExecutor().submit(() -> {
            log.info("接收消息: {}", message);
            try {
                // 模拟处理耗时, 随机休眠 5 - 40 毫秒
                int rnd = (int) (Math.random() * 36) + 5;
                Thread.sleep(rnd);

                publish.publish(message);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

}


