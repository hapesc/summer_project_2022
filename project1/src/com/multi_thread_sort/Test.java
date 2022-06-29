package com.multi_thread_sort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args){
        GetFilePath path=GetFilePath.getFilePath();
        String filePath = path.getInputPath(1, -1, 0, '0');
        System.out.println(filePath);
        int batchSize=1000000;
        ArrayList<String[]> data=new ArrayList<>();
        data.add(new String[batchSize]);
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filePath));
            String line=null;
            int cnt=0;

            while ((line=bf.readLine())!=null){
                if(cnt<batchSize)
                    data.get(data.size()-1)[cnt++]=line;
                else{
                    cnt=0;
                    data.add(new String[batchSize]);
                    data.get(data.size()-1)[cnt++]=line;
                }

            }

            FileWriter out=new FileWriter(path.getOutputPath('a',1,1,0));
            for(int i=0;i<data.size();i++) {
                for (String s : data.get(i))
                    out.write(s + "\n");
            }
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
