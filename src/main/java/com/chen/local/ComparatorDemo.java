package com.chen.local;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/11/26
 */
public class ComparatorDemo {

    public static void main(String[] args) {
        List<DemoClass> sortList = new ArrayList<>();
        sortList.add(new DemoClass(1, "a"));
        sortList.add(new DemoClass(6, "b"));
        sortList.add(new DemoClass(13, "c"));
        sortList.add(new DemoClass(null, null));
        sortList.add(new DemoClass(null, "e"));
        sortList.add(new DemoClass(1, null));
        System.out.println(sortList);

        sortList = sortList.stream()
                .sorted(Comparator.comparing(DemoClass::getId, Comparator.nullsFirst(Integer::compareTo))
                        .thenComparing(DemoClass::getName, Comparator.nullsFirst(String::compareTo)))
                .collect(Collectors.toList());
        System.out.println(sortList);
    }

    @Data
    @AllArgsConstructor
    public static class DemoClass {
        Integer id;
        String name;
    }

}


