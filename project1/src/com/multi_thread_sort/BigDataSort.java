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
    static GetFilePath path=new GetFilePath();
    public static void main(String[] args) throws IOException {
//        String filePath="/Users/michael-liang/Desktop/IO_Test/TestSet_B.txt";
//        String tempPath="/Users/michael-liang/Desktop/IO_Test/TestSet_B_temp/";
//        String[] FilePaths=new String[41];


        for(int i=1;i<=40;i++) {
            String filePath=path.getInputPath(i,-1,'0');
            sortSingleFile(i,filePath, path.TempPaths);
        }

    }

    public static void sortSingleFile(int num,String filePath,String[] tempPaths) throws IOException {

        int batchSize=500000;
        int eof = 0;
        long start = System.currentTimeMillis();
        int cnt=0,PathCnt=0;
        String[] buffer = new String[batchSize];
        try (Reader data = new InputStreamReader(Files.newInputStream(Paths.get(filePath)));

        ) {

            while(eof!=-1) {


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

                for (String a : result) {
                    char alpha = a.charAt(0);

                    Writer output = new FileWriter(path.getOutPath(alpha,num));
                    output.write(a);
                    output.close();
                }
                System.out.println("success"+PathCnt);

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        } finally {
            System.out.println(System.currentTimeMillis() - start);
        }
        PathCnt--;


    }

    private static void mergeTempFile(int[]num,int batchSize) throws IOException {
        long start=System.currentTimeMillis();
        String outputPath;
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
