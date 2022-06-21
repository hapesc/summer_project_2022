package com.multi_thread_sort;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;

public class FileReader {
    public static void main(String[] args) {
        int batchSize=100000;
        int eof = 0;
        long start = System.currentTimeMillis();
        try (Reader data = new InputStreamReader(Files.newInputStream(Paths.get("/Users/michael-liang/Desktop/IO_Test/TestSet_A.txt")));
             Writer output = new FileWriter("/Users/michael-liang/Desktop/IO_Test/TestSet_A_sorted.txt",true)
        ) {
            int cnt=0;
            String[] buffer = new String[batchSize];
        while(eof!=-1) {
//      单次读取100000行
            if(cnt>=batchSize)
                buffer=new String[batchSize];

            char[] temp = new char[16];
            for ( cnt = 0; cnt < batchSize; cnt++) {
                eof = data.read(temp, 0, 16);
                buffer[cnt] = String.valueOf(temp);
            }

//      多线程归并排序
            ForkJoinPool pool = new ForkJoinPool(); // 分配一个并行线程池
            SortTask sortTask = new SortTask(buffer, new StringSort());
            pool.execute(sortTask);
            String[] result = sortTask.join();

            for (String a : result)
                output.write(a);
            System.out.println("success");
        }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        } finally {
            System.out.println(System.currentTimeMillis() - start);
        }
    }
}
