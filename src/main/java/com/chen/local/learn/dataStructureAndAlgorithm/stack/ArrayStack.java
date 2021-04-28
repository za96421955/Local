package com.chen.local.learn.dataStructureAndAlgorithm.stack;

import com.chen.local.learn.dataStructureAndAlgorithm.IStack;
import com.chen.local.learn.dataStructureAndAlgorithm.array.Array;

/**
 * 顺序栈（数组，有限，需动态扩容）
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/25
 */
public class ArrayStack<T> implements IStack<T> {
    protected static int DEFAULT_LENGTH = 4;

    private int length;
    private Object[] array;
    private int size;

    public ArrayStack() {
        this.init(DEFAULT_LENGTH);
    }

    private void init(int length) {
        this.length = length;
        this.array = new Object[length];
        this.size = 0;
    }

    @Override
    public void push(T e) {
        if (size >= length) {
            System.out.println("stack is pull");
            return;
        }
        array[size++] = e;
    }

    @Override
    public T pop() {
        if (size <= 0) {
            return null;
        }
        return (T) array[--size];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.init(length);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(",").append(array[i]);
        }
        return "[" + (sb.length() <= 0 ? "" : sb.substring(1)) + "]";
    }

    public static void main(String[] args) {
        ArrayStack<Integer> stack = new ArrayStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        System.out.println(stack.size() + " : " + stack);

        System.out.println("\n=========================================");
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.size() + " : " + stack);

        System.out.println("\n=========================================");
        stack.push(4);
        stack.push(5);
        System.out.println(stack.size() + " : " + stack);
    }

}


