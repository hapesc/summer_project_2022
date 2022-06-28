package com.multi_thread_sort;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

public class BigDataSort {
    static GetFilePath path = GetFilePath.getFilePath();
    public static final String[] IPadress={"",
            "10.251.134.43",
            "10.251.134.44",
            "10.251.134.112",
            "10.251.134.66",
            "10.251.134.67",
            "10.251.134.68",
            "10.251.134.79",
            "10.251.134.80"};

    public static void main(String[] args) throws IOException, InterruptedException {

        long start = System.currentTimeMillis();
        ExecutorService pools = null;

        for (int i = 1; i <= 1; i++) {
            String filePath = path.getInputPath(i, -1, 0, '0');
            System.out.println(filePath);
            new SingleFileSort(path, i).start();
        }


            pools=Executors.newFixedThreadPool(26);
            int[] sizes=new int[26];

//            ArrayList<ForkJoinPool> forkpools = new ArrayList<>(26);
            MergeTask[] tasks = new MergeTask[26];



            for (int i = 0; i < 26; i++) {

                        char alpha = (char) ('a' + i);
//                        int size = new HashSet<>(path.getTempFiles().get(0).get(i)).size();

                        sizes[i] = 2500;
//                        System.out.println(sizes[i]);
                        tasks[i] = new MergeTask(alpha, path, getList(sizes[i]), 1, 1, 0);
                        pools.submit(tasks[i]);
//                        tasks[i].run();
            }
//            pools.shutdown();
            pools.awaitTermination(20,TimeUnit.MINUTES);
            //todo 归并完将临时文件移动到result文件夹


        System.out.println("总用时：" + (System.currentTimeMillis() - start) / 1000.0);


    }
    public static ArrayList<Integer> getList(int size){
        ArrayList<Integer> a=new ArrayList<>(size);
        for(int i=0;i<size;i++)
            a.add(i);
        return a;
    }

}


