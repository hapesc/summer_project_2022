package com.multi_thread_sort;

import com.TCPtransmit.Client;
import com.TCPtransmit.Server;
import com.TCPtransmit.Transmit;

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

        System.out.println("请输入你的主机序号：");
        Scanner in=new Scanner(System.in);
        int clientName=in.nextInt();
        long start = System.currentTimeMillis();
        ExecutorService pools = null;

        for (int i = 1; i <= 1; i++) {
            String filePath = path.getInputPath(i, -1, 0, '0');
            System.out.println(filePath);
//            new SingleFileSort(path, i).start();
        }


            pools=Executors.newFixedThreadPool(2);
            int[] sizes=new int[26];

//            ArrayList<ForkJoinPool> forkpools = new ArrayList<>(26);
            MergeTask[] tasks = new MergeTask[26];



            for (int i = 0; i < 26; i++) {

                        char alpha = (char) ('a' + i);
//                        int size = new HashSet<>(path.getTempFiles().get(0).get(i)).size();

                        sizes[i] = 2500;
//                        System.out.println(sizes[i]);
                        tasks[i] = new MergeTask(alpha, path, getList(sizes[i]), 1, 1, 0);
//                        pools.submit(tasks[i]);
//                        tasks[i].run();
            }
            pools.shutdown();
            if(pools.awaitTermination(40,TimeUnit.MINUTES)){
                new Transmit(4700,path).start();
            }
            //等5min
            Thread.sleep(1000*60*5);
            System.out.println("final merge start");
            //归并
            pools=Executors.newFixedThreadPool(2);
            int THRESHOLD=clientName<3?4:3;
            char[] alphas=getAlphas(clientName);
            for(int i=0;i<THRESHOLD;i++){
                pools.submit(new TempFileMerge(alphas[i],path,null,-1,-1,-1,null));
            }
            pools.shutdown();
            pools.awaitTermination(10,TimeUnit.MINUTES);
            System.out.println("总用时：" + (System.currentTimeMillis() - start) / (60*1000.0));


    }
    public static ArrayList<Integer> getList(int size){
        ArrayList<Integer> a=new ArrayList<>(size);
        for(int i=0;i<size;i++)
            a.add(i);
        return a;
    }
    private static char[] getAlphas(int clientName){
        char[] chars=null;
        switch (clientName){
            case 1:chars= new char[]{'a', 'b', 'c', 'd'};break;
            case 2:chars=new char[]{'e','f','g','h'};break;
            case 3:chars=new char[]{'i','j','k'};break;
            case 4:chars=new char[]{'l','m','n'};break;
            case 5:chars=new char[]{'o','p','q'};break;
            case 6:chars=new char[]{'r','s','t'};break;
            case 7:chars=new char[]{'u','v','w'};break;
            case 8:chars=new char[]{'x','y','z'};break;
        }
        return chars;

    }

}


