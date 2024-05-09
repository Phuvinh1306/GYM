package com.hotrodoan.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class test {
    public static void main(String[] args) {
        Path pa = Paths.get(System.getProperty("user.dir"));
        System.out.println(pa);
    }
}
