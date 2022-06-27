package com.multi_thread_sort;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.*;

public class BigDataSort {
    static GetFilePath path=GetFilePath.getFilePath();

    public static void main(String[] args) throws IOException, InterruptedException {

        long start=System.currentTimeMillis();
        ExecutorService pools=Executors.newCachedThreadPool();

        for(int i=1;i<=8;i++) {
            String filePath=path.getInputPath(i,-1,'0');
            System.out.println(filePath);
            SingleFileSort task=new SingleFileSort(path,i);
            pools.submit(task);
        }
        if(pools.awaitTermination(10,TimeUnit.MINUTES))
        for(int i=0;i<26;i++){
            char alpha=(char)('a'+i);
            pools.submit(new TempFileMerge(alpha,path.getTempFiles().get(i),path,-1));
        }




    }




}


