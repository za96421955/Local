package com.chen.local.learn.dataStructureAndAlgorithm.search.binarySearch;

import com.chen.local.learn.dataStructureAndAlgorithm.ISearch;
import com.chen.local.learn.dataStructureAndAlgorithm.ISort;
import com.chen.local.learn.dataStructureAndAlgorithm.sort.app.QSort;

import java.util.Arrays;

/**
 * 二分查找
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/6
 */
public class BinarySearch implements ISearch {

    private int searchCount;

    public BinarySearch() {
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
        // high = MAX_INT, low = MAX_INT, int overflow
//        int mid = (high + low) >> 1;
        // 避免溢出写法
        int mid = low + (high - low) >> 1;
        System.out.println("SearchCount: " + searchCount
                + ", low: " + low
                + ", high: " + high
                + ", mid: " + mid
                + ", midValue: " + elements[mid]);
        if (elements[mid] > target) {
            return this.search(elements, target, low, mid - 1);
        }
        if (elements[mid] < target) {
            return this.search(elements, target, mid + 1, high);
        }
        return mid;
    }

    @Override
    public int getSearchCount() {
        return searchCount;
    }

    public static void main(String[] args) {
        ISort sort = new QSort();
        ISearch search = new BinarySearch();

        int[] elements = {6, 5, 4, 3, 2, 1, 10, 9, 8, 7};
        System.out.println("Sort Start: ");
        sort.sort(elements);
        System.out.println(Arrays.toString(elements));

        int target = (int) (Math.random() * 11);
        System.out.println("\nSearch Start: " + target);
        int index = search.search(elements, target);
        System.out.println("index: " + index + ", searchCount: " + search.getSearchCount());

        System.out.println("\n===================================");
        elements = new int[200];
        for (int i = 0; i < elements.length; ++i) {
            elements[i] = (int) (Math.random() * 200);
        }
        System.out.println("Sort Start: ");
        sort.sort(elements);
        System.out.println(Arrays.toString(elements));

        target = (int) (Math.random() * 200);
        System.out.println("\nSearch Start: " + target);
        long begin = System.currentTimeMillis();
        index = search.search(elements, target);
        long end = System.currentTimeMillis();
        System.out.println("index: " + index + ", searchCount: " + search.getSearchCount());
        System.out.println("耗时: " + (end - begin) + "ms");
    }

}


