package com.chen.local.learn.dataStructureAndAlgorithm.sort.app;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;
import com.chen.local.learn.dataStructureAndAlgorithm.sort.internal.InsertSort;
import com.chen.local.learn.dataStructureAndAlgorithm.sort.internal.MergeSort;

import java.util.Arrays;

/**
 * C默认排序, qsort（不稳定，非原地排序）
 * 核心：快速排序
 * 包含：归并排序、插入排序
 * 空间复杂度：O(1)
 * 时间复杂度：O(nlogn)
 *
 * 10W，0-199 随机数
 * 耗时: 11ms
 * compare: 1534799, option: 3337856
 *
 * 100W，0-199 随机数
 * 耗时: 854ms
 * compare: 2509203617, option: 4974885
 *
 * 100W，0-999 随机数
 * 100次平均耗时: 208ms
 * compare: 512939446, option: 6816479
 *
 * 1000W，0-199 随机数
 * StackOverflowError
 *
 * 1000W，0-999 随机数
 * 耗时: 71360ms
 * compare: 50140748247, option: 84234013
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/30
 */
public class QSort implements ISort {
    private static final int MERGE_SORT_LIMIT = 262144;

    private long compareCount;
    private long optionCount;

    private ISort mergeSort;
    private ISort insertSort;

    private void init() {
        this.compareCount = 0;
        this.optionCount = 0;
        this.mergeSort = new MergeSort();
        this.insertSort = new InsertSort();
    }

    @Override
    public void sort(int[] elements) {
        this.init();
        // 1, 数据量 <= 4时, 退化为插入排序
        if (elements.length <= 4) {
            System.out.println("Run Insert Sort");
            insertSort.sort(elements);
            compareCount += insertSort.getCompareCount();
            optionCount += insertSort.getOptionCount();
            return;
        }
        // 2, 数据量不大时(<1MB, 262144), 使用归并排序
        if (elements.length <= MERGE_SORT_LIMIT) {
            System.out.println("Run Merge Sort");
            mergeSort.sort(elements);
            compareCount = mergeSort.getCompareCount();
            optionCount = mergeSort.getOptionCount();
            return;
        }
        // 3, 快速排序, 使用三数取中法
        this.quickSort(elements, 0, elements.length - 1);
    }

    public void quickSort(int[] elements, int begin, int end) {
        if (begin >= end) {
            return;
        }
        // 快速排序, 使用三数取中法
        int p = this.partition(elements, begin, end);
        this.quickSort(elements, begin, p - 1);
        this.quickSort(elements, p + 1, end);
    }

    /**
     * 获取中位数
     * 最简单的中位数获取方式, 非效率最高
     */
//    private int partition(int[] elements, int begin, int end) {
//        int partitionValue = elements[end];
//        int partition = begin;
//        for (int i = begin; i <= end - 1; ++i) {
//            ++compareCount;
//            if (elements[i] < partitionValue) {
//                this.swap(elements, partition, i);
//                ++partition;
//            }
//        }
//        this.swap(elements, partition, end);
//        return partition;
//    }

    /**
     * 获取中位数, 三数取中
     */
    private int partition(int[] elements, int begin, int end) {
        // 三数取中
        int pi = end + (begin - end) / 2;
        int pv = elements[pi];
        int head = elements[begin];
        int tail = elements[end];
        if ((head < tail && tail < pv)
            || (pv < tail && tail < head)) {
            pv = tail;
            pi = end;
        }
        else if ((tail < head && head < pv)
            || (pv < head && head < tail)) {
            pv = head;
            pi = begin;
        }

        // 二分运算, 获取中位数索引
        int p = begin;
        compareCount += end - begin + 1;
        for (int i = begin; i <= end; ++i) {
            if (elements[i] < pv) {
                this.swap(elements, p, i);
                if (p == pi) {  // 保持 pi >= p
                    pi = i;
                }
                ++p;
            }
        }
        this.swap(elements, p, pi);
        return p;
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


