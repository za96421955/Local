package com.chen.local.learn.dataStructureAndAlgorithm.sort.internal;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;

import java.util.Arrays;

/**
 * 快速排序（不稳定，原地排序）
 * 空间复杂度：O(1)
 * 时间复杂度：O(nlogn)
 *
 * // 原地交换, 获取中位数
 * 10W，0-199 随机数
 * 耗时: 21ms
 * compare: 25908409, option: 548674
 *
 * 100W，0-199 随机数
 * 平均耗时: 1072ms
 * compare: 2509143818, option: 5876238
 *
 * 100W，0-999 随机数
 * 耗时: 284ms
 * compare: 512379277, option: 7084010
 *
 * 1000W，0-199 随机数
 * StackOverflowError
 *
 * 1000W，0-999 随机数
 * 耗时: 20320ms
 * compare: 50127812168, option: 74462296
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/30
 */
public class QuickSort implements ISort {

    private long compareCount;
    private long optionCount;

    private void init() {
        this.compareCount = 0;
        this.optionCount = 0;
    }

    @Override
    public void sort(int[] elements) {
        this.init();
        this.quickSort(elements, 0, elements.length - 1);
    }

    public void quickSort(int[] elements, int begin, int end) {
        if (begin >= end) {
            return;
        }
        int p = this.partition(elements, begin, end);
//        System.out.println("\nbegin: " + begin + ", end: " + end + ", p: " + p);
//        System.out.println(Arrays.toString(elements));
        this.quickSort(elements, begin, p - 1);
        this.quickSort(elements, p + 1, end);
    }

    /**
     * 获取中位数
     * 最简单的中位数获取方式, 非效率最高
     */
    private int partition(int[] elements, int begin, int end) {
        int partitionValue = elements[end];
        int partition = begin;
        compareCount += end - begin;
        for (int i = begin; i <= end - 1; ++i) {
            if (elements[i] < partitionValue) {
                this.swap(elements, partition, i);
                ++partition;
            }
        }
        this.swap(elements, partition, end);
        return partition;
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


