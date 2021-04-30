package com.chen.local.learn.dataStructureAndAlgorithm.sort;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;

import java.util.Arrays;

/**
 * 插入排序（稳定, 原地排序）
 * 空间复杂度：O(1)
 * 时间复杂度：O(n^2)
 *
 * 10W，0-199 随机数
 * 耗时: 810ms
 * compare: 2486580609, swap: 99999
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class InsertSort implements ISort {

    private long compareCount;
    private long swapCount;

    private void init() {
        this.compareCount = 0;
        this.swapCount = 0;
    }

    @Override
    public void sort(int[] elements) {
        this.init();
        int length = elements.length;
        for (int i = 1; i < length; ++i) {
            int curr = elements[i];
            int j = i - 1;
            for (; j >= 0; --j) {
                compareCount++;
                if (elements[j] > curr) {
                    elements[j + 1] = elements[j];
                } else {
                    break;
                }
            }
            swapCount++;
            elements[j + 1] = curr;
        }
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
        ISort sort = new InsertSort();

        int[] elements = {6, 5, 4, 3, 2, 1, 10, 9, 8, 7};
        sort.sort(elements);
        System.out.println(Arrays.toString(elements));
        System.out.println("compare: " + sort.getCompareCount() + ", swap: " + sort.getSwapCount());

        System.out.println("\n===================================");
        elements = new int[100000];
        for (int i = 0; i < elements.length; ++i) {
            elements[i] = (int) (Math.random() * 200);
        }
        long begin = System.currentTimeMillis();
        sort.sort(elements);
        long end = System.currentTimeMillis();
//        System.out.println(Arrays.toString(elements));
        System.out.println("耗时: " + (end - begin) + "ms");
        System.out.println("compare: " + sort.getCompareCount() + ", swap: " + sort.getSwapCount());
    }

}


