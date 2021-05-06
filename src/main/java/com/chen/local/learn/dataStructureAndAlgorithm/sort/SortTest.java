package com.chen.local.learn.dataStructureAndAlgorithm.sort;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;
import com.chen.local.learn.dataStructureAndAlgorithm.sort.app.QSort;
import com.chen.local.learn.dataStructureAndAlgorithm.sort.internal.*;

import java.util.Arrays;

/**
 * 排序性能测试
 * <p>
 * 100000, 0-199 随机数, 循环 20 次:
 * 平均耗时(冒泡): 1620ms
 * 平均耗时(插入): 2282ms
 * 平均耗时(选择): 2636ms
 * 平均耗时(归并): 6ms
 * 平均耗时(快排): 11ms
 * 平均耗时(qsort): 6ms
 *
 * 1000000, 0-199 随机数, 循环 20 次:
 * 平均耗时(归并): 68ms
 * 平均耗时(快排): 867ms
 * 平均耗时(qsort): 1010ms
 *
 * 1000000, 0-999 随机数, 循环 20 次:
 * 平均耗时(归并): 75ms
 * 平均耗时(快排): 253ms
 * 平均耗时(qsort): 209ms
 *
 * 10000000, 0-999 随机数, 循环 3 次:
 * 平均耗时(归并): 817ms
 * 平均耗时(快排): 20486ms
 * 平均耗时(qsort): 20664ms
 *
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/6
 */
public class SortTest {

    public long sort(ISort sort, int[] elements) {
        long begin = System.currentTimeMillis();
        sort.sort(elements);
        long end = System.currentTimeMillis();
//        System.out.println(Arrays.toString(elements));

        long consume = end - begin;
        System.out.println("耗时: " + consume + "ms");
        System.out.println("compare: " + sort.getCompareCount() + ", option: " + sort.getOptionCount());
        return consume;
    }

    public long bubbleSort(int[] elements) {
        System.out.println("\nBubbleSort Start: ");
        return this.sort(new BubbleSort(), Arrays.copyOf(elements, elements.length));
    }

    public long insertSort(int[] elements) {
        System.out.println("\nInsertSort Start: ");
        return this.sort(new InsertSort(), Arrays.copyOf(elements, elements.length));
    }

    public long selectSort(int[] elements) {
        System.out.println("\nSelectSort Start: ");
        return this.sort(new SelectSort(), Arrays.copyOf(elements, elements.length));
    }

    public long mergeSort(int[] elements) {
        System.out.println("\nMergeSort Start: ");
        return this.sort(new MergeSort(), Arrays.copyOf(elements, elements.length));
    }

    public long quickSort(int[] elements) {
        System.out.println("\nQuickSort Start: ");
        return this.sort(new QuickSort(), Arrays.copyOf(elements, elements.length));
    }

    public long qsort(int[] elements) {
        System.out.println("\nQSort Start: ");
        return this.sort(new QSort(), Arrays.copyOf(elements, elements.length));
    }

    public static void main(String[] args) throws Exception {
        SortTest test = new SortTest();

        int[] elements = {6, 5, 4, 3, 2, 1, 10, 9, 8, 7};
        test.insertSort(elements);

        int size = 10000000;
        int range = 1000;
        int count = 3;
        long bubbleSortConsume = 0;
        long insertSortConsume = 0;
        long selectSortConsume = 0;
        long mergeSortConsume = 0;
        long quickSortConsume = 0;
        long qsortSortConsume = 0;
        for (int j = 1; j <= count; ++j) {
            System.out.println("\n===================================");
            System.out.println("Generate Sorting Number: ");
            elements = new int[size];
            for (int i = 0; i < elements.length; ++i) {
                elements[i] = (int) (Math.random() * range);
            }
//            System.out.println(Arrays.toString(elements));

//            bubbleSortConsume += test.bubbleSort(elements);
//            insertSortConsume += test.insertSort(elements);
//            selectSortConsume += test.selectSort(elements);
            mergeSortConsume += test.mergeSort(elements);
            quickSortConsume += test.quickSort(elements);
            qsortSortConsume += test.qsort(elements);

            Thread.sleep((long) (Math.random() * 500) + 500);
        }
        System.out.println();
        System.out.println(size + ", 0-" + (range - 1) + " 随机数, 循环 " + count + " 次: ");
        System.out.println("平均耗时(冒泡): " + (bubbleSortConsume / count) + "ms");
        System.out.println("平均耗时(插入): " + (insertSortConsume / count) + "ms");
        System.out.println("平均耗时(选择): " + (selectSortConsume / count) + "ms");
        System.out.println("平均耗时(归并): " + (mergeSortConsume / count) + "ms");
        System.out.println("平均耗时(快排): " + (quickSortConsume / count) + "ms");
        System.out.println("平均耗时(qsort): " + (qsortSortConsume / count) + "ms");
    }

}
