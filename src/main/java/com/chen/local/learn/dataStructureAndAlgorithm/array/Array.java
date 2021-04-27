package com.chen.local.learn.dataStructureAndAlgorithm.array;

import com.chen.local.learn.dataStructureAndAlgorithm.IArray;

import java.util.Arrays;

/**
 * 全量复制动态数组
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/22
 */
public class Array<T> implements IArray<T> {
    protected static int DEFAULT_LENGTH = 4;
    protected static int EXPAND_MULTIPLE = 2;
    protected static double EXPAND_FACTOR = 0.75;
    protected static double REDUCE_FACTOR = 0.1;

    protected int length;
    protected Object[] array;
    protected int cursor;

    public Array() {
        this.init(DEFAULT_LENGTH);
    }

    public Array(int length) {
        this.init(length);
    }

    protected void init(int length) {
        this.length = length;
        this.array = new Object[this.length];
        this.cursor = 0;
    }

    protected boolean checkExpand() {
        return cursor > array.length * EXPAND_FACTOR;
    }

    // 全量扩展
    protected void expand() {
        if (!this.checkExpand()) {
            return;
        }
        length *= EXPAND_MULTIPLE;
        Object[] newArray = new Object[length];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    protected boolean checkReduce() {
        return cursor <= array.length * REDUCE_FACTOR;
    }

    // 全量缩容
    protected void reduce() {
        if (!this.checkReduce()) {
            return;
        }
        length /= EXPAND_MULTIPLE;
        Object[] newArray = new Object[length];
        System.arraycopy(array, 0, newArray, 0, newArray.length);
        array = newArray;
    }

    @Override
    public void add(T e) {
        array[cursor++] = e;
        this.expand();
    }

    public T get(int i) {
        return (T) array[i];
    }

    public T remove(int i) {
        if (i >= cursor) {
            return null;
        }
        T element = this.get(i);
        this.compress(i);
        cursor--;
        this.reduce();
        return element;
    }

    // 左移压缩
    protected void compress(int index) {
        if (cursor - index < 0) {
            return;
        }
        System.arraycopy(array, index + 1, array, index, cursor - index);
    }

    @Override
    public int size() {
        return cursor;
    }

    @Override
    public void clear() {
        this.init(length);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    public static void main(String[] args) {
        Array<Integer> array = new Array<>(8);
        array.add(1);
        System.out.println(array);
        array.add(2);
        System.out.println(array);
        array.add(3);
        System.out.println(array);
        array.add(4);
        System.out.println(array);
        array.add(5);
        System.out.println(array);
        array.add(6);
        System.out.println(array);
        array.add(7);
        System.out.println(array);
        array.add(8);
        System.out.println(array);
        System.out.println(array.get(2) + " : " + array);
        System.out.println(array.remove(2) + " : " + array);

        System.out.println(array.remove(0) + " : " + array);
        System.out.println(array.remove(0) + " : " + array);
        System.out.println(array.remove(0) + " : " + array);
        System.out.println(array.remove(0) + " : " + array);
        System.out.println(array.remove(0) + " : " + array);
        System.out.println(array.remove(0) + " : " + array);
        System.out.println(array.remove(0) + " : " + array);
    }

}


