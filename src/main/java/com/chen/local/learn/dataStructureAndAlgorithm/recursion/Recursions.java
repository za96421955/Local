package com.chen.local.learn.dataStructureAndAlgorithm.recursion;

import java.util.HashMap;
import java.util.Map;

/**
 * 递归算法
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/29
 */
public class Recursions {

    // 已计算结果的散列
    private Map<String, Integer> results;
    // 递归计算次数
    private int count;

    public Recursions() {
        this.init();
    }

    public void init() {
        this.results = new HashMap<>();
        this.count = 0;
    }

    public int getCount() {
        return count;
    }

    /**
     * 走台阶, 一次可以走1层/2层
     * N层台阶, 有几种走法
     */
    public int steps(int n) {
        // 散列缓存
        if (results.containsKey(n + "")) {
            return results.get(n + "");
        }
        count++;
        // 1层时，只有1种走法
        // 2层时，只有2种走法（1 + 1，2 + 0）
        if (n == 1 || n == 2) {
            return n;
        }
        int result = this.steps(n - 1) + this.steps(n - 2);
        results.put(n + "", result);
        return result;
    }

    /**
     * 汉诺塔
     * 次数：2^n - 1
     * f(1)：A ===> B
     * f(n)：A => C, A => B, C => B
     */
    public void hanoi(int n, String a, String b, String c) {
        count++;
        if (n == 1) {
            System.out.println(a + " ===> " + b);
            return;
        }
        this.hanoi(--n, a, c, b);
        System.out.println(a + " ===> " + b);
        this.hanoi(n, c, b, a);
    }

    public static void main(String[] args) {
        Recursions recursion = new Recursions();
        recursion.init();
        System.out.println(recursion.steps(10) + " : count=" + recursion.getCount());

        recursion.init();
        recursion.hanoi(3, "A", "B", "C");
        System.out.println("count=" + recursion.getCount());
    }

}


