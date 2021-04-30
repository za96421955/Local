package com.chen.local.learn.dataStructureAndAlgorithm.sort.internal;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;

import java.util.Arrays;

/**
 * 归并排序（稳定，非原地排序）
 * 空间复杂度：O(n)
 * 时间复杂度：O(nlogn)
 *
 * // 二分归并
 * 10W，0-199 随机数
 * 耗时: 10ms
 * compare: 1534807, option: 1734805
 *
 * 100W，0-199 随机数
 * 耗时: 84ms
 * compare: 18649507, option: 20649505
 *
 * 1000W，0-199 随机数
 * 耗时: 754ms
 * compare: 219780707, option: 239780705
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class MergeSort implements ISort {

    private long compareCount;
    private long optionCount;

    private void init() {
        this.compareCount = 0;
        this.optionCount = 0;
    }

    @Override
    public void sort(int[] elements) {
        this.init();
        this.mergeSort(elements, 0, elements.length - 1);
    }

    private void mergeSort(int[] elements, int begin, int end) {
        if (begin >= end) {
            return;
        }
        int mid = (begin + end) / 2;
        this.mergeSort(elements, begin, mid);
        this.mergeSort(elements, mid + 1, end);
        // 合并有序数组
        this.merge(elements, begin, mid, end);
    }

    private void merge(int[] elements, int begin, int mid, int end) {
//        System.out.println("\nbegin: " + begin + ", mid: " + mid + ", end: " + end);
//        System.out.println(Arrays.toString(elements));

        // begin - mid, mid + 1 - end, 分别为有序内容
        // 新建临时数组, 依次填入begin - mid, mid + 1 - end内容
        int[] temps = new int[end - begin + 1];
        int before = begin;
        int after = mid + 1;
        int curr = 0;
        while (before <= mid && after <= end) {
            ++compareCount;
            ++optionCount;
            if (elements[before] <= elements[after]) {
                temps[curr++] = elements[before++];
            } else {
                temps[curr++] = elements[after++];
            }
        }

        // 拷贝剩余数组内容
        if (before <= mid) {
            optionCount += mid - before + 1;
            System.arraycopy(elements, before, temps, curr, mid - before + 1);
        } else {
            optionCount += end - after + 1;
            System.arraycopy(elements, after, temps, curr, end - after + 1);
        }

        // 临时数组拷贝回原数组
        optionCount += end - begin + 1;
        System.arraycopy(temps, 0, elements, begin, end - begin + 1);
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
        ISort sort = new MergeSort();

        int[] elements = {6, 5, 4, 3, 2, 1, 10, 9, 8, 7};
        sort.sort(elements);
        System.out.println(Arrays.toString(elements));
        System.out.println("compare: " + sort.getCompareCount() + ", option: " + sort.getOptionCount());

        System.out.println("\n===================================");
        elements = new int[100000];
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


