package com.multi_thread_sort;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

public class BigDataSort {
    public static void main(String[] args) throws IOException {
        String filePath="/Users/michael-liang/Desktop/IO_Test/TestSet_B.txt";
        String tempPath="/Users/michael-liang/Desktop/IO_Test/TestSet_B_temp/";
        Path tempDir = Paths.get(tempPath);
        if(!new File(tempPath).isDirectory())
            Files.createDirectory(tempDir);
        tempPath+="temp";
        String[] FilePaths=new String[26];
        sortSingleFile(filePath,tempPath);

    }

    public static void sortSingleFile(String filePath,String tempPath) throws IOException {
//
        int batchSize=500000;
        int eof = 0;
        long start = System.currentTimeMillis();
        int cnt=0,PathCnt=0;
        String[] buffer = new String[batchSize];
        try (Reader data = new InputStreamReader(Files.newInputStream(Paths.get(filePath)));

        ) {

            while(eof!=-1) {
                Writer output = new FileWriter(tempPath+ PathCnt +".txt");

                if(cnt>=batchSize) {
                    buffer = new String[batchSize];
                    PathCnt++;
                }

                char[] temp = new char[16];
                for ( cnt = 0; cnt < batchSize; cnt++) {
                    eof = data.read(temp, 0, 16);
                    buffer[cnt] = String.valueOf(temp);
                }

//      多线程归并排序
                ForkJoinPool pool = new ForkJoinPool(); // 分配一个并行线程池
                SortTask sortTask = new SortTask(buffer, new StringSort());
                pool.execute(sortTask);
                String[] result = sortTask.compute();

                for (String a : result)
                    output.write(a);
                System.out.println("success"+PathCnt);
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        } finally {
            System.out.println(System.currentTimeMillis() - start);
        }
        PathCnt--;
        int[] a=new int[2];
        a[0]=0;a[1]=1;
        mergeTempFile(a,tempPath,batchSize,"/Users/michael-liang/Desktop/IO_Test/TestSet_B_sorted01.txt");

    }

    private static void mergeTempFile(int[]num,String dir,int batchSize,String outputPath) throws IOException {
        long start=System.currentTimeMillis();
        try(Writer output=new FileWriter(outputPath)) {
            ArrayList<String[]> temp = new ArrayList<>();
            Reader[] tempFile = new Reader[num.length];
            int eof = 0;
            for (int i = 0; i < num.length; i++) {
                tempFile[i] = new FileReader(dir + num[i] + ".txt");
            }
            int cnt = 0;
            char buffer[] = new char[16];
            //初始化字符串数组temp，以完成第一轮比较
            for (int i = 0; i < tempFile.length; i++) {
                String[] a = new String[batchSize];
                for (int j = 0; j < batchSize; j++) {
                    tempFile[i].read(buffer, 0, 16);
                    a[j] = String.valueOf(buffer);
                }
                temp.add(a);

            }
            String[] result = new MergeSort().merge2way(temp.get(0), temp.get(1));
            for (String a : result)
                output.write(a);
        }finally {
            System.out.println("merge success"+" time:"+(start-System.currentTimeMillis()));
        }
    }

    private static int findMin(String[] arr){
        int index=0;
        for(int i=0;i<arr.length;i++){
            if(arr[i].compareTo(arr[index])<0)
                index=i;
        }
        return index;
    }

}
