package com.chen.local.learn.dataStructureAndAlgorithm.stack;

import org.apache.commons.lang3.StringUtils;

/**
 * TODO
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/4/25
 */
public class History {

    private final Stack<String> backStack;
    private final Stack<String> advanceStack;
    private String curr;

    public History() {
        backStack = new Stack<>();
        advanceStack = new Stack<>();
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

    public static void main(String[] args) {
        History history = new History();
        history.go("http://url1111111111111111");
        history.go("http://url2222222222222222");
        history.go("http://url3333333333333333");
        System.out.println(history.back());
        System.out.println(history.back());
        System.out.println(history.back());
        System.out.println(history.advance());
        System.out.println(history.advance());
        System.out.println(history.advance());
        System.out.println(history.back());
        System.out.println(history.back());
        System.out.println(history.back());

        System.out.println("==============================================================");
        history.go("http://url4444444444444444");
        history.go("http://url5555555555555555");
        System.out.println(history.advance());
        System.out.println(history.back());
        System.out.println(history.back());
        System.out.println(history.back());
        System.out.println(history.advance());
        System.out.println(history.advance());
        System.out.println(history.advance());
    }

}


