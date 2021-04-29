package com.chen.local.learn.dataStructureAndAlgorithm.sort;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;

import java.util.Arrays;

/**
 * 冒泡排序
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class BubbleSort implements ISort {

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
        for (int i = 0; i < length - 1; i++) {
            boolean isSwap = false;
            for (int j = i + 1; j < length; j++) {
                compareCount++;
                if (elements[i] > elements[j]) {
                    swapCount++;
                    int temp = elements[i];
                    elements[i] = elements[j];
                    elements[j] = temp;
                    isSwap = true;
                }
            }
            // 无值交换, 已满序, 提前结束
            if (!isSwap) {
                break;
            }
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
        ISort sort = new BubbleSort();

        int[] elements = {4, 3, 2, 1, 5, 6};
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


