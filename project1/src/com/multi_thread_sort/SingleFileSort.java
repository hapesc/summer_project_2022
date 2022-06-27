package com.multi_thread_sort;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleFileSort implements Runnable{
    public static GetFilePath path;
    public int num;

    public SingleFileSort(GetFilePath path, int num) {
        this.path = path;
        this.num = num;
    }
    public static void sortSingleFile(int num,String filePath,String[] tempPaths) throws IOException {

        int batchSize=1000000;
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
        }
        PathCnt--;
        ExecutorService pools= Executors.newCachedThreadPool();
        for(int i=0;i<26;i++){
            char alpha= (char) ('a'+i);
            TempFileMerge task=new TempFileMerge(alpha,path.tempFiles.get(i),path);
            pools.submit(task);
        }
        System.out.println((System.currentTimeMillis() - start)/1000.0);
    }


    @Override
    public void run() {
        try {
            sortSingleFile(num,path.getInputPath(num,-1,'0'), path.TempPaths);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
