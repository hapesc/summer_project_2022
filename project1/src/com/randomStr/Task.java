package com.randomStr;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executor;

public class Task implements Runnable {
    String path;
    public Task(int i) {
        if(i<10)
        this.path="0"+i+".txt";
        else
            this.path=i+".txt";
    }

    @Override
    public void run() {
        try (FileWriter output = new FileWriter("D:/IO_Test/data"+path,true)) {

            Random r = new Random();
            StringBuffer sb;
            String strSeed = "abcdefghijklmnopqrstuvwxyz";
            for (long i = 0; i < 1e8; i++) {
                sb = new StringBuffer(16);
                for (int j = 0; j < 15; j++) {
                    sb.append(strSeed.charAt(r.nextInt(25)));
                }
                sb.append("\n");
                output.write(sb.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
