package com.randomStr;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executor;

public class Task implements Runnable {

    @Override
    public void run() {
        try (FileWriter output = new FileWriter("/Users/michael-liang/Desktop/IO_Test/TestSet_B.txt",true)) {

            Random r = new Random();
            StringBuffer sb;
            String strSeed = "abcdefghijklmnopqrstuvwxyz";
            for (long i = 0; i < 1e8; i++) {
                sb = new StringBuffer(16);
                sb.append('b');
                for (int j = 0; j < 14; j++) {
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
