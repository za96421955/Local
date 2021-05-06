package com.chen.local.learn.dataStructureAndAlgorithm.sort.internal;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;

import java.util.Arrays;

/**
 * 选择排序（不稳定, 原地排序）
 * 空间复杂度：O(1)
 * 时间复杂度：O(n^2)
 *
 * 10W，0-199 随机数
 * 耗时: 3981ms
 * compare: 5000049999, option: 99474
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class SelectSort implements ISort {

    private long compareCount;
    private long optionCount;

    private void init() {
        this.compareCount = 0;
        this.optionCount = 0;
    }

    @Override
    public void sort(int[] elements) {
        this.init();
        int length = elements.length;
        for (int i = 0; i < length - 1; ++i) {
            int min = i;
            for (int j = i; j < length; ++j) {
                compareCount++;
                if (elements[j] < elements[min]) {
                    min = j;
                }
            }
            if (min == i) {
                continue;
            }
            optionCount++;
            int temp = elements[i];
            elements[i] = elements[min];
            elements[min] = temp;
        }
    }

    @Override
    public long getCompareCount() {
        return compareCount;
    }

    @Override
    public long getOptionCount() {
        return optionCount;
    }

}


