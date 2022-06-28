package com.multi_thread_sort;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RecursiveTask;


public class TempFileMerge implements Runnable {
    private List<Integer> tempFiles;
    private GetFilePath path;
    private FailedTree<String> tree;
    private char alpha;
    private int num;
    private int mergedTimes;
    private int tempNum;
    private int batchSize=100000;
    private CountDownLatch cLock;

    public TempFileMerge(char alpha, GetFilePath path, List<Integer> arrayList, int num, int tempNum, int mergedTimes, CountDownLatch cLock) {
        this.tempFiles = arrayList;
        this.path = path;
        this.alpha = alpha;
        this.num = num;
        this.mergedTimes = mergedTimes;
        this.tempNum = tempNum;
        this.cLock=cLock;
    }


    public void run() {
        mergeFile();
        cLock.countDown();
    }



    private    void mergeFile( ) {
        List<Integer> fileNameList=tempFiles;
        Map<BufferedReader, String> map = new HashMap<>();
        String outputPath=path.getOutputPath(alpha,num,tempNum,mergedTimes+1);
        path.getTempFiles().get(mergedTimes+1).get(alpha-'a').add(outputPath);
        try (FileWriter writer = new FileWriter(outputPath)) {

            for (Integer fileName : fileNameList) {
                BufferedReader tmpReader = new BufferedReader(new FileReader(path.getInputPath(num,fileName,mergedTimes,alpha)));
                map.put(tmpReader, tmpReader.readLine());
            }
            while (true) {
                boolean canRead = false;
                Map.Entry<BufferedReader, String> minEntry = null;
                for (Map.Entry<BufferedReader, String> entry : map.entrySet()) {
                    String value = entry.getValue();
                    if (value == null) {
                        continue;
                    }
                    // 获取当前 reader 内容最小 entry
                    if ((minEntry == null) || ( value.compareTo(minEntry.getValue())< 0)) {
                        minEntry = entry;
                    }
                    canRead = true;
                }
                // 当且仅当所有 reader 内容为空时，跳出循环
                if (!canRead) {
                    break;
                }
                writer.write(minEntry.getValue() + '\n');
                minEntry.setValue(minEntry.getKey().readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 注意关闭分片文件输入流
            for (BufferedReader reader : map.keySet()) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}

