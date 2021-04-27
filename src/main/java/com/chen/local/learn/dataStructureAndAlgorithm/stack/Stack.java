package com.chen.local.learn.dataStructureAndAlgorithm.stack;

import com.chen.local.learn.dataStructureAndAlgorithm.IStack;
import com.chen.local.learn.dataStructureAndAlgorithm.array.Array;

/**
 * 栈
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/25
 */
public class Stack<T> implements IStack<T> {

    private Array<T> array;
    private int size;

    public Stack() {
        this.array = new Array<>();
        this.size = 0;
    }

    @Override
    public void push(T e) {
        array.add(e);
        size++;
    }

    @Override
    public T pop() {
        if (size <= 0) {
            return null;
        }
        return array.remove(--size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        array = new Array<>();
        size = 0;
    }

    @Override
    public String toString() {
        return array.toString();
    }

    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.size());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.size());
    }

}


