package com.chen.local.learn.dataStructureAndAlgorithm.sort.internal;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;

import java.util.Arrays;

/**
 * 插入排序（稳定, 原地排序）
 * 空间复杂度：O(1)
 * 时间复杂度：O(n^2)
 *
 * 10W，0-199 随机数
 * -- 普通插入排序
 * 耗时: 1201ms
 * compare: 2491054406, option: 2491154405
 *
 * -- 哨兵插入排序（优势？）
 * 耗时: 2385ms
 * compare: 2487336779, option: 2487236776
 *
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class InsertSort implements ISort {

    private long compareCount;
    private long optionCount;

    private void init() {
        this.compareCount = 0;
        this.optionCount = 0;
    }

    @Override
    public void sort(int[] elements) {
        this.init();
//        this.normalInsertSort(elements);
        this.sentinelInsertSort(elements);
    }

    /**
     * 普通插入排序
     */
    private void normalInsertSort(int[] elements) {
        for (int i = 1; i < elements.length; ++i) {
            int curr = elements[i];
            if (elements[i] > elements[i - 1]) {
                continue;
            }
            int j = i - 1;
            for (; j >= 0 && elements[j] > curr; --j) {
                compareCount++;
                optionCount++;
                elements[j + 1] = elements[j];
            }
            optionCount++;
            elements[j + 1] = curr;
        }
    }

    /**
     * 哨兵插入排序
     */
    private void sentinelInsertSort(int[] elements) {
        int n = elements.length;
        // 选择哨兵
        int min = 0;
        for (int i = 1; i < n; ++i) {
            compareCount++;
            if (elements[i] < elements[min]) {
                min = i;
            }
        }
        this.swap(elements, min, 0);

        // 插入排序
        for (int i = 2; i < n; ++i) {
            compareCount++;
            if (elements[i] > elements[i - 1]) {
                continue;
            }
            int curr = elements[i];
            int j = i;
            for (; elements[j - 1] > curr; --j) {
                compareCount++;
                optionCount++;
                elements[j] = elements[j - 1];
            }
            optionCount++;
            elements[j] = curr;
        }
    }

    private void swap(int[] elements, int i, int j) {
        if (i == j) {
            return;
        }
        ++optionCount;
        int temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
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


