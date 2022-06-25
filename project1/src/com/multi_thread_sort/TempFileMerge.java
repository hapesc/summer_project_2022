package com.multi_thread_sort;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;



public class TempFileMerge implements Runnable {
    private static MergeSort mergeSort=new MergeSort();
    private ArrayList<String[]> arrays;
    int[] num;
    String tempDir;
    int batchSize;
    String outputPath;
    public TempFileMerge(int[]num,String dir,int batchSize,String outputPath) {
        this.batchSize=batchSize;
        this.num=num;
        this.outputPath=outputPath;
        this.tempDir=dir;
    }

    @Override
    public void run() {
        long start=System.currentTimeMillis();
        try(Writer output=new FileWriter(outputPath)) {
            arrays=new ArrayList<>();
            Reader[] tempFile = new Reader[num.length];
            int eof = 0;
            for (int i = 0; i < num.length; i++) {
                tempFile[i] = new FileReader(tempDir + num[i] + ".txt");
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
                arrays.add(a);

            }
            String[] result=null;
            if(arrays.size()==2)
             result = mergeSort.merge2way(arrays.get(0),arrays.get(1));
            else if (arrays.size()==3) {
                result= mergeSort.merge3way(arrays.get(0),arrays.get(1),arrays.get(2));
            }
            for (String a : result)
                output.write(a);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("merge success"+" time:"+(start-System.currentTimeMillis()));
        }
    }
}
