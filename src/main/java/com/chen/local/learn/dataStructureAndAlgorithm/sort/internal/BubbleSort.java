package com.chen.local.learn.dataStructureAndAlgorithm.sort.internal;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;

import java.util.Arrays;

/**
 * 冒泡排序（稳定, 原地排序）
 * 空间复杂度：O(1)
 * 时间复杂度：O(n^2)
 *
 * 10W，0-199 随机数
 * 耗时: 4042ms
 * compare: 4999825749, swap: 9943677
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class BubbleSort implements ISort {

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
        for (int i = 0; i < length - 1; i++) {
            for (int j = i + 1; j < length; j++) {
                compareCount++;
                if (elements[i] > elements[j]) {
                    optionCount++;
                    int temp = elements[i];
                    elements[i] = elements[j];
                    elements[j] = temp;
                }
            }
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

    public static void main(String[] args) {
        ISort sort = new BubbleSort();

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


