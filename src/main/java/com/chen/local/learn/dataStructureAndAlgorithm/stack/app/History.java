package com.chen.local.learn.dataStructureAndAlgorithm.stack.app;

import com.chen.local.learn.dataStructureAndAlgorithm.stack.ArrayStack;
import org.apache.commons.lang3.StringUtils;

/**
 * 浏览器历史记录
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/25
 */
public class History {

    private final ArrayStack<String> backStack;
    private final ArrayStack<String> advanceStack;
    private String curr;

    public History() {
        backStack = new ArrayStack<>();
        advanceStack = new ArrayStack<>();
    }

    public void go(String url) {
        if (StringUtils.isNotBlank(curr)) {
            backStack.push(curr);
        }
        curr = url;
        advanceStack.clear();
    }

    public String back() {
        if (StringUtils.isBlank(curr) || backStack.size() <= 0) {
            return null;
        }
        advanceStack.push(curr);
        curr = backStack.pop();
        return curr;
    }

    public String advance() {
        if (StringUtils.isBlank(curr) || advanceStack.size() <= 0) {
            return null;
        }
        backStack.push(curr);
        curr = advanceStack.pop();
        return curr;
    }

    public String curr() {
        return curr;
    }

    public static void main(String[] args) {
        History history = new History();
        history.go("http://url111");
        history.go("http://url222");
        history.go("http://url333");
        System.out.println(history.back() + " : " + history.curr());
        System.out.println(history.back() + " : " + history.curr());
        System.out.println(history.back() + " : " + history.curr());
        System.out.println(history.advance() + " : " + history.curr());
        System.out.println(history.advance() + " : " + history.curr());
        System.out.println(history.advance() + " : " + history.curr());
        System.out.println(history.back() + " : " + history.curr());
        System.out.println(history.back() + " : " + history.curr());
        System.out.println(history.back() + " : " + history.curr());

        System.out.println("==============================================================");
        history.go("http://url444");
        history.go("http://url555");
        System.out.println(history.advance() + " : " + history.curr());
        System.out.println(history.back() + " : " + history.curr());
        System.out.println(history.back() + " : " + history.curr());
        System.out.println(history.back() + " : " + history.curr());
        System.out.println(history.advance() + " : " + history.curr());
        System.out.println(history.advance() + " : " + history.curr());
        System.out.println(history.advance() + " : " + history.curr());
    }

}


