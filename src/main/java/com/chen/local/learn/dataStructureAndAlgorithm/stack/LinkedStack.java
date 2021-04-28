package com.chen.local.learn.dataStructureAndAlgorithm.stack;

import com.chen.local.learn.dataStructureAndAlgorithm.IStack;

/**
 * 链式栈（链表，无限）
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/28
 */
public class LinkedStack<T> implements IStack<T> {

    private LinkedStack<T> top;

    private LinkedStack<T> prev;
    private T element;
    private int size;

    public LinkedStack() {}

    private LinkedStack(T element, LinkedStack<T> prev) {
        this.element = element;
        this.prev = prev;
    }

    @Override
    public boolean push(T e) {
        top = new LinkedStack<>(e, top);
        size++;
        return true;
    }

    @Override
    public T pop() {
        if (top == null) {
            return null;
        }
        LinkedStack<T> removed = top;
        T element = removed.element;
        top = removed.prev;
        removed.prev = null;
        removed.element = null;
        size--;
        return element;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        top = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        LinkedStack<T> curr = top;
        while (curr != null) {
            sb.insert(0, "," + curr.element);
            curr = curr.prev;
        }
        return "[" + (sb.length() <= 0 ? "" : sb.substring(1)) + "]";
    }

    public static void main(String[] args) {
        LinkedStack<Integer> stack = new LinkedStack<>();
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


