package com.multi_thread_sort;

public class Outpath {
    public static String getOutPath(char alpha,int num){
        String outPath="";
        if(num==0)
            outPath="result";
        else if (num>0) {
            outPath="result"+num;
        }
        return outPath+alpha+".txt";
    }
}
