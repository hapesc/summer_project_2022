package com.multi_thread_sort;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class MergeTask implements Runnable {
    private static ForkJoinPool pool = new ForkJoinPool();
    private static TempFileMerge task = null;
    private int size;
    private char alpha;
    private GetFilePath path;
    private int num;
    private int tempNum;
    private int mergedTimes;

    public MergeTask(char alpha, GetFilePath path, List<Integer> arrayList, int num, int tempNum, int mergedTimes) {
        task = new TempFileMerge(alpha, path, arrayList, num, tempNum, mergedTimes);
        this.size = arrayList.size();
        this.alpha = alpha;
        this.mergedTimes = mergedTimes;
        this.path = path;
        this.num = num;
        this.tempNum = tempNum;
    }

    @Override
    public void run() {

        while (size > 1) {
            System.out.println("Task:" + alpha + (mergedTimes + 1));
            size=pool.invoke(task);
            mergedTimes++;
//            size = new HashSet<>(path.getTempFiles().get(mergedTimes).get(alpha-'a')).size();
//            size/=2;
            task = new TempFileMerge(alpha, path, BigDataSort.getList(size), num, tempNum, mergedTimes);
            pool = new ForkJoinPool();
        }
        System.out.println("Merge Task Finish:" + alpha + mergedTimes);
    }
}
