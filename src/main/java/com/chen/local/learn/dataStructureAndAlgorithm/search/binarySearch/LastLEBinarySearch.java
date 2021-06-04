package com.chen.local.learn.dataStructureAndAlgorithm.search.binarySearch;

import com.chen.local.learn.dataStructureAndAlgorithm.ISearch;
import com.chen.local.learn.dataStructureAndAlgorithm.ISort;
import com.chen.local.learn.dataStructureAndAlgorithm.sort.app.QSort;

import java.util.Arrays;

/**
 * 二分查找
 * 变形: 找到最后一个小于等于目标值的位置
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/6
 */
public class LastLEBinarySearch implements ISearch {

    private int searchCount;

    public LastLEBinarySearch() {
        this.init();
    }

    private void init() {
        this.searchCount = 0;
    }

    @Override
    public int search(int[] elements, int target) {
        this.init();
        return this.search(elements, target, 0, elements.length - 1);
    }

    private int search(int[] elements, int target, int low, int high) {
        if (high < low) {
            return -1;
        }
        searchCount++;
        int mid = (high + low) >> 1;
        System.out.println("SearchCount: " + searchCount
                + ", low: " + low
                + ", high: " + high
                + ", mid: " + mid
                + ", midValue: " + elements[mid]);
        if (elements[mid] > target) {
            return this.search(elements, target, low, mid - 1);
        }
        // 已找到小于等于目标索引, 判断右边一位是否仍大于目标, 小于等于则继续查找
        if (mid >= elements.length - 1 || elements[mid + 1] > target) {
            return mid;
        }
        System.out.println("continue: " + mid);
        return this.search(elements, target, mid + 1, high);
    }

    @Override
    public int getSearchCount() {
        return searchCount;
    }

    public static void main(String[] args) {
        ISort sort = new QSort();
        ISearch search = new LastLEBinarySearch();

        int[] elements = {6, 5, 4, 3, 2, 1, 10, 9, 8, 7};
        System.out.println("Sort Start: ");
        sort.sort(elements);
        System.out.println(Arrays.toString(elements));

        int target = (int) (Math.random() * 11);
        System.out.println("\nSearch Start: " + target);
        int index = search.search(elements, target);
        System.out.println("index: " + index + ", searchCount: " + search.getSearchCount());

        System.out.println("\n===================================");
        elements = new int[20];
        for (int i = 0; i < elements.length; ++i) {
            elements[i] = (int) (Math.random() * 5);
        }
        System.out.println("Sort Start: ");
        sort.sort(elements);
        System.out.println(Arrays.toString(elements));
        System.out.println("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19]");

        target = (int) (Math.random() * 5) + 1;
        System.out.println("\nSearch Start: " + target);
        long begin = System.currentTimeMillis();
        index = search.search(elements, target);
        long end = System.currentTimeMillis();
        System.out.println("index: " + index + ", searchCount: " + search.getSearchCount());
        System.out.println("耗时: " + (end - begin) + "ms");
    }

}


