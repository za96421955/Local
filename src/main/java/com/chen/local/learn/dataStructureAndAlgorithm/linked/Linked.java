package com.chen.local.learn.dataStructureAndAlgorithm.linked;

import com.chen.local.learn.dataStructureAndAlgorithm.Structure;

/**
 * 链表
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/22
 */
public interface Linked<T> extends Structure<T> {

    /**
     * @description 反转
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 17:40
     **/
    Linked<T> reverse();

    /**
     * @description 环检测
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 17:40
     **/
    boolean checkCycle();

    /**
     * @description 合并
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 17:41
     * @param merged
     **/
    Linked<T> merge(Linked<T> merged);

    /**
     * @description 冷数据移除
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 17:44
     **/
    T lru();

    /**
     * @description 获取前节点
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 18:01
     **/
    Linked<T> getPrev();

    /**
     * @description 获取后节点
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 18:01
     **/
    Linked<T> getNext();

    /**
     * @description 获取首节点
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 17:42
     **/
    Linked<T> getFirst();

    /**
     * @description 获取中间节点
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 17:42
     **/
    Linked<T> getMid();

    /**
     * @description 获取尾节点
     * <p>〈功能详细描述〉</p>
     *
     * @author 陈晨
     * @date 2021/4/22 17:42
     **/
    Linked<T> getLast();

}


