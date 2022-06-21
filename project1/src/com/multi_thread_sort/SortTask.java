package com.multi_thread_sort;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class SortTask extends RecursiveTask<String[]> {
    /**
     * 排序数组长度的阈值
     */
    private static final int THRESHOLD = 100;

    private static MergeSort mergeSort = new MergeSort();
    /**
     * 需要排序的数组
     */
    private String[] originArray;
    /**
     * 排序执行类
     */
    private SortActuator sortActuator;

    SortTask(String[] originArray, SortActuator sortActuator) {
        this.originArray = originArray;
        this.sortActuator = sortActuator;
    }

    @Override
    protected String[] compute() {
        if (originArray.length <= THRESHOLD) {
            // 数组长度<+阈值，则将数组进行排序
            return sortActuator.sort(originArray);
        } else {
            // 数组长度大于阈值，则继续将数组进行拆分
            int mid = originArray.length / 2;
            String[] leftArray = Arrays.copyOfRange(originArray, 0, mid);
            String[] rightArray = Arrays.copyOfRange(originArray, mid, originArray.length);
            SortTask leftTask = new SortTask(leftArray, sortActuator);
            SortTask rightTask = new SortTask(rightArray, sortActuator);
            invokeAll(leftTask, rightTask);
            // 将拆分后的数组排序并统计结果
            return mergeSort.merge(leftTask.join(), rightTask.join());
        }
    }
}