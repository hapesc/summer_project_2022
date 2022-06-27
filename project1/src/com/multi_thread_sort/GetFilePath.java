package com.multi_thread_sort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GetFilePath {
    public  String[] TempPaths=new String[26];
    ArrayList<ArrayList<String>> tempFiles=new ArrayList<>(26);

    public GetFilePath() {
        for (int i = 0; i < 26; i++) {
            tempFiles.add(i,new ArrayList<>());
            String tempPath = "/Users/michael-liang/Desktop/IO_Test/result";
            char alpha = (char) ('a' + i);
            tempPath = tempPath + alpha + "Temp/";
            TempPaths[i] = tempPath;
            Path tempDir = Paths.get(tempPath);
            if (!new File(tempPath).isDirectory()) {
                try {
                    Files.createDirectory(tempDir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * 获得"dataXX.txt"或"resultaTemp0.txt"的文件地址
     * @param num 文件序号
     * @param temp  -1代表data1-40，大于0代表临时文件序号
     * @param alpha 临时文件首字母
     * @return  对应文件路径
     */
    public  String getInputPath(int num,int temp,char alpha){
        String dir="/Users/michael-liang/Desktop/IO_Test/";
        if(temp==-1) {
            if (num < 10)
                return dir+"data" + "0" + num + ".txt";
            else
                return dir+"data" + num + ".txt";
        }else if(temp>=0){
            return dir+"result"+alpha+"Temp"+num+".txt";
        }
        return null;
    }


    /**
     *
     * @param alpha 字符串首字母
     * @param num -1代表最终输出文件，1-40代表临时文件
     * @param num2 代表每一个batch产生的临时文件序号
     * @return  对应文件路径
     */
    public  String getOutputPath(char alpha,int num,int num2){
        String outPath=null;
        if(num==-1) {
            String outputDir="/Users/michael-liang/Desktop/Result/";
            Path tempDir = Paths.get(outputDir);
            if (!new File(outputDir).isDirectory()) {
                try {
                    Files.createDirectory(tempDir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            outPath = "/Users/michael-liang/Desktop/Result/result" + alpha;
        }

        if (num>0) {
            outPath=TempPaths[alpha-'a']+"result"+alpha+num+"_"+num2;
        }
        return outPath+".txt";
    }

    public String getTempFile(char alpha,int num,int num2){
        return TempPaths[alpha-'a']+"result"+alpha+num+"_"+num2+".txt";
    }
}
