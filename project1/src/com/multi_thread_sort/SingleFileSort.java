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
    public static void sortSingleFile(int num,String filePath,String[] tempPaths) throws IOException, InterruptedException {
        ArrayList<ArrayList<String>> tempFileFromSingleFile=new ArrayList<>(26);
        int batchSize=1000000;
        int eof = 0;
        long start = System.currentTimeMillis();
        int cnt=0,PathCnt=-1;
        String line;
        ArrayList<String> buffer=new ArrayList<>(batchSize);
        Writer[] output=new FileWriter[26];
        for(int i=0;i<26;i++){
            tempFileFromSingleFile.add(i,new ArrayList<>());
        }
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
//                        path.getTempFiles().get(i).add(p);
                        tempFileFromSingleFile.get(i).add(p);

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
        ExecutorService pools=Executors.newCachedThreadPool();

        for(int i=0;i<26;i++) {
            char alpha=(char)('a'+i);
            TempFileMerge task=new TempFileMerge(alpha,tempFileFromSingleFile.get(i),path,0);
            pools.submit(task);
        }
        pools.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println((System.currentTimeMillis() - start)/1000.0);
    }


    @Override
    public void run() {
        try {
            sortSingleFile(num,path.getInputPath(num,-1,'0'), path.getTempPaths());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
