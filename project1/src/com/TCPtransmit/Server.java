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

    public void serverStart(int port) throws IOException {
        this.port = port;
        server=new ServerSocket(port);
        Socket socket=null;
        while (true){
            socket=server.accept();
            Thread t=new Thread(new SeverThread(socket,path));
            t.start();
        }
    }
}
