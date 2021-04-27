package com.chen.local.learn.dataStructureAndAlgorithm.stack;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 计算器
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @date 2021/4/25 17:13
 */
public class Calculator {
    private static final Map<String, Integer> SYMBOLS = new HashMap<>();
    private static final String ADD = "+";
    private static final String SUBTRACT = "-";
    private static final String MULTIPLE = "*";
    private static final String DIVIDE = "/";

    private final Stack<Double> resultStack;
    private final Stack<String> symbolStack;

    static {
        SYMBOLS.put(ADD, 0);
        SYMBOLS.put(SUBTRACT, 0);
        SYMBOLS.put(MULTIPLE, 1);
        SYMBOLS.put(DIVIDE, 1);
    }

    public Calculator() {
        this.resultStack = new Stack<>();
        this.symbolStack = new Stack<>();
    }

    private boolean isSymbol(String symbol) {
        return SYMBOLS.containsKey(symbol);
    }

    public double calcu(double v1, double v2, String symbol) {
        if (ADD.equals(symbol)) {
            return v1 + v2;
        }
        if (SUBTRACT.equals(symbol)) {
            return v1 - v2;
        }
        if (MULTIPLE.equals(symbol)) {
            return v1 * v2;
        }
        if (DIVIDE.equals(symbol)) {
            return v1 / v2;
        }
        throw new RuntimeException(String.format("symbol[%s] not exists", symbol));
    }

    public double calcu(String content) {
        String[] contents = content.split(" ");
        System.out.println("contents: " + contents.length);
        Arrays.stream(contents).forEach(curr -> {
            // 若当前内容非运算符, 则直接入栈
            if (!this.isSymbol(curr)) {
                resultStack.push(Double.parseDouble(curr));
                return;
            }

            String lastSymbol = symbolStack.pop();
            // 若站内没有运算符, 则直接入栈
            if (StringUtils.isBlank(lastSymbol)) {
                symbolStack.push(curr);
                return;
            }
            // 比较last与当前运算符
            // 若 last >= curr, 则运算 last
            // 否则重新入栈
            if (SYMBOLS.get(lastSymbol) >= SYMBOLS.get(curr)) {
                double v2 = resultStack.pop();
                double v1 = resultStack.pop();
                double result = this.calcu(v1, v2, lastSymbol);
                resultStack.push(result);
                System.out.println(v1 + " " + lastSymbol + " " + v2 + " = " + result);
            } else {
                symbolStack.push(lastSymbol);
            }
            symbolStack.push(curr);
        });
        System.out.println("result: " + resultStack.toString());
        System.out.println("symbol: " + symbolStack.toString());
        // 入栈完成, 计算结果
        String symbol;
        while ((symbol = symbolStack.pop()) != null) {
            Double v2 = resultStack.pop();
            Double v1 = resultStack.pop();
            // 栈检查
            if (v1 == null || v2 == null) {
                throw new RuntimeException(String.format("content[%s] style exception", content));
            }
            double result = this.calcu(v1, v2, symbol);
            resultStack.push(result);
            System.out.println(v1 + " " + symbol + " " + v2 + " = " + result);
        }
        return resultStack.pop();
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        double result = calculator.calcu("5 + 3 * 2 - 4 / 6");
        System.out.println(result);
    }

}


