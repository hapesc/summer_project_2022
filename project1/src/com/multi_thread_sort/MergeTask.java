package com.multi_thread_sort;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;

public class MergeTask implements Runnable {
//    private static ForkJoinPool pool = new ForkJoinPool();
    private static TempFileMerge task = null;
    private int size;
    private char alpha;
    private GetFilePath path;
    private int num;
    private int tempNum;
    private int mergedTimes;
    private List<Integer> arrayList;
    private CountDownLatch cLock=new CountDownLatch(5);

    public MergeTask(char alpha, GetFilePath path, List<Integer> arrayList, int num, int tempNum, int mergedTimes) {
        this.arrayList=arrayList;
        this.size = arrayList.size();
        this.alpha = alpha;
        this.mergedTimes = mergedTimes;
        this.path = path;
        this.num = num;
        this.tempNum = tempNum;
    }

    @Override
    public void run() {
        Thread[] threads=new Thread[5];
        for(int i=0;i<5;i++){
            int from=i*500;
            int to=(i+1)*500<arrayList.size()?(i+1)*500:arrayList.size();
            threads[i]=new Thread(new TempFileMerge(alpha,path,arrayList.subList(from,to),num,tempNum,mergedTimes,cLock));
            threads[i].start();
        }


        try {
            cLock.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cLock=new CountDownLatch(1);
        new Thread(new TempFileMerge(alpha,path,BigDataSort.getList(5),num,tempNum,mergedTimes+1,cLock)).start();
        try {
            cLock.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Merge Task Finish:" + alpha + mergedTimes);
    }
}
