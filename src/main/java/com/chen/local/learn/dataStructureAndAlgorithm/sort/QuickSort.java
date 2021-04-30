package com.chen.local.learn.dataStructureAndAlgorithm.sort;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;

import java.util.Arrays;

/**
 * 快速排序（不稳定，原地排序）
 * 空间复杂度：O(n)
 * 时间复杂度：O(nlogn)
 *
 * // 原地交换, 获取中位数
 * 10W，0-199 随机数
 * 耗时: 21ms
 * compare: 25908409, swap: 548674
 *
 * 100W，0-199 随机数
 * 耗时: 1078ms
 * compare: 2510276347, swap: 5925982
 *
 * 1000W，0-199 随机数
 * StackOverflowError
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/30
 */
public class QuickSort implements ISort {

    private long compareCount;
    private long swapCount;

    private void init() {
        this.compareCount = 0;
        this.swapCount = 0;
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
     */
    private int partition(int[] elements, int begin, int end) {
        int partitionValue = elements[end];
        int partition = begin;
        for (int i = begin; i <= end - 1; ++i) {
            ++compareCount;
            if (elements[i] < partitionValue) {
                if (partition != i) {
                    ++swapCount;
                    int temp = elements[partition];
                    elements[partition] = elements[i];
                    elements[i] = temp;
                }
                ++partition;
            }
        }
        if (partition != end) {
            ++swapCount;
            int temp = elements[partition];
            elements[partition] = elements[end];
            elements[end] = temp;
        }
        return partition;
    }

    @Override
    public long getCompareCount() {
        return compareCount;
    }

    @Override
    public long getSwapCount() {
        return swapCount;
    }

    public static void main(String[] args) {
        ISort sort = new QuickSort();

        int[] elements = {6, 5, 4, 3, 2, 1, 10, 9, 8, 7};
        System.out.println(Arrays.toString(elements));
        sort.sort(elements);
        System.out.println(Arrays.toString(elements));
        System.out.println("compare: " + sort.getCompareCount() + ", swap: " + sort.getSwapCount());

        System.out.println("\n===================================");
        elements = new int[10000000];
        for (int i = 0; i < elements.length; ++i) {
            elements[i] = (int) (Math.random() * 200);
        }
//        System.out.println(Arrays.toString(elements));

        System.out.println("Sort Start: ");
        long begin = System.currentTimeMillis();
        sort.sort(elements);
        long end = System.currentTimeMillis();
//        System.out.println(Arrays.toString(elements));

        System.out.println("耗时: " + (end - begin) + "ms");
        System.out.println("compare: " + sort.getCompareCount() + ", swap: " + sort.getSwapCount());
    }

}


