package com.zhilai.pcb.utils;

import org.apache.ibatis.reflection.ArrayUtil;
import org.springframework.util.StringUtils;
import org.thymeleaf.util.ListUtils;

import java.util.Arrays;
import java.util.Random;

public class MacRandom {
    private static String SEPARATOR_OF_MAC = ":";
    public static String getRandomMac(){
        Random random = new Random();
        String[] mac = {
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff))
        };
        return StringUtils.collectionToDelimitedString(Arrays.asList(mac),SEPARATOR_OF_MAC);
    }

    public static void main(String[] args) {
        System.out.println(MacRandom.getRandomMac());
    }
}
