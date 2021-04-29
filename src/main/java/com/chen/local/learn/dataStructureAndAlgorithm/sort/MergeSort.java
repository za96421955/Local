package com.chen.local.learn.dataStructureAndAlgorithm.sort;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;

import java.util.Arrays;

/**
 * 归并排序（稳定，非原地排序）
 * 空间复杂度：O(n)
 * 时间复杂度：O(nlogn)
 *
 * 10W，0-199 随机数
 * // 最小排序长度 128
 * 耗时: 859ms
 * compare: 2492396854, swap: 1996931
 *
 * // 完全2分
 * 耗时: 1772ms
 * compare: 2491440395, swap: 3037859
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class MergeSort implements ISort {

    private long compareCount;
    private long swapCount;

    private ISort sort;

    private void init() {
        this.compareCount = 0;
        this.swapCount = 0;
        this.sort = new InsertSort();
    }

    @Override
    public void sort(int[] elements) {
        this.init();
        this.mergeSort(elements, 0, elements.length - 1);
    }

    private void mergeSort(int[] elements, int begin, int end) {
//        if (begin >= end) {
//            return;
//        }
        if (end - begin <= 128) {
            return;
        }
        int mid = (begin + end) / 2;
        this.mergeSort(elements, begin, mid);
        this.mergeSort(elements, mid + 1, end);
        // 排序、合并
        this.mergeSort(elements, begin, mid, end);
    }

    private void mergeSort(int[] elements, int begin, int mid, int end) {
        // 拆分排序
        int[] before = new int[mid - begin + 1];
        int[] after = new int[end - mid];
        System.arraycopy(elements, begin, before, 0, before.length);
        System.arraycopy(elements, mid + 1, after, 0, after.length);
        // 分别排序
        this.internalSort(before);
        this.internalSort(after);

        // 合并后, 再排序
        int[] merge = new int[end - begin + 1];
        System.arraycopy(before, 0, merge, 0, before.length);
        System.arraycopy(after, 0, merge, before.length, after.length);
        this.internalSort(merge);

        // 排序后内容覆盖
        System.arraycopy(merge, 0, elements, begin, merge.length);
    }

    private void internalSort(int[] elements) {
        sort.sort(elements);
        compareCount += sort.getCompareCount();
        swapCount += sort.getSwapCount();
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
        ISort sort = new MergeSort();

        int[] elements = {6, 5, 4, 3, 2, 1};
        sort.sort(elements);
        System.out.println(Arrays.toString(elements));
        System.out.println("compare: " + sort.getCompareCount() + ", swap: " + sort.getSwapCount());

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
        System.out.println("compare: " + sort.getCompareCount() + ", swap: " + sort.getSwapCount());
    }

}


