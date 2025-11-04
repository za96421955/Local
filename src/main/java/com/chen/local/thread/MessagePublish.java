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
public class MessagePublish {

    private MessageContext context;

    public MessagePublish(MessageContext context) {
        this.context = context;
    }

    public void publish(String message) throws InterruptedException {
        log.info("发送消息: {}", message);
        synchronized (context.getResponseLock()) {
            log.info("++++++++++ 锁 ++++++++++");
//            context.getResponseLock().wait();
        }
        log.info(">>>>>> 消息: {}, 收发结束", message);
        synchronized (context.getComplete()) {
//            context.getComplete().notifyAll();
        }
    }

}


