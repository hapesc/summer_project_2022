package com.multi_thread_sort;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class MergeTask implements Runnable{
    private static ForkJoinPool pool=new ForkJoinPool();
    private static TempFileMerge task=null;
    private int size;
    private char alpha;
    private GetFilePath path;
    private int num;
    private int tempNum;
    private int mergedTimes;
    public MergeTask(char alpha, GetFilePath path, List<Integer> arrayList, int num, int tempNum, int mergedTimes) {
        task=new TempFileMerge(alpha,path,arrayList,num,tempNum,mergedTimes);
        this.size=arrayList.size();
        this.alpha=alpha;
        this.mergedTimes=mergedTimes;
        this.path=path;
        this.num=num;
        this.tempNum=tempNum;
    }

    @Override
    public void run() {

        while(size>1){
            pool.invoke(task);
            size=task.join();
            mergedTimes++;
            task=new TempFileMerge(alpha,path,BigDataSort.getList(size),num,tempNum,mergedTimes);
        }
        System.out.println("Merge Task Finish:"+alpha+mergedTimes);
    }
}
