package com.chen.local.learn.dataStructureAndAlgorithm.file;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * TODO
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/24
 */
public class FileReader {

    public static void main(String[] args) throws Exception {
        File f = new File("/Users/chenchen/Desktop/4，Demo/file/read.txt");
        if (f.exists()) {
            f.delete();
            System.out.println("==== delete end ====");
        }

        FileOutputStream fileOutput = new FileOutputStream(f);
        for (int i = Integer.MAX_VALUE - 20; i < Integer.MAX_VALUE; ++i) {
            System.out.println(i);
            byte[] bytes = {(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) (i)};
            fileOutput.write(bytes);
        }
        fileOutput.flush();
        fileOutput.close();
        System.out.printf("==== write end (%s) ====%n", f.length());

        FileInputStream fileInput = new FileInputStream(f);
        byte[] bytes = new byte[4];
        fileInput.skip(0);
        fileInput.read(bytes);
        System.out.printf("%s %s %s %s%n", bytes[0] << 24, bytes[1] << 24 >>> 8, bytes[2] << 24 >>> 16, bytes[3] << 24 >>> 24);
        System.out.printf("%s%s%s%s%n"
                , StringUtils.leftPad(Integer.toBinaryString(bytes[0]), 8, "0")
                , StringUtils.leftPad(Integer.toBinaryString(bytes[1]), 8, "0")
                , StringUtils.leftPad(Integer.toBinaryString(bytes[2]), 8, "0")
                , StringUtils.leftPad(Integer.toBinaryString(bytes[3]), 8, "0"));
        // >>> 保留高位
        int i = bytes[0] << 24
                | bytes[1] << 24 >>> 8
                | bytes[2] << 24 >>> 16
                | bytes[3] << 24 >>> 24;
        System.out.println(i);
        fileInput.close();
        System.out.println("==== read end ====");
    }

}


