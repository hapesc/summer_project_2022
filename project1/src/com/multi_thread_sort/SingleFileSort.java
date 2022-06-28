package com.multi_thread_sort;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingleFileSort implements Runnable{
    public static GetFilePath path;
    public int num;

    public SingleFileSort(GetFilePath path, int num) {
        this.path = path;
        this.num = num;
    }
    public static void sortSingleFile(int num,String filePath) throws IOException, InterruptedException {

        int batchSize=1000000;
        int eof = 0;
        long start = System.currentTimeMillis();
        int cnt=0,PathCnt=-1;
        String line;
        ArrayList<String> buffer=new ArrayList<>(batchSize);
        Writer[] output=new FileWriter[26];
        for(int i=0;i<26;i++){
            path.getTempFiles().get(0).add(i,new ArrayList<>());
        }
        try (BufferedReader data = new BufferedReader(new FileReader(filePath))) {

            while ((line = data.readLine()) != null) {
                buffer.add(line+'\n');

                if (buffer.size() >= batchSize) {
                    PathCnt++;
                    for (int i = 0; i < 26; i++) {

                        char alpha = (char) ('a' + i);
                        String p=path.getOutputPath(alpha, num, PathCnt,0);
                        if(PathCnt>0)
                            output[i].close();
                        output[i] = new FileWriter(p);
//                        path.getTempFiles().get(i).add(p);
                        path.getTempFiles().get(0).get(i).add(p);

                    }


                    //      多线程归并排序
                    String[] result=new String[batchSize];
                    buffer.toArray(result);
                    Arrays.parallelSort(result);

                    for (String a : result) {
                        int at = a.charAt(0) - 'a';
                        if (at >= 0)
                            output[at].write(a);
                    }
                    buffer.clear();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("error");
        }
        PathCnt--;

        System.out.println((System.currentTimeMillis() - start)/1000.0);
    }


    @Override
    public void run() {
        try {
            sortSingleFile(num,path.getInputPath(num,-1,0,'0'));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
