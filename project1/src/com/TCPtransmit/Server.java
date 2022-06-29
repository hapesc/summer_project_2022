package com.TCPtransmit;

import com.multi_thread_sort.GetFilePath;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private  ServerSocket server=null;
    private int port;
    private GetFilePath path;

    public Server(int port, GetFilePath path) {
        this.port = port;
        this.path = path;
    }

    /**
     * 开始监听对应接口，每建立一个连接就开启一个线程，负责与client的专门连接
     * @throws IOException
     */
    public void serverStart() throws IOException {

        server=new ServerSocket(port);
        Socket socket=null;
        while (true){
            socket=server.accept();
            Thread t=new Thread(new SeverThread(socket,path));
            t.start();
        }
    }
}
