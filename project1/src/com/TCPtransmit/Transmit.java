package com.TCPtransmit;

import com.multi_thread_sort.GetFilePath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

// 服务器端程序
public class Transmit {
    private static final HashMap<Character,String> IPmap=new HashMap<>();
    private static final HashMap<Character,Integer> clientNames=new HashMap<>();
    private GetFilePath path;
    private int port;
    public static final String[] IPadress={"",
            "10.251.134.43",
            "10.251.134.44",
            "10.251.134.112",
            "10.251.134.66",
            "10.251.134.67",
            "10.251.134.68",
            "10.251.134.79",
            "10.251.134.80"};

    public Transmit(int port,GetFilePath path) {
        this.path=path;
        this.port=port;
        for(int i=0;i<8;i++){
            char alpha=(char)('a'+i);
            IPmap.put(alpha,IPadress[i/4+1]);
            clientNames.put(alpha,i/4+1);
        }
        for(int i=8,cnt=0;i<26;i++,cnt++){
            char alpha=(char)('a'+i);
            IPmap.put(alpha,IPadress[3+(cnt/3)]);
            clientNames.put(alpha,cnt/3+3);
        }

    }
    public void start(){
        //todo 为文件传输写一个启动方法
        Server ss=new Server(port,path);
        try {
            ss.serverStart();
        }catch (IOException e){
            e.printStackTrace();
        }
//        for(int i=0;i<26;i++){
//            char alpha=(char)('a'+i);
//            int clientName=(Integer)clientNames.get(alpha);
//            new Thread(new Client(IPmap.get(alpha), port,alpha,clientName,path)).start();
//        }
    }
}


