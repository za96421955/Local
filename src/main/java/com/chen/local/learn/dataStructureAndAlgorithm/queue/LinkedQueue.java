package com.chen.local.learn.dataStructureAndAlgorithm.queue;

import com.chen.local.learn.dataStructureAndAlgorithm.IQueue;

/**
 * 链式队列（链表，无界）
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/27
 */
public class LinkedQueue<T> implements IQueue<T> {
    private LinkedQueue<T> head;
    private LinkedQueue<T> tail;

    private LinkedQueue<T> next;
    private T element;
    private int length;

    public LinkedQueue() {
    }

    private LinkedQueue(T element, LinkedQueue<T> next) {
        this.element = element;
        this.next = next;
    }

    @Override
    public boolean enqueue(T t) {
        if (head == null) {
            head = new LinkedQueue<>(t, null);
            tail = head;
        } else {
            tail.next = new LinkedQueue<>(t, null);
            tail = tail.next;
        }
        length++;
        return true;
    }

    @Override
    public T dequeue() {
        LinkedQueue<T> removeNode = head;
        if (removeNode == null) {
            return null;
        }
        T element = removeNode.element;
        head = removeNode.next;
        // 移除head
        removeNode.next = null;
        removeNode.element = null;
        length--;
        return element;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        length = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        LinkedQueue<T> curr = head;
        while (curr != null) {
            sb.append(",").append(curr.element);
            curr = curr.next;
        }
        return "[" + (sb.length() <= 0 ? "" : sb.substring(1)) + "]";
    }

    public static void main(String[] args) {
        LinkedQueue<String> queue = new LinkedQueue<>();
        queue.enqueue("aaa");
        System.out.println(queue);
        queue.enqueue("bbb");
        System.out.println(queue);
        queue.enqueue("ccc");
        System.out.println(queue);
        queue.enqueue("ddd");
        System.out.println(queue);
        queue.enqueue("eee");
        System.out.println(queue);
        System.out.println(queue.size());

        System.out.println("\n=========================================");
        System.out.println(queue.dequeue());
        System.out.println(queue);
        System.out.println(queue.dequeue());
        System.out.println(queue);
        System.out.println(queue.dequeue());
        System.out.println(queue);
        System.out.println(queue.dequeue());
        System.out.println(queue);
        System.out.println(queue.dequeue());
        System.out.println(queue);

        System.out.println("\n=========================================");
        queue.enqueue("eee");
        System.out.println(queue);
        System.out.println(queue.size());
        queue.clear();
        System.out.println(queue);
    }

}
