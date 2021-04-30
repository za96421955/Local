package com.chen.local.learn.dataStructureAndAlgorithm.sort.external;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;

import java.util.Arrays;

/**
 * 计数排序（稳定, 非原地排序, 可外部排序（内存外/硬盘））
 * （桶排序的特殊情况）
 * 空间复杂度：O(n)
 * 时间复杂度：O(n)
 *
 * 10W，0-199 随机数
 * 耗时: 7ms
 * compare: 0, option: 300199
 *
 * 100W，0-199 随机数
 * 耗时: 13ms
 * compare: 0, option: 3000199
 *
 * 1000W，0-199 随机数
 * 耗时: 64ms
 * compare: 0, option: 30000199
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class CountingSort implements ISort {

    private long compareCount;
    private long optionCount;

    private void init() {
        this.compareCount = 0;
        this.optionCount = 0;
    }

    @Override
    public void sort(int[] elements) {
        this.init();
        // 计数
        int[] counts = new int[200];
        for (Integer e : elements) {
            ++optionCount;
            counts[e] = counts[e] + 1;
        }
        // 累计 <=n 的元素的个数
        for (int i = 1; i < counts.length; ++i) {
            ++optionCount;
            counts[i] = counts[i] + counts[i - 1];
        }

        // 倒序设置结果
        int[] results = new int[elements.length];
        for (int i = elements.length - 1; i >= 0; --i) {
            ++optionCount;
            int curr = elements[i];
            int count = counts[curr] - 1;
            counts[curr] = count;
            results[count] = curr;
        }
        optionCount += elements.length;
        System.arraycopy(results, 0, elements, 0, elements.length);
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
        ISort sort = new CountingSort();

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


