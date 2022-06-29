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
        //先进行500路归并,每一个归并任务交给一个线程执行
        Thread[] threads=new Thread[5];
        for(int i=0;i<5;i++){
            int from=i*500;
            int to= Math.min((i + 1) * 500, arrayList.size());
            threads[i]=new Thread(new TempFileMerge(alpha,path,arrayList.subList(from,to),num,tempNum,mergedTimes,cLock));
            threads[i].start();
        }


        try {
            cLock.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cLock=new CountDownLatch(1);
        //然后进行5路归并
        new Thread(new TempFileMerge(alpha,path,BigDataSort.getList(5),-1,-1,mergedTimes+1,cLock)).start();
        try {
            cLock.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Merge Task Finish:" + alpha + mergedTimes);
    }
}
