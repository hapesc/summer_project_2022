package com.randomStr;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Random_str {
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread t[]=new Thread[10];
        for(int i=0;i<10;i++){
            t[i]=new Thread(new Task());
            t[i].start();
            t[i].join();
        }
        System.out.println("success");
    }
}

