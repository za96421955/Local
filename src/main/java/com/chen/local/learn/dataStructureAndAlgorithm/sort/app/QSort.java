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
 * 耗时: 1490ms
 * compare: 2511595276, option: 7320116
 *
 * 100W，0-999 随机数
 * 耗时: 443ms
 * compare: 514344223, option: 8738089
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
        // 1, 数据量不大时(<1MB, 262144), 使用归并排序
        if (elements.length <= MERGE_SORT_LIMIT) {
//            System.out.println("Run Merge Sort");
            mergeSort.sort(elements);
            compareCount = mergeSort.getCompareCount();
            optionCount = mergeSort.getOptionCount();
            return;
        }
        // 2, 快速排序, 使用三数取中法
        this.quickSort(elements, 0, elements.length - 1);
    }

    public void quickSort(int[] elements, int begin, int end) {
        if (begin >= end) {
            return;
        }

        // 3, 数据量 <= 4时, 退化为插入排序
        if (end - begin + 1 <= 4) {
//            System.out.println("\nRun Insert Sort");
            int[] sorting = new int[end - begin + 1];
            System.arraycopy(elements, begin, sorting, 0, sorting.length);
//            System.out.println("begin: " + begin + ", end: " + end);
//            System.out.println(Arrays.toString(elements));

            insertSort.sort(sorting);
            compareCount += insertSort.getCompareCount();
            optionCount += insertSort.getOptionCount();
            System.arraycopy(sorting, 0, elements, begin, sorting.length);
//            System.out.println(Arrays.toString(elements));
            return;
        }

        // 2, 快速排序, 使用三数取中法
        int p = this.partition(elements, begin, end);
        this.quickSort(elements, begin, p - 1);
        this.quickSort(elements, p + 1, end);
    }

    /**
     * 获取中位数, 三数取中
     */
    private int partition(int[] elements, int begin, int end) {
        // 三数取中
        int mid = (begin + end) / 2;
        int[] pvs = {elements[begin], elements[end], elements[mid]};
        insertSort.sort(pvs);
        compareCount += insertSort.getCompareCount();
        optionCount += insertSort.getOptionCount();
        int pv = pvs[1];
        int pi = mid;
        if (pv == elements[begin]) {
            pi = begin;
        }
        if (pv == elements[end]) {
            pi = end;
        }

//        int mid = (begin + end) / 2;
//        int head = elements[begin];
//        int tail = elements[end];
//        int pv = elements[mid];
//        int pi = mid;
//        if (head < tail && tail < pv) {
//            pv = tail;
//            pi = end;
//        }
//        if (tail < head && head < pv) {
//            pv = head;
//            pi = begin;
//        }

        // 二分运算
        int p = begin;
        for (int i = begin; i <= end; ++i) {
            ++compareCount;
            if (elements[i] < pv) {
                this.swap(elements, p, i);
                if (p == pi) {
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

    public static void main(String[] args) {
        ISort sort = new QSort();

        int[] elements = {18, 5, 14, 3, 14, 15, 9, 15, 15, 10, 6, 16, 0, 18, 18, 3, 2, 7, 17, 11, 15, 2, 16, 10, 7, 6, 7, 18, 16, 0};
        System.out.println(Arrays.toString(elements));
        sort.sort(elements);
        System.out.println(Arrays.toString(elements));
        System.out.println("compare: " + sort.getCompareCount() + ", option: " + sort.getOptionCount());

        System.out.println("\n===================================");
        elements = new int[1000000];
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
        System.out.println("compare: " + sort.getCompareCount() + ", option: " + sort.getOptionCount());
    }

}


