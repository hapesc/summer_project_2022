package com.randomStr;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Random_str {
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread t[]=new Thread[40];
        for(int i=1;i<=10;i++){
            t[i-1]=new Thread(new Task(1));
            t[i-1].start();
            t[i-1].join();
        }
        System.out.println("success");
    }
}

