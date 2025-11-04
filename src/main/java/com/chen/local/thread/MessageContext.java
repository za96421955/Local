package com.chen.local.thread;

import lombok.Getter;

/**
 * TODO
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2025/11/4
 */
@Getter
public class MessageContext {

    private Object complete = new Object();
    private Object responseLock = new Object();

    private MessageExecutor executor;

    public MessageContext() {
        this.executor = new MessageExecutor();
    }

    public static void main(String[] args) throws InterruptedException {
        MessageContext context = new MessageContext();
        MessagePublish publish = new MessagePublish(context);
        MessageArrived arrived = new MessageArrived(context, publish);

        for (int i = 1; i <= 100000; i++) {
            arrived.arrived("message - " + i);
        }

//        synchronized (context.getComplete()) {
//            context.getComplete().wait();
//        }

        Thread.sleep(10000L);
    }

}


