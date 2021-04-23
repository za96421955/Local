package com.chen.local.learn.dataStructureAndAlgorithm.array;

import com.chen.local.learn.dataStructureAndAlgorithm.Structure;

import java.util.Arrays;

/**
 * 数组
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/22
 */
public class StepArray<T> extends Array<T> {

    private Object[] expandArray;
    private int copyCursor;
    private int copyEndCursor;

    // 逐步扩展
    @Override
    protected void expand() {
        if (expandArray != null || !this.checkExpand()) {
            return;
        }
        length *= EXPAND_MULTIPLE;
        expandArray = new Object[length];
        copyCursor = 0;
        copyEndCursor = cursor - 1;
        this.copy();
    }

    protected void copy() {
        if (expandArray == null) {
            return;
        }
        expandArray[copyCursor] = array[copyCursor++];
        if (copyCursor > copyEndCursor) {
            array = expandArray;
            expandArray = null;
            copyCursor = 0;
            copyEndCursor = 0;
        }
    }

    @Override
    public Structure<T> add(T e) {
        if (expandArray != null) {
            expandArray[cursor++] = e;
            this.copy();
            return this;
        }
        super.add(e);
        return this;
    }

    @Override
    public T get(int i) {
        Object element = null;
        if (expandArray != null) {
            element = expandArray[i];
            this.copy();
        }
        return element != null ? (T) element : super.get(i);
    }

    @Override
    public T remove(int i) {
        T element = this.get(i);
        if (expandArray != null) {
            expandArray[i] = null;
        }
        if (i < array.length) {
            array[i] = null;
        }
        this.compress(i);
        cursor--;
        this.reduce();
        return element;
    }

    @Override
    protected void compress(int index) {
        if (expandArray != null) {
            for (int i = index; i < expandArray.length - 1; i++) {
                Object temp = expandArray[i];
                expandArray[i] = expandArray[i + 1];
                expandArray[i + 1] = temp;
            }
        }
        if (index < array.length) {
            super.compress(index);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(array) + " : " + Arrays.toString(expandArray);
    }

    public static void main(String[] args) {
        StepArray<Integer> array = new StepArray<>();
        System.out.println(array.add(1));
        System.out.println(array.add(2));
        System.out.println(array.add(3));
        System.out.println(array.add(4));
        System.out.println(array.add(5));
        System.out.println(array.add(6));
        System.out.println(array.add(7));
        System.out.println(array.get(2) + " : " + array);
        System.out.println(array.remove(2) + " : " + array);

        System.out.println(array.add(8));
        System.out.println(array.add(9));
        System.out.println(array.get(6) + " : " + array);
        System.out.println(array.get(6) + " : " + array);
        System.out.println(array.get(6) + " : " + array);
        System.out.println(array.get(6) + " : " + array);
        System.out.println(array.get(6) + " : " + array);
    }

}
