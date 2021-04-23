package com.chen.local;

import com.chen.local.base.utils.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TODO
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/11/26
 */
public class Demo {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(null);
        list.add(2);
        list.add(3);
        list.add(null);
        list.add(4);
        list.add(5);
        System.out.println(list);

        list.sort(Comparator.nullsLast(Comparator.comparing(Integer::intValue)).thenComparing(Integer::byteValue).reversed());
        System.out.println(list);
        System.out.println(list.stream().filter(Objects::nonNull).filter(integer -> integer % 2 == 0).collect(Collectors.toList()));
    }

    @Data
    private static class Test {
        private long id;
        public Test(long id) {
            this.id = id;
        }
    }
}


