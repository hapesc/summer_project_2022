package com.multi_thread_sort;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;

public class BigDataSort {
    public static void main(String[] args) throws IOException {
        String filePath="/Users/michael-liang/Desktop/IO_Test/TestSet_B.txt";
        String tempPath="/Users/michael-liang/Desktop/IO_Test/TestSet_B_temp/";
        Files.createDirectory(Paths.get(tempPath));
        tempPath+="temp";
        String[] FilePaths=new String[26];
        sortSingleFile(filePath,tempPath);
    }

    public static void sortSingleFile(String filePath,String tempPath) throws FileNotFoundException {
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
//      单次读取100000行
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
                String[] result = sortTask.join();

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
//        mergeTempFile(PathCnt,tempPath);

    }

    private static void mergeTempFile(int num,String dir) throws FileNotFoundException {
        String[] merged=new String[num];
        Reader[] tempFile=new Reader[num];
        int eof=0;
        for(int i=0;i<num;i++){
            tempFile[i]=new FileReader(dir+i+".txt");
        }


    }

}
