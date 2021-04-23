package com.chen.local.base.utils;

import java.util.Date;

/**
 * 任务工具
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/12/28
 */
public abstract class JobUtil {

    /**
     * @description 阻塞线程, 等待任务开始
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2020/12/28 10:19
     **/
    public static void wait(Date startTime) throws Exception {
        while (true) {
            if (System.currentTimeMillis() > startTime.getTime()) {
                break;
            }
            System.out.println(DateUtil.formatDateTime(DateUtil.now()) + ": sleep");
            Thread.sleep(60 * 1000);
        }
    }

}


