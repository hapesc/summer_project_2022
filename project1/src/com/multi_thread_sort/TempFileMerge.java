package com.multi_thread_sort;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;


public class TempFileMerge extends RecursiveTask<Integer> {
    private List<Integer> tempFiles;
    private GetFilePath path;
    private FailedTree<String> tree;
    private char alpha;
    private int num;
    private int mergedTimes;
    private int tempNum;

    public TempFileMerge(char alpha, GetFilePath path, List<Integer> arrayList, int num, int tempNum, int mergedTimes) {
        this.tempFiles = arrayList;
        this.path = path;
        this.alpha = alpha;
        this.num = num;
        this.mergedTimes = mergedTimes;
        this.tempNum = tempNum;
    }


    private void run() {
        String outputPath=path.getOutputPath(alpha,num,tempNum,mergedTimes+1);

        path.getTempFiles().get(mergedTimes+1).get(alpha-'a').add(outputPath);
        //把Temp+mergedTimes/result+a-z/num.txt和Temp+mergedTimes/result+a-z/num+1.txt归并成Temp+mergedTimes+1/result+a-z/num.txt
        try(Writer output=new FileWriter(outputPath)){

            ArrayList<BufferedReader> bufferedReaders=new ArrayList<>(tempFiles.size());
            for(int i=0;i<tempFiles.size();i++){
                bufferedReaders.add(i,new BufferedReader(new FileReader(path.getInputPath(num,tempFiles.get(i),mergedTimes,alpha))));
            }
            String line="";
            ArrayList<String> toBeMerged =new ArrayList<>(tempFiles.size());
            for(BufferedReader b:bufferedReaders){
                line=b.readLine()+'\n';
                toBeMerged.add(line);
            }
            tree=new FailedTree<>(toBeMerged);
            Integer s=tree.getWinner();
            output.write(tree.getLeaf(s));
            output.flush();
            while (bufferedReaders.size() > 0) {
                String newLeaf = bufferedReaders.get(s).readLine()+"\n";
                if (newLeaf == null || newLeaf.equals("")) {
                    bufferedReaders.get(s).close();
                    int remove = s;
                    bufferedReaders.remove(remove);
                    tree.del(s);
                } else {
                   tree.add(newLeaf, s);
                }
                s = tree.getWinner();
                if (s == null) {
                    break;
                }
                output.write(tree.getLeaf(s) );
                output.flush();
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected Integer compute() {
        //todo 重写归并
        if (tempFiles.size() ==3) {
            int size=tempFiles.size();
            BufferedReader[] reader = new BufferedReader[size];

            for (int i = 0; i < size; i++) {
                try {
                    reader[i] = new BufferedReader(new FileReader(path.getInputPath(num, tempFiles.get(i), mergedTimes, alpha)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //读取文件并归并
            try {

                String[] strings=MergeSort.merge3way(readFromFile(reader[0]),readFromFile(reader[1]),readFromFile(reader[2]))  ;
                String outputPath = path.getOutputPath(alpha, num, tempNum, mergedTimes + 1);
                path.getTempFiles().get(mergedTimes + 1).get(alpha - 'a').add(outputPath);
//                a.add(1);
                Writer out = new FileWriter(outputPath);
                for (String s : strings) {
                    out.write(s);
                    out.flush();
                }
                out.close();
//                for(String s:tempFiles){
//                    new File(s).delete();
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return 1;
        }
        if (tempFiles.size() == 2) {
//            ArrayList<String> a=new ArrayList<String>();
            BufferedReader[] reader = new BufferedReader[2];
            String[] buffer = new String[2];
            for (int i = 0; i < 2; i++) {
                try {
                    reader[i] = new BufferedReader(new FileReader(path.getInputPath(num, tempFiles.get(i), mergedTimes, alpha)));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            //读取文件并归并
            try {
                String[] strings = MergeSort.merge2way(readFromFile(reader[0]), readFromFile(reader[1]));
                String outputPath = path.getOutputPath(alpha, num, tempNum, mergedTimes + 1);
                path.getTempFiles().get(mergedTimes + 1).get(alpha - 'a').add(outputPath);
//                a.add(outputPath);
                Writer out = new FileWriter(outputPath);
                for (String s : strings) {
                    out.write(s);
                    out.flush();
                }
                out.close();
//                for(String s:tempFiles){
//                    new File(s).delete();
//                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 1;
        }

        int mid1 = tempFiles.size() / 2;
        int mid2 = mid1 * 2;
        List<Integer> left = tempFiles.subList(0, mid1);
        List<Integer> mid= tempFiles.subList(mid1,mid2);
        List<Integer> right = tempFiles.subList(mid1, tempFiles.size());
        TempFileMerge TaskL = new TempFileMerge(alpha, path, left, num, tempNum, mergedTimes);
        TempFileMerge TaskM=new TempFileMerge(alpha,path,mid,num,tempNum,mergedTimes);
        TempFileMerge TaskR = new TempFileMerge(alpha, path, right, num, tempNum, mergedTimes);
        invokeAll(TaskL, TaskR,TaskM);
//        a.addAll(TaskM.join());
        return TaskL.join() + TaskR.join()+TaskM.join();
    }

    private String[] readFromFile(BufferedReader b) throws IOException {
        String line = "";
        ArrayList<String> buffer = new ArrayList<>();

        while ((line = b.readLine()) != null) {
            buffer.add(line + '\n');
        }
        String[] str = new String[buffer.size()];
        buffer.toArray(str);
        return str;
    }

}

