package com.chen.local.learn.dataStructureAndAlgorithm.stack;

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
        throw new RuntimeException("symbol not exists");
    }

    public double calcu(String content) {
        String[] contents = content.split(" ");
        Arrays.stream(contents).forEach(s -> {
            if (!isSymbol(s)) {
                Double v1 = resultStack.pop();
                if (v1 == null) {
                    resultStack.push(Double.parseDouble(s));
                    return;
                }
                double v2 = Double.parseDouble(s);
                String symbol = symbolStack.pop();
                resultStack.push(this.calcu(v1, v2, symbol));
                return;
            }
            symbolStack.push(s);
        });
        return resultStack.pop();
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        double result = calculator.calcu("1 + 1 * 2");
        System.out.println(result);
    }

}


