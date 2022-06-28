package com.TCPtransmit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
// 服务器端程序
public class Transmit {
    public static final String[] IPadress={"",
                                            "10.251.134.43",
                                            "10.251.134.44",
                                            "10.251.134.112",
                                            "10.251.134.66",
                                            "10.251.134.67",
                                            "10.251.134.68",
                                            "10.251.134.79",
                                            "10.251.134.80"};
    public static void main(String[] args) throws IOException {
//        ServerSocket server=new ServerSocket(4700);
        try{
            ServerSocket server=null;
            try{
                server=new ServerSocket(4700);
            }catch(Exception e) {
                System.out.println("can not listen to:"+e);
            }
            Socket socket=null;
            try{
                socket=server.accept();
            }catch(Exception e) {
                System.out.println("Error."+e);
            }
            String line;
            BufferedReader is=new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            BufferedReader sin=new BufferedReader(new  InputStreamReader(System.in));
            System.out.println("Client:"+is.readLine());
            line=sin.readLine();
            while(!line.equals("bye")){
                os.println(line);
                //刷新输出流，使Client马上收到该字符串
                os.flush();
                //在系统标准输出上打印读入的字符串
                System.out.println("Server:"+line);
                //从Client读入一字符串，并打印到标准输出上
                System.out.println("Client:"+is.readLine());
                //从系统标准输入读入一字符串
                line=sin.readLine();
            }  //继续循环
            os.close(); //关闭Socket输出流
            is.close(); //关闭Socket输入流
            socket.close(); //关闭Socket
            server.close(); //关闭ServerSocket
        }catch(Exception e){
            System.out.println("Error:"+e);
        }
        }

}


