package com.chen.local.learn.dataStructureAndAlgorithm.sort;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;

import java.util.Arrays;

/**
 * 选择排序
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class SelectSort implements ISort {

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
            swapCount++;
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
    public long getSwapCount() {
        return swapCount;
    }

    public static void main(String[] args) {
        ISort sort = new SelectSort();

        int[] elements = {6, 5, 4, 3, 2, 1};
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
        System.out.println("耗时: " + (end - begin) + "ms");
        System.out.println("compare: " + sort.getCompareCount() + ", swap: " + sort.getSwapCount());
    }

}


