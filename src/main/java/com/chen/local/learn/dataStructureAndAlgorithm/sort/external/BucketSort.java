package com.chen.local.learn.dataStructureAndAlgorithm.sort.external;

import com.chen.local.learn.dataStructureAndAlgorithm.ISort;
import com.chen.local.learn.dataStructureAndAlgorithm.sort.internal.MergeSort;
import com.chen.local.learn.dataStructureAndAlgorithm.sort.internal.QuickSort;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 桶排序（稳定, 非原地排序, 可外部排序（内存外/硬盘））
 * 空间复杂度：O(n)
 * 时间复杂度：O(n)
 *
 * 10W，0-199 随机数
 * 耗时: 22ms
 * compare: 1188668, option: 2972320
 *
 * 100W，0-199 随机数
 * 耗时: 87ms
 * compare: 15124098, option: 36378560
 *
 * 1000W，0-199 随机数
 * 耗时: 1403ms
 * compare: 183468677, option: 429028480
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class BucketSort implements ISort {

    private long compareCount;
    private long optionCount;

    private ISort internalSort;

    private void init() {
        this.compareCount = 0;
        this.optionCount = 0;
        // 效率优先
        this.internalSort = new MergeSort();
        // 空间优先
//        this.internalSort = new QuickSort();
    }

    @Override
    public void sort(int[] elements) {
        this.init();
        this.memorySort(elements);
    }

    /**
     * 内存排序, 非桶排序优势
     */
    private void memorySort(int[] elements) {
        // 分桶, n / size
        List<Integer>[] buckets = new List[20];
        for (Integer e : elements) {
            ++optionCount;
            int i = e / buckets.length;
            List<Integer> bucket = buckets[i];
            if (bucket == null) {
                buckets[i] = new ArrayList<>();
                bucket = buckets[i];
            }
            bucket.add(e);
        }
        // 桶排序
        int cursor = 0;
        for (List<Integer> bucket : buckets) {
//            System.out.println("bucket: " + bucket);
            if (CollectionUtils.isEmpty(bucket)) {
                continue;
            }
            int[] sorting = new int[bucket.size()];
            for (int j = 0; j < bucket.size(); ++j) {
                ++optionCount;
                sorting[j] = bucket.get(j);
            }
            internalSort.sort(sorting);
            compareCount += internalSort.getCompareCount();
            optionCount += internalSort.getOptionCount();
            // 复制排序结果
            optionCount += sorting.length;
            System.arraycopy(sorting, 0, elements, cursor, sorting.length);
            cursor += sorting.length;
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
        ISort sort = new BucketSort();

        int[] elements = {6, 5, 4, 3, 2, 1, 10, 9, 8, 7};
        sort.sort(elements);
        System.out.println(Arrays.toString(elements));
        System.out.println("compare: " + sort.getCompareCount() + ", option: " + sort.getOptionCount());

        System.out.println("\n===================================");
        elements = new int[1000000];
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


