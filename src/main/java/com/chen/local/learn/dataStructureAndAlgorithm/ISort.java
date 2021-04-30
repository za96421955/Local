package com.chen.local.learn.dataStructureAndAlgorithm;

/**
 * 排序
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public interface ISort {

    /**
     * 满序度: n * (n - 1) / 2
     * int[6] = 6 * 5 / 2 = 15
     */
    void sort(int[] elements);

    long getCompareCount();

    long getOptionCount();

}


