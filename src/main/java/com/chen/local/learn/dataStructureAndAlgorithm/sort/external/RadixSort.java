package com.chen.local.learn.dataStructureAndAlgorithm.sort.external;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 线性排序: 基数排序（稳定, 非原地排序, 可外部排序（内存外/硬盘））
 * （基于桶排序实现）
 * 空间复杂度：O(n)
 * 时间复杂度：O(n)
 *
 * 10W，0-199 随机数
 * 耗时: 77ms
 * compare: 0, option: 1100000
 *
 * 100W，0-199 随机数
 * 耗时: 366ms
 * compare: 0, option: 11000000
 *
 * 100W，0-999999 随机数
 * 耗时: 594ms
 * compare: 0, option: 20000000
 *
 * 1000W，0-199 随机数
 * 耗时: 9311ms
 * compare: 0, option: 110000000
 *
 * 1000W，0-999999 随机数
 * 耗时: 15918ms
 * compare: 0, option: 200000000
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class RadixSort implements ISort {

    private long compareCount;
    private long optionCount;

    private void init() {
        this.compareCount = 0;
        this.optionCount = 0;
    }

    @Override
    public void sort(int[] elements) {
        this.init();
        // 位数补齐
        int max = 6;
        String[] makeUpElements = new String[elements.length];
        for (int i = 0; i < elements.length; ++i) {
            ++optionCount;
            makeUpElements[i] = StringUtils.leftPad(elements[i] + "", max, "0");
        }
        // 从后往前依次排序
        this.memoryBucketSort(makeUpElements, max - 1);
        // 复制结果
        for (int i = 0; i < elements.length; ++i) {
            ++optionCount;
            elements[i] = Integer.parseInt(makeUpElements[i]);
        }
    }

    private void memoryBucketSort(String[] elements, int bit) {
        if (bit < 0) {
            return;
        }
        // 分桶
        List<String>[] buckets = new List[10];
        for (String e : elements) {
//            System.out.println("element: " + e + ", bit: " + bit);
            ++optionCount;
            int i = Integer.parseInt(e.substring(bit, bit + 1));
            List<String> bucket = buckets[i];
            if (bucket == null) {
                buckets[i] = new ArrayList<>();
                bucket = buckets[i];
            }
            bucket.add(e);
        }
        // 桶合并
        int cursor = 0;
        for (List<String> bucket : buckets) {
            if (CollectionUtils.isEmpty(bucket)) {
                continue;
            }
            String[] sorting = new String[bucket.size()];
            for (int j = 0; j < bucket.size(); ++j) {
                ++optionCount;
                sorting[j] = bucket.get(j);
            }
            // 复制排序结果
            optionCount += sorting.length;
            System.arraycopy(sorting, 0, elements, cursor, sorting.length);
            cursor += sorting.length;
        }
        // 继续前一位排序
        this.memoryBucketSort(elements, --bit);
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
        ISort sort = new RadixSort();

        int[] elements = {6, 5, 4, 3, 2, 1, 10, 9, 8, 7};
        sort.sort(elements);
        System.out.println(Arrays.toString(elements));
        System.out.println("compare: " + sort.getCompareCount() + ", option: " + sort.getOptionCount());

        System.out.println("\n===================================");
        elements = new int[30];
        for (int i = 0; i < elements.length; ++i) {
            elements[i] = (int) (Math.random() * 30);  // 1000000
        }
        System.out.println(Arrays.toString(elements));

        System.out.println("Sort Start: ");
        long begin = System.currentTimeMillis();
        sort.sort(elements);
        long end = System.currentTimeMillis();
        System.out.println(Arrays.toString(elements));

        System.out.println("耗时: " + (end - begin) + "ms");
        System.out.println("compare: " + sort.getCompareCount() + ", option: " + sort.getOptionCount());
    }

}


