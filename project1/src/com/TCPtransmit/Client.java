package com.TCPtransmit;

import com.multi_thread_sort.GetFilePath;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

public class Client {
    private Socket client=null;
    private String IP;
    private int port;
    private char alpha;
    private int clientName;
    private GetFilePath path;

    public Client(String IP, int port, char alpha, int clientName,GetFilePath path) {
        this.IP = IP;
        this.port = port;
        this.alpha = alpha;
        this.clientName = clientName;
        this.path=path;
    }

    public void clientStart() throws IOException {
        this.client=new Socket(IP,port);
        OutputStream os=client.getOutputStream();
        Writer writer=new OutputStreamWriter(os);
        //先将主机序号和文件名传输到server上
        writer.write(clientName+'\n');
        writer.flush();
        writer.write(alpha+'\n');
        writer.flush();
        String fileName=path.getInputPath(0,-1,0,alpha);
    }
}
