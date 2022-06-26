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

        long start=System.currentTimeMillis();
        for(int i=1;i<=40;i++) {
            String filePath=path.getInputPath(i,-1,'0');
            System.out.println(filePath);
            sortSingleFile(i,filePath, path.TempPaths);
        }
        System.out.println("总用时："+(System.currentTimeMillis()-start)/1000);
    }

    /**
     *
     * @param num       文件序号
     * @param filePath  待排序文件地址
     * @param tempPaths 临时文件地址
     * @throws IOException
     */
    public static void sortSingleFile(int num,String filePath,String[] tempPaths) throws IOException {

        int batchSize=250000;
        int eof = 0;
        long start = System.currentTimeMillis();
        int cnt=0,PathCnt=-1;
        String line;
        ArrayList<String> buffer=new ArrayList<>(batchSize);
        Writer[] output=new FileWriter[26];
//        for(int i=0;i<26;i++){
//            char alpha= (char) ('a'+i);
//
//            output[i]=new FileWriter(path.getTempFile(alpha,num,PathCnt),true);
//        }
        try (BufferedReader data = new BufferedReader(new FileReader(filePath))) {

            while ((line = data.readLine()) != null) {
                buffer.add(line+'\n');

                if (buffer.size() >= batchSize) {
                    PathCnt++;
                    for (int i = 0; i < 26; i++) {

                        char alpha = (char) ('a' + i);
                        String p=path.getTempFile(alpha, num, PathCnt);
                        if(PathCnt>0)
                        output[i].close();
                        output[i] = new FileWriter(p, true);
                        path.tempFiles.get(i).add(p);

                    }


                    //      多线程归并排序
//                    ForkJoinPool pool = new ForkJoinPool(); // 分配一个并行线程池
//                    SortTask sortTask = new SortTask(buffer, new StringSort());
//                    pool.execute(sortTask);
                    String[] result=new String[batchSize];
                    buffer.toArray(result);
                    Arrays.parallelSort(result);
//                    String[] result = sortTask.compute();

                    for (String a : result) {
                        int at = a.charAt(0) - 'a';
                        if (at >= 0)
                            output[at].write(a);
                    }
//                    System.out.println("success" + PathCnt);
                    buffer.clear();
                }
            }
        }catch(IOException e){
                e.printStackTrace();
                System.out.println("error");
            } finally{
                System.out.println(System.currentTimeMillis() - start);
            }
        PathCnt--;
        }



    }


