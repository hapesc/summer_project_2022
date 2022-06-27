package com.multi_thread_sort;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;



public class TempFileMerge implements Runnable {
    private ArrayList<String> tempFiles;
    private GetFilePath path;
    private FailedTree<String> tree;
    private char alpha;
    private int Mode;
    public TempFileMerge(char alpha,ArrayList<String> tempFiles, GetFilePath path,int Mode) {
        this.tempFiles = tempFiles;
        this.path = path;
        this.alpha=alpha;
        this.Mode=Mode;
    }

    @Override
    public void run() {
        try(Writer output=new FileWriter(path.getOutputPath(alpha,-1,Mode))){
            ArrayList<BufferedReader> bufferedReaders=new ArrayList<>(tempFiles.size());
            for(int i=0;i<tempFiles.size();i++){
                bufferedReaders.add(i,new BufferedReader(new FileReader(tempFiles.get(i))));
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
}
