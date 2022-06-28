package com.multi_thread_sort;

public class Test {
    public static void main(String[] args){
        GetFilePath p=GetFilePath.getFilePath();
        System.out.println(p.getInputPath(1,-1,0,'a'));
        System.out.println((char)('a'+25));
    }
}
